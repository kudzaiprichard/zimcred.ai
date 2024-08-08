package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    List<UserNotification> findByUserIdAndSeen(Long user_id, Boolean seen);
    long countByUserIdAndSeen(Long user_id, Boolean seen);
    UserNotification findByUserIdAndNotificationId(Long user_id, Integer notification_id);
}
