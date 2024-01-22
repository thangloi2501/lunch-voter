package com.gtech.service;

import static com.gtech.utils.VoteUtils.RANDOM;
import static com.gtech.utils.VoteUtils.TOPIC_VOTE;

import com.gtech.db.entity.UserVote;
import com.gtech.dto.api.VoteItem;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VoteHelper {
  static boolean hasVotes(@NonNull List<UserVote> votes) {
    return votes.stream()
        .anyMatch(vote -> Objects.nonNull(vote.getVoteValue()));
  }

  static UserVote chooseRandomVote(List<UserVote> votes) {
    int randomIndex = RANDOM.nextInt(votes.size());
    return votes.get(randomIndex);
  }

  static String getTopicDestination(String code) {
    return String.format("%s/%s", TOPIC_VOTE, code);
  }

  static List<VoteItem> toVoteItems(List<UserVote> userVotes) {
    return userVotes
        .stream()
        .map(userVote -> VoteItem.builder()
            .name(userVote.getName())
            .voteValue(userVote.getVoteValue())
            .updatedAt(userVote.getUpdatedAt())
            .build())
        .toList();
  }
}
