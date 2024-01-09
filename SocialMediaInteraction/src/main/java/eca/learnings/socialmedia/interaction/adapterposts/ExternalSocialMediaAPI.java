package eca.learnings.socialmedia.interaction.adapterposts;

import java.util.List;

//Adapter Design Pattern
public interface ExternalSocialMediaAPI {
    List<String> getPosts();
}
