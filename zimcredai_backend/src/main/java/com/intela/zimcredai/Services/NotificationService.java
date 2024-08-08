package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.CustomerPortfolio;
import com.intela.zimcredai.Models.Notification;
import com.intela.zimcredai.Models.User;
import com.intela.zimcredai.Models.UserNotification;
import com.intela.zimcredai.Repositories.NotificationRepository;
import com.intela.zimcredai.Repositories.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.intela.zimcredai.Util.Util.getNullPropertyNames;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    // Create a new notification
    public Notification create(Notification notification, Set<User> users) {
        Notification savedNotification = notificationRepository.save(notification);
        for (User user : users) {
            UserNotification userNotification = UserNotification.builder()
                    .user(user)
                    .notification(savedNotification)
                    .seen(false)
                    .build();
            userNotificationRepository.save(userNotification);
        }
        return savedNotification;
    }

    // Fetch a notification by ID
    public Notification fetchById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
    }

    // Update an existing notification
    public Notification update(Integer id, Notification notification) {
        Notification dBNotification = this.fetchById(id);
        BeanUtils.copyProperties(notification, dBNotification, getNullPropertyNames(notification));
        return this.notificationRepository.save(dBNotification);
    }

    // Delete a notification by ID
    public void delete(Integer id) {
        Notification dbNotification = this.fetchById(id);
        notificationRepository.delete(dbNotification);
    }

    // Fetch all notifications
    public List<Notification> fetchAll() {
        return notificationRepository.findAll();
    }

    // Mark a notification as read for a specific user
    public void markAsRead(Integer notificationId, Long userId) {
        UserNotification userNotification = userNotificationRepository.findByUserIdAndNotificationId(userId, notificationId);
        if (userNotification != null) {
            userNotification.setSeen(true);
            userNotificationRepository.save(userNotification);
        }
    }

    // Fetch all unseen notifications for a specific user
    // Todo: check if user id should be long on testing
    public List<UserNotification> fetchUnseenNotificationsForUser(Long userId) {
        return userNotificationRepository.findByUserIdAndSeen(userId, false);
    }

    // Count all unseen notifications for a specific user
    public long countUnseenNotificationsForUser(Long userId) {
        return userNotificationRepository.countByUserIdAndSeen(userId, false);
    }
}
