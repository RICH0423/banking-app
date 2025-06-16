package com.banking.api.controller;

import com.banking.api.model.Account;
import com.banking.api.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestParam String email) {
        Account account = accountService.login(email);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestParam String email,
                                           @RequestParam double amount) {
        Account account = accountService.deposit(email, amount);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }
}
