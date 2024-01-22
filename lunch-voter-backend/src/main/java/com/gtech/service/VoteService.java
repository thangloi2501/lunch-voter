package com.gtech.service;

import com.gtech.db.entity.UserVote;
import com.gtech.db.entity.VoteSession;
import com.gtech.db.repository.UserVoteRepo;
import com.gtech.db.repository.VoteSessionRepo;
import com.gtech.dto.api.CreateRequest;
import com.gtech.dto.api.CreateResponse;
import com.gtech.dto.api.EndRequest;
import com.gtech.dto.api.JoinRequest;
import com.gtech.dto.api.JoinResponse;
import com.gtech.dto.api.LeaveRequest;
import com.gtech.dto.api.SubmitRequest;
import com.gtech.dto.api.VoteItem;
import com.gtech.dto.message.UserInfoMessage;
import com.gtech.dto.message.VoteInfoMessage;
import com.gtech.exception.ApiException;
import com.gtech.utils.AppUtils;
import com.gtech.utils.VoteUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

  private final VoteSessionRepo voteSessionRepo;
  private final UserVoteRepo userVoteRepo;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  public VoteService(
      VoteSessionRepo voteSessionRepo,
      UserVoteRepo userVoteRepo,
      SimpMessagingTemplate simpMessagingTemplate) {
    this.voteSessionRepo = voteSessionRepo;
    this.userVoteRepo = userVoteRepo;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  @Transactional
  public CreateResponse createSession(CreateRequest createRequest) {
    final var code = VoteUtils.generateCode();
    final var userCode = VoteUtils.generateCode();

    final var voteSession = voteSessionRepo.save(
        VoteSession.builder()
            .code(code)
            .creatorCode(userCode)
            .build());

    userVoteRepo.save(UserVote.builder()
        .name(createRequest.getName())
        .userCode(userCode)
        .voteSessionId(voteSession.getId())
        .build());

    return CreateResponse.builder()
        .code(code)
        .userCode(userCode)
        .build();
  }

  @Transactional
  public JoinResponse joinSession(JoinRequest joinRequest) {
    final var voteSession = validateAndGetSession(joinRequest.getCode());

    final var userCode = VoteUtils.generateCode();
    userVoteRepo.save(UserVote.builder()
        .name(joinRequest.getName())
        .userCode(userCode)
        .voteSessionId(voteSession.getId())
        .build());

    simpMessagingTemplate.convertAndSend(
        VoteHelper.getTopicDestination(joinRequest.getCode()),
        UserInfoMessage.builder()
            .name(joinRequest.getName())
            .action(AppUtils.getMessage("message.user.join-session"))
            .updatedAt(LocalDateTime.now())
            .build());

    return JoinResponse.builder()
        .code(voteSession.getCode())
        .userCode(userCode)
        .build();
  }

  @Transactional
  public void submitVote(SubmitRequest submitRequest) {
    final var voteSession = validateAndGetSession(submitRequest.getCode());

    final var userVote = userVoteRepo.findOneByUserCode(submitRequest.getUserCode())
        .orElseThrow(() -> ApiException.notFound("User vote", "userCode", submitRequest.getUserCode()));

    if (Objects.isNull(userVote.getVoteSession())
        || !Objects.equals(userVote.getVoteSession().getId(), voteSession.getId())) {
      throw ApiException.from(HttpStatus.BAD_REQUEST, "vote.error.user-vote-does-not-match");
    }

    userVote.setVoteValue(submitRequest.getVoteValue());
    userVoteRepo.saveAndFlush(userVote);

    simpMessagingTemplate.convertAndSend(
        VoteHelper.getTopicDestination(submitRequest.getCode()),
        VoteInfoMessage.builder()
            .voteItems(VoteHelper.toVoteItems(voteSession.getUserVotes()))
            .build());
  }

  @Transactional
  public void endSession(EndRequest endRequest) {
    final var voteSession = validateAndGetSession(endRequest.getCode());

    if (!Objects.equals(endRequest.getUserCode(), voteSession.getCreatorCode())) {
      throw ApiException.from(HttpStatus.UNAUTHORIZED, "vote.error.user-not-allow-end-session");
    }

    final var votes = voteSession.getUserVotes();
    if (!VoteHelper.hasVotes(votes)) {
      throw ApiException.from(HttpStatus.BAD_REQUEST, "vote.error.empty-user-vote-list");
    }

    final UserVote finalVote = VoteHelper.chooseRandomVote(votes);

    voteSession.setFinalUserVoteId(finalVote.getId());
    voteSessionRepo.save(voteSession);

    final var creatorName = votes.stream()
        .filter(vote -> Objects.equals(vote.getUserCode(), endRequest.getUserCode()))
        .map(UserVote::getName)
        .findFirst()
        .orElse(endRequest.getUserCode());

    simpMessagingTemplate.convertAndSend(
        VoteHelper.getTopicDestination(endRequest.getCode()),
        UserInfoMessage.builder()
            .name(creatorName)
            .action(AppUtils.getMessage("message.user.end-session"))
            .updatedAt(LocalDateTime.now())
            .build());
  }

  @Transactional
  public List<VoteItem> getVotes(String code) {
    final var voteSession = voteSessionRepo.findOneByCode(code)
        .orElseThrow(() -> ApiException.notFound("Vote session", "code", code));

    return VoteHelper.toVoteItems(voteSession.getUserVotes());
  }

  @Transactional
  public void leaveSession(LeaveRequest leaveRequest) {
    final var voteSession = validateAndGetSession(leaveRequest.getCode());

    final var userVote = userVoteRepo.findOneByUserCode(leaveRequest.getUserCode())
        .orElseThrow(() -> ApiException.notFound("User vote", "userCode", leaveRequest.getUserCode()));

    if (Objects.equals(voteSession.getCreatorCode(), userVote.getUserCode())) {
      throw ApiException.from(HttpStatus.BAD_REQUEST, "vote.leave.error.creator-cannot-leave-session");
    }

    userVoteRepo.delete(userVote);

    simpMessagingTemplate.convertAndSend(
        VoteHelper.getTopicDestination(leaveRequest.getCode()),
        UserInfoMessage.builder()
            .name(userVote.getName())
            .action(AppUtils.getMessage("message.user.leave-session"))
            .updatedAt(LocalDateTime.now())
            .build());
  }

  private VoteSession validateAndGetSession(String code) {
    final var voteSession = voteSessionRepo.findOneByCode(code)
        .orElseThrow(() -> ApiException.notFound("Vote session", "code", code));

    if (Objects.nonNull(voteSession.getFinalUserVote())) {
      throw ApiException.from(HttpStatus.BAD_REQUEST, "vote.error.session-ended");
    }

    return voteSession;
  }
}
