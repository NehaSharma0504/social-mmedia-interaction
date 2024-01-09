package eca.learnings.socialmedia.interaction.controller;


import eca.learnings.socialmedia.interaction.model.FollowerRequest;
import eca.learnings.socialmedia.interaction.model.Notification;
import eca.learnings.socialmedia.interaction.service.FollowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping
    public ResponseEntity<Void> addFollower(@RequestBody FollowerRequest request) {
        followerService.addFollower(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/removeFollower/{followerId}/from/{followeeId}/{accountName}")
    public ResponseEntity<Void> removeFollower(@PathVariable String followerId, @PathVariable String followeeId, @PathVariable String accountName) {
        followerService.removeFollower(followerId, followeeId, accountName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<Notification>> getFollowerNotifications(@PathVariable("id") Long followerId) {
        List<Notification> notifications = followerService.getFollowerNotifications(followerId);
        return ResponseEntity.ok(notifications);
    }
}

