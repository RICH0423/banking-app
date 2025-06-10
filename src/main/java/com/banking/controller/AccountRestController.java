package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountRestController {

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Map<String, String> body) {
        Account account = accountDAO.login(body.get("email"), body.get("password"));
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable int id) {
        Account account = accountDAO.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable int id) {
        return transactionDAO.getTransactionsByAccountId(id);
    }
}
