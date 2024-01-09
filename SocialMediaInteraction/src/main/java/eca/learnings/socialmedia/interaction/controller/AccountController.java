package eca.learnings.socialmedia.interaction.controller;


import eca.learnings.socialmedia.interaction.model.Account;
import eca.learnings.socialmedia.interaction.model.User;
import eca.learnings.socialmedia.interaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    // Other controller methods

    @Autowired
    AccountService accountService;

    @PostMapping("createAccount/{userId}")
    public ResponseEntity<String> createAccountForUser(@PathVariable String userId, @RequestBody Account account) {
        // Retrieve the user with the given user ID from the database
        Mono<User> user = accountService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Set the user for the account and save the account in the database
        account.setUser(user.block());
        accountService.createAccount(account);

        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
    }

    @GetMapping("getAccounts/{userId}")
    public ResponseEntity<List<Account>> getAccountsForUser(@PathVariable String userId) {
        // Retrieve all accounts for the user with the given user ID from the database
        List<Account> accounts = accountService.getAccountsForUser(userId);
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }
}
