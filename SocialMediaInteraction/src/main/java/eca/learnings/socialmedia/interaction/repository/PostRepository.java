package eca.learnings.socialmedia.interaction.repository;

import eca.learnings.socialmedia.interaction.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    // Add other custom queries as needed
}
