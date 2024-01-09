package eca.learnings.socialmedia.interaction.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
@Setter
public class PostRequest {
    private ObjectId id;
    @NonNull
    private String accountId;
    @NonNull
    private String authorId; // userId
    private String author; // Username of the user creating the post
    private String content; // Content of the post
    private List<String> tags; // List of hashtags associated with the post
    private String title;
    // Any other fields related to the post, such as media, location, etc.

    // Constructors, getters, and setters
}
