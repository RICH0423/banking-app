package com.banking.controller;

import com.banking.model.Account;
import com.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody LoginRequest request) {
        Account account = accountService.login(request.getEmail(), request.getPassword());
        if (account == null) {
            return ResponseEntity.status(401).build();
        }
        account.setPassword(null); // hide password
        return ResponseEntity.ok(account);
    }
}
