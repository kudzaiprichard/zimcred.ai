package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.Coordinator;
import com.intela.zimcredai.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Integer> {
    Optional<Coordinator> findByUserId(Long id);
}
