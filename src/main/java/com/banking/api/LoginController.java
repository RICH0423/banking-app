package com.banking.api;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AccountDAO accountDAO;

    public LoginController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account credentials) {
        Account account = accountDAO.login(credentials.getEmail(), credentials.getPassword());
        if (account == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        account.setPassword(null); // hide password
        return ResponseEntity.ok(account);
    }
}
