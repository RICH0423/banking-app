package com.banking.api;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AccountDAO accountDAO = new AccountDAO();

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody LoginRequest request) {
        Account account = accountDAO.login(request.getEmail(), request.getPassword());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        account.setPassword(null); // Hide password
        return ResponseEntity.ok(account);
    }
}
