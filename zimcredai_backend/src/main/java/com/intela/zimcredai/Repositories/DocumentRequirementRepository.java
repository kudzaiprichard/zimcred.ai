package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.DocumentRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRequirementRepository extends JpaRepository<DocumentRequirement, Integer> {
}
