package com.gtech.db.repository;

import com.gtech.db.entity.UserVote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoteRepo extends JpaRepository<UserVote, Long> {
  Optional<UserVote> findOneByUserCode(String userCode);
}
