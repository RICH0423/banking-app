package com.banking.controller;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable int id) {
        Account account = accountService.getAccount(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setPassword(null);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable int id) {
        return accountService.getTransactions(id);
    }

    @PostMapping("/{id}/transactions")
    public ResponseEntity<Account> addTransaction(@PathVariable int id, @RequestBody TransactionRequest request) {
        Transaction tx = new Transaction();
        tx.setTransactionType(request.getTransactionType());
        tx.setAmount(request.getAmount());
        tx.setDescription(request.getDescription());
        Account account = accountService.addTransaction(id, tx);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setPassword(null);
        return ResponseEntity.ok(account);
    }
}
