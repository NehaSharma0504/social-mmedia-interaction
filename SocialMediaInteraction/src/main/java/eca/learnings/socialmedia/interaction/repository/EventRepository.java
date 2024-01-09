package eca.learnings.socialmedia.interaction.repository;

import eca.learnings.socialmedia.interaction.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event,Long> {
}
