package eca.learnings.socialmedia.interaction.adapterposts;


import eca.learnings.socialmedia.interaction.model.SocialMediaPost;

import java.util.List;

public interface SocialMediaDataProvider {
    List<SocialMediaPost> getPosts();
}
