package eca.learnings.socialmedia.interaction.controller;


import eca.learnings.socialmedia.interaction.model.Event;
import eca.learnings.socialmedia.interaction.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        // Save the event to the database using the EventRepository
        return eventRepository.save(event);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        // Retrieve the event from the database using the EventRepository
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

