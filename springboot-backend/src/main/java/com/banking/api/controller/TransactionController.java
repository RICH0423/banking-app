package com.banking.api.controller;

import com.banking.api.service.AccountService;
import com.banking.api.service.TransactionService;
import com.banking.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public TransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Void> addTransaction(@PathVariable int accountId, @RequestBody Transaction transaction) {
        transaction.setAccountId(accountId);

        if ("WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
            var acc = accountService.getAccountById(accountId);
            if (acc == null || acc.getBalance() < transaction.getAmount()) {
                return ResponseEntity.badRequest().build();
            }
            double newBalance = acc.getBalance() - transaction.getAmount();
            if (!accountService.updateBalance(accountId, newBalance)) {
                return ResponseEntity.internalServerError().build();
            }
        } else if ("DEPOSIT".equalsIgnoreCase(transaction.getTransactionType())) {
            var acc = accountService.getAccountById(accountId);
            if (acc == null) return ResponseEntity.badRequest().build();
            double newBalance = acc.getBalance() + transaction.getAmount();
            if (!accountService.updateBalance(accountId, newBalance)) {
                return ResponseEntity.internalServerError().build();
            }
        }

        transactionService.addTransaction(transaction);
        return ResponseEntity.ok().build();
    }
}
