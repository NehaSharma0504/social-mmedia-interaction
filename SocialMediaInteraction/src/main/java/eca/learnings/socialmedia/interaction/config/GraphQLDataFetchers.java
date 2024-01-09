package eca.learnings.socialmedia.interaction.config;

import eca.learnings.socialmedia.interaction.model.Notification;
import eca.learnings.socialmedia.interaction.model.Post;
import eca.learnings.socialmedia.interaction.repository.PostRepository;
import eca.learnings.socialmedia.interaction.service.NotificationService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraphQLDataFetchers {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationService notificationService;

    public DataFetcher<List<Notification>> getUserNotificationsDataFetcher() {
        return dataFetchingEnvironment -> {
            Long userId = Long.parseLong(dataFetchingEnvironment.getArgument("userId"));
            return notificationService.getUserNotifications(userId);
        };
    }

    public DataFetcher<List<Post>> getAllPostsDataFetcher() {
        return dataFetchingEnvironment -> postRepository.findAll();
    }

    public DataFetcher<Post> getPostByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            Long postId = Long.parseLong(dataFetchingEnvironment.getArgument("id"));
            return postRepository.findById(postId).orElse(null);
        };
    }
}
