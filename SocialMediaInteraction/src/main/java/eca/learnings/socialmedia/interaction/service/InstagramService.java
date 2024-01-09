package eca.learnings.socialmedia.interaction.service;

import eca.learnings.socialmedia.interaction.dao.SocialMediaDao;
import eca.learnings.socialmedia.interaction.model.PostRequest;
import eca.learnings.socialmedia.interaction.model.SocialMediaPost;
import eca.learnings.socialmedia.interaction.repository.SocialMediaPostsRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InstagramService implements SocialMediaPostsRepository {
    private final SocialMediaDao postDAO;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    public InstagramService(SocialMediaDao postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public void createPost(PostRequest request) {
        // Create the Social Media Post using the Builder pattern
        SocialMediaPost post = new SocialMediaPost.PostBuilder(request.getId() , request.getTitle(), request.getContent())
                .author(request.getAccountId(),request.getAuthor(),request.getAuthorId())
                .tags(request.getTags())
                .build();
        // Convert the SocialMediaPost object to a MongoDB Document using the toBson() method
        Document postDocument = post.toBson();
        // Insert the postDocument into the collection
        kafkaTemplate.send("social_media_posts", postDocument.toJson().toString());
        postDAO.saveData(postDocument,"social_media_posts");
    }

    @Override
    public List<? super SocialMediaPost> getPosts(String userId, String accountId) {
        return postDAO.getPosts("social_media_posts", SocialMediaPost.class);
    }
}
