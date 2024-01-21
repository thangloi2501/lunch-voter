package com.gtech.service;

import static com.gtech.utils.VoteUtils.RANDOM;
import static com.gtech.utils.VoteUtils.TOPIC_VOTE;

import com.gtech.db.entity.UserVote;
import com.gtech.db.entity.VoteSession;
import com.gtech.db.repository.UserVoteRepo;
import com.gtech.db.repository.VoteSessionRepo;
import com.gtech.exception.ApiException;
import com.gtech.model.CreateRequest;
import com.gtech.model.CreateResponse;
import com.gtech.model.EndRequest;
import com.gtech.model.JoinRequest;
import com.gtech.model.JoinResponse;
import com.gtech.model.SubmitRequest;
import com.gtech.model.VoteResponse;
import com.gtech.utils.AppUtils;
import com.gtech.utils.VoteUtils;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.NonNull;
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
        getTopicDestination(joinRequest.getCode()),
        VoteResponse.builder()
            .content(AppUtils.getMessage("vote.user-join", joinRequest.getName()))
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
    userVoteRepo.save(userVote);

    simpMessagingTemplate.convertAndSend(
        getTopicDestination(submitRequest.getCode()),
        VoteResponse.builder()
            .content(AppUtils.getMessage("vote.user-submit",
                userVote.getName(),
                submitRequest.getVoteValue()))
            .build());
  }

  @Transactional
  public void endSession(EndRequest endRequest) {
    final var voteSession = validateAndGetSession(endRequest.getCode());

    if (!Objects.equals(endRequest.getUserCode(), voteSession.getCreatorCode())) {
      throw ApiException.from(HttpStatus.UNAUTHORIZED, "vote.error.user-not-allow-end-session");
    }

    final var votes = voteSession.getUserVotes();
    if (!hasVotes(votes)) {
      throw ApiException.from(HttpStatus.BAD_REQUEST, "vote.error.empty-user-vote-list");
    }

    final UserVote finalVote = chooseRandomVote(votes);

    voteSession.setFinalUserVoteId(finalVote.getId());
    voteSessionRepo.save(voteSession);

    final var creatorName = votes.stream()
        .filter(vote -> Objects.equals(vote.getUserCode(), endRequest.getUserCode()))
        .map(UserVote::getName)
        .findFirst()
        .orElse(endRequest.getUserCode());

    simpMessagingTemplate.convertAndSend(
        getTopicDestination(endRequest.getCode()),
        VoteResponse.builder()
            .content(String.format("User %s end session. Final vote: %s",
                creatorName, finalVote.getVoteValue()))
            .build());
  }

  private boolean hasVotes(@NonNull List<UserVote> votes) {
    return votes.stream()
        .anyMatch(vote -> Objects.nonNull(vote.getVoteValue()));
  }

  private UserVote chooseRandomVote(List<UserVote> votes) {
    int randomIndex = RANDOM.nextInt(votes.size());
    return votes.get(randomIndex);
  }

  private String getTopicDestination(String code) {
    return String.format("%s/%s", TOPIC_VOTE, code);
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
