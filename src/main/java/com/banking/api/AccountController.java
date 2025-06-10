package com.banking.api;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountDAO accountDAO = new AccountDAO();

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") int id) {
        Account account = accountDAO.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setPassword(null);
        return ResponseEntity.ok(account);
    }
}
