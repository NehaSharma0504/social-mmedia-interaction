package eca.learnings.userdata.service;

import eca.learnings.userdata.model.User;
import eca.learnings.userdata.repository.impl.MongoDBUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {

    @Autowired
    MongoDBUserRepository mongoDBUserRepository;

    public User getUserById(@PathVariable("userId") String userId) {
        return mongoDBUserRepository.findById(userId,"user_data");
    }
}
