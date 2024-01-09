package eca.learnings.socialmedia.interaction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;


    @NoArgsConstructor
    @Getter
    @Setter
    public class Event {

        @Id
        private Long id;

        private String eventType;

        private Long userId;

        private LocalDateTime timestamp;
        public Event(String eventType, Long userId, LocalDateTime timestamp) {
            this.eventType = eventType;
            this.userId = userId;
            this.timestamp = timestamp;
        }
    }

