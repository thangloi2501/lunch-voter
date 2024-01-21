package com.gtech.db.entity;

import com.gtech.db.listener.Auditable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "user_vote")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVote extends AbstractEntity implements Auditable {

  @NaturalId
  @Column(name = "user_code", nullable = false, length = 36, unique = true)
  private String userCode;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 200)
  private String voteValue;

  @Builder.Default
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt = LocalDateTime.now();

  @Column(name="vote_session_id", updatable=false, nullable=false)
  private Long voteSessionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @JoinColumn(name = "vote_session_id", referencedColumnName = "id", updatable = false, insertable = false)
  private VoteSession voteSession;

  @Override
  public int hashCode() {
    return Objects.hashCode(this.userCode);
  }

  @Override
  public boolean equals(Object other) {
      if (this == other) {
          return true;
      }

      if (!(other instanceof UserVote that)) {
          return false;
      }

    return Objects.equals(that.userCode, this.userCode);
  }
}
