package eca.learnings.socialmedia.interaction.repository;


import eca.learnings.socialmedia.interaction.model.Follower;
import eca.learnings.socialmedia.interaction.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, Long> {
    List<Notification> findByFollower(Follower follower);
}

