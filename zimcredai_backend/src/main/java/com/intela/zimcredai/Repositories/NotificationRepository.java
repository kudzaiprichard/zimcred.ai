package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.Notification;
import com.intela.zimcredai.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // Find notifications by User and seen status
    List<Notification> findByUsersAndSeen(Set<User> users, Boolean seen);

    // Count notifications by User and seen status
    long countByUsersAndSeen(Set<User> users, Boolean seen);
}
