package eca.learnings.socialmedia.interaction.service;

import eca.learnings.socialmedia.interaction.model.Account;
import eca.learnings.socialmedia.interaction.model.User;
import eca.learnings.socialmedia.interaction.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    private final MongoOperations mongoOperations;
    private final WebClient.Builder webClientBuilder;
    @Autowired
    public AccountService(MongoOperations mongoOperations, WebClient.Builder webClientBuilder) {
        this.mongoOperations = mongoOperations;
        this.webClientBuilder = webClientBuilder;
    }

    public void createAccount(Account account) {
        // Additional logic, if required, before saving the account
        account.setAccountId(UUID.randomUUID().toString().replace("-", ""));
        mongoOperations.save(account,"account_details");
    }

    public List<Account> getAccountsForUser(String userId) {
        // Additional logic, if required, to retrieve accounts for a user
        return accountRepository.findByUserId(userId);
    }
    public Mono<User> getUserById(String userId) {
        return webClientBuilder
                .baseUrl("http://gateway-service:8111")
                .build()
                .get()
                .uri("/myapp/users/{userId}", userId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    // Handle 4xx errors (e.g., 404) here
                    // You can return a default value or throw a custom exception
                    return Mono.empty();
                })
                .bodyToMono(User.class);
    }
    // Other methods for updating, deleting, etc.
}
