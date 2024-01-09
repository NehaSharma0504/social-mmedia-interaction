package eca.learnings.socialmedia.interaction.repository;

import eca.learnings.socialmedia.interaction.model.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowerRepository extends MongoRepository<Follower, Long> {
    // Custom query methods if needed
}

