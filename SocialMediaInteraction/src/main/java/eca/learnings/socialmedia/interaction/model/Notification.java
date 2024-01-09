package eca.learnings.socialmedia.interaction.model;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private String type;
    private String message;

    public Notification(Long id, String type, String message) {
        this.id = id;
        this.type = type;
        this.message = message;
    }

    private Follower follower;
}
