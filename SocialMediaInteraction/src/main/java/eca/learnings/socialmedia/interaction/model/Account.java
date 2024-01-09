package eca.learnings.socialmedia.interaction.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@Document
public class Account {
    private String accountId;

    @Pattern(regexp = "^(facebook|twitter|instagram)$", message = "Account name must be either facebook, twitter, or instagram")
    private String accountName;
    private User user; // Reference to the User this account belongs to
}
