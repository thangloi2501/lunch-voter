package com.gtech.service;

import static com.gtech.utils.VoteUtils.RANDOM;
import static com.gtech.utils.VoteUtils.TOPIC_VOTE;

import com.gtech.db.entity.UserVote;
import com.gtech.dto.api.VoteItem;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

@UtilityClass
public class VoteHelper {

  static boolean allVoted(@NonNull List<UserVote> votes) {
    return !CollectionUtils.isEmpty(votes)
        && votes.stream()
        .allMatch(vote -> Objects.nonNull(vote.getVoteValue()));
  }

  static UserVote chooseRandomVote(List<UserVote> votes) {
    int randomIndex = RANDOM.nextInt(votes.size());
    return votes.get(randomIndex);
  }

  static String getTopicDestination(String code) {
    return String.format("%s/%s", TOPIC_VOTE, code);
  }

  static List<VoteItem> toVoteItems(
      List<UserVote> userVotes, Optional<UserVote> finalVote) {
    return userVotes
        .stream()
        .map(userVote -> VoteItem.builder()
            .name(userVote.getName())
            .voteValue(userVote.getVoteValue())
            .updatedAt(userVote.getUpdatedAt())
            .isFinal(finalVote.isPresent()
                && Objects.equals(finalVote.get().getUserCode(), userVote.getUserCode()))
            .build())
        .toList();
  }
}
