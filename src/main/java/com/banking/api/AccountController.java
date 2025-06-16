package com.banking.api;

import com.banking.api.dto.LoginRequest;
import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountDAO accountDAO = new AccountDAO();

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody LoginRequest request) {
        Account account = accountDAO.login(request.getEmail(), request.getPassword());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        account.setPassword(null);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable int id) {
        Account account = accountDAO.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setPassword(null);
        return ResponseEntity.ok(account);
    }
}
