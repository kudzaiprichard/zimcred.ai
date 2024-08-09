package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    List<UserNotification> findByUserIdAndSeen(Long user_id, Boolean seen);
    long countByUserIdAndSeen(Long user_id, Boolean seen);

    @Query("SELECT u FROM user_notifications u WHERE u.user.id = :userId AND u.notification.id = :notificationId")
    UserNotification findByUserIdAndNotificationId(@Param("userId") Long userId, @Param("notificationId") Integer notificationId);

}
