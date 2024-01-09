package eca.learnings.socialmedia.interaction.repository;

import eca.learnings.socialmedia.interaction.model.PostRequest;
import eca.learnings.socialmedia.interaction.model.SocialMediaPost;

import java.util.List;

public interface SocialMediaPostsRepository {

    void createPost(PostRequest request);
    List<? super SocialMediaPost> getPosts(String userId, String accountId);
}
