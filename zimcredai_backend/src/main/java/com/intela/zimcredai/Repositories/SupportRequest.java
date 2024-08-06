package com.intela.zimcredai.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRequest extends JpaRepository<SupportRequest, Integer> {
}
