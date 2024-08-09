package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.PendingUserChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PendingUserChangeRepository extends JpaRepository<PendingUserChange, Integer> {
    List<PendingUserChange> findByUserId(Long id);
    Optional<PendingUserChange> findByToken(String token);
}
