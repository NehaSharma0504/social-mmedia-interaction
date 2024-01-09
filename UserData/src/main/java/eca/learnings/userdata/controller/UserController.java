package eca.learnings.userdata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eca.learnings.userdata.dao.UserDao;
import eca.learnings.userdata.model.User;
import eca.learnings.userdata.service.KafkaProducer;
import eca.learnings.userdata.service.UserService;
import eca.learnings.userdata.uniqueIdentifier.UniqueIdGenerator;
import eca.learnings.userdata.utils.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @Slf4j
    @RestController
    @RequestMapping("/users")
    public class UserController {
        private final UserDao postDAO;
        @Autowired
        UserService userService;
        @Autowired
        KafkaProducer kafkaProducer;
        ObjectMapper objectMapper = new ObjectMapper();
        @Autowired
        public UserController(UserDao postDAO) {
            this.postDAO = postDAO;
        }
        RateLimiter rateLimiter = new RateLimiter(10, 1);
        @PostMapping("/addUser")
        public ResponseEntity<String> createUser(@RequestBody User user) {
            // Acquire a permit before processing the request
            rateLimiter.processRequest(1);

            try {
                user.setUserId(UniqueIdGenerator.generateUniqueId());
                //send to kafka
                try{
                    kafkaProducer.send(objectMapper.writeValueAsString(user));
                }
                catch (KafkaException e){
                   log.error(e.getMessage());
                }
                postDAO.saveData(user.toBson(), "user_data");
                return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } finally {
                // Release the permit after processing the request
                rateLimiter.releasePermits(1);
            }
        }

        @PutMapping("updateUser/{userId}")
        public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, @RequestBody User updatedUser) {
            User user = userService.getUserById(userId);
            if (user!=null) {
                updatedUser.setUserId(userId);
                postDAO.saveData(updatedUser.toBson(),"user_data");
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @GetMapping("/allUsers")
        public List<? super User> getUsers() {
           return postDAO.getUser("user_data", User.class);
        }

        @GetMapping("/{userId}")
        public User getUserById(@PathVariable("userId") String userId) {
            return postDAO.getUserById(userId, "user_data");
        }

    }
