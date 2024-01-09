package eca.learnings.socialmedia.interaction.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class FollowerRequest {
    @Id
    private Long id;

    private Account accountDetails;

    private String followeeUserId;
}

