package eca.learnings.socialmedia.interaction.service;

import eca.learnings.socialmedia.interaction.model.Follower;
import eca.learnings.socialmedia.interaction.model.Notification;
import eca.learnings.socialmedia.interaction.repository.FollowerRepository;
import eca.learnings.socialmedia.interaction.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {
    public void sendNotification(Notification notification) {
        // Logic to send the notification
    }
    private final FollowerRepository followerRepository;
    private final NotificationRepository notificationRepository;

    // Simulated data - replace with actual database access
    private static final Map<Long, List<Notification>> userNotifications = new HashMap<>();

    static {
        userNotifications.put(1L, Arrays.asList(
                new Notification(1L, "LIKE", "John liked your post."),
                new Notification(2L, "COMMENT", "Jane commented on your photo.")
        ));
        // Add more sample data as needed
    }
    public NotificationService(FollowerRepository followerRepository, NotificationRepository notificationRepository) {
        this.followerRepository = followerRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getUserNotifications(Long userId) {
        return userNotifications.getOrDefault(userId, Collections.emptyList());
    }
    public List<Notification> getNotificationsForFollower(Long followerId) {
        List<Notification> notifications = new ArrayList<>();

        // Retrieve the follower entity from the repository
        Optional<Follower> optionalFollower = followerRepository.findById(followerId);
        if (optionalFollower.isPresent()) {
            Follower follower = optionalFollower.get();

            // Get the notifications associated with the follower
            notifications = notificationRepository.findByFollower(follower);
        }

        return notifications;
    }
}
