package com.gtech.db.repository;

import com.gtech.db.entity.VoteSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteSessionRepo extends JpaRepository<VoteSession, Long> {
  Optional<VoteSession> findOneByCode(String code);
}
