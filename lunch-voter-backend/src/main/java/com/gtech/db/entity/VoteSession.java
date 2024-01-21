package com.gtech.db.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "vote_session")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteSession extends AbstractEntity {

  @NaturalId
  @Column(nullable = false, length = 36, unique = true)
  private String code;

  @Column(name = "creator_code", nullable = false, length = 36)
  private String creatorCode;

  @Builder.Default
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "final_user_vote_id", insertable = false)
  private Long finalUserVoteId;

  @Builder.Default
  @LazyGroup("user_votes")
  @OneToMany(
      mappedBy = "voteSession",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<UserVote> userVotes = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY)
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(
      name = "final_user_vote_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private UserVote finalUserVote;

  @Override
  public int hashCode() {
    return Objects.hash(this.code);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof VoteSession that)) {
      return false;
    }

    return Objects.equals(that.code, this.code);
  }
}
