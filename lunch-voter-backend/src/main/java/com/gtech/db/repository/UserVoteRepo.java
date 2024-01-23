package com.gtech.db.repository;

import com.gtech.db.entity.UserVote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoteRepo extends JpaRepository<UserVote, Long> {
  Optional<UserVote> findOneByUserCode(String userCode);

  @Modifying
  @Query("delete from UserVote uv where uv.id = ?1")
  void delete(Long id);
}
