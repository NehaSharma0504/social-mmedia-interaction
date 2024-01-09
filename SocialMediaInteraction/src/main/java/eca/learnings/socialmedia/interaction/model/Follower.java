package eca.learnings.socialmedia.interaction.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Follower {
    @Id
    private Long id;

    private Long followerId;

    // Constructors, getters, and setters

    public Follower() {
    }

    public Follower(Long followerId) {
        this.followerId = followerId;
    }

}

