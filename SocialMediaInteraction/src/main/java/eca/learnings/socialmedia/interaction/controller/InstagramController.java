package eca.learnings.socialmedia.interaction.controller;


import eca.learnings.socialmedia.interaction.model.PostRequest;
import eca.learnings.socialmedia.interaction.model.SocialMediaPost;
import eca.learnings.socialmedia.interaction.service.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

import static eca.learnings.socialmedia.interaction.utils.Utility.isValidUser;

@RestController
@RequestMapping("/instagram")
public class InstagramController {

    @Autowired
    public InstagramService service;
    @PostMapping("/createPost")
    public ResponseEntity<String> createPost(@RequestBody PostRequest request) {
        if(isValidUser(request.getAuthorId(),request.getAccountId())){
              service.createPost(request);
              return ResponseEntity.ok("Post created successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data. Please provide valid user id and account id.");
    }
    @GetMapping("/getInstagramPosts/{userId}/{accountId}")
    public List<? super SocialMediaPost> getPosts(@PathParam("userId") String userId, @PathParam("accountId") String accountId) {
        // Fetch social media posts using the get method from the DAO
        return  service.getPosts(userId, accountId);

    }

}
