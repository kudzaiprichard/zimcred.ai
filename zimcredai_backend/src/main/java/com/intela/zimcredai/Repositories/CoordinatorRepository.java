package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Integer> {
}
