package eca.learnings.socialmedia.interaction.service;

import eca.learnings.socialmedia.interaction.model.FollowerRequest;
import eca.learnings.socialmedia.interaction.model.Notification;
import eca.learnings.socialmedia.interaction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowerService {
    private final NotificationService notificationService;

    private final MongoOperations mongoOperations;

    @Autowired
    public FollowerService(MongoOperations mongoOperations, NotificationService notificationService) {
        this.mongoOperations = mongoOperations;
        this.notificationService = notificationService;
    }

    public void addFollower(FollowerRequest request) {
        User followee = mongoOperations.findById(request.getFolloweeUserId(), User.class);
        if (followee != null) {
            // Check if the followeeUserId is not already in the follower's followers list
            String platformName = request.getAccountDetails().getAccountName();
            List<String> platformFollowers = followee.getFollowersMap().getOrDefault(platformName, new ArrayList<>());
            if (!platformFollowers.contains(request.getAccountDetails().getUser().getUserId())) {
                platformFollowers.add((request.getAccountDetails().getUser().getUserId()));
                mongoOperations.save(followee, "user_data");
            }
        }
    }

    public List<Notification> getFollowerNotifications(Long followerId) {
        // Logic to get real-time notifications for a follower
        return notificationService.getNotificationsForFollower(followerId);
    }

    public void removeFollower(String followerId, String followeeId, String accountName) {
        // Fetch the user document for the user with followeeId
        User followeeUser = mongoOperations.findById(followeeId, User.class);

        if (followeeUser != null) {
            // Check if the followerId is present in the followers list
            List<String> platformFollowers = followeeUser.getFollowersMap().get(accountName);
            if (platformFollowers.contains(followerId)) {
                // Remove the followerId from the followers list
                platformFollowers.remove(followerId);

                // Save the updated user back to the database
                mongoOperations.save(followeeUser, "user_data");
            }
        }
    }
}

