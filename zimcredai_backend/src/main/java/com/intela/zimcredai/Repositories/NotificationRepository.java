package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndSeen(Long user_id, Boolean seen);
    long countByUserIdAndSeen(Long user_id, Boolean seen);
}
