package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.SupportRequest;
import com.intela.zimcredai.Models.SupportRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, Integer> {
    List<SupportRequest> findByStatus(SupportRequestStatus status);
    List<SupportRequest> findByUserId(Long user_id);
}
