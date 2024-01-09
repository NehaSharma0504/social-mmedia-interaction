package eca.learnings.socialmedia.interaction.repository;

import eca.learnings.socialmedia.interaction.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account, String> {
    List<Account> findByUserId(String userId);
}
