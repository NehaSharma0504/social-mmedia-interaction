package eca.learnings.socialmedia.interaction.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // getters and setters
}