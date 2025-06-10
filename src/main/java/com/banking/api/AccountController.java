package com.banking.api;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountController(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable int id) {
        Account account = accountDAO.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.setPassword(null);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/account/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable int id) {
        return ResponseEntity.ok(transactionDAO.getTransactionsByAccountId(id));
    }

    @PostMapping("/account/{id}/transactions")
    public ResponseEntity<?> createTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
        Account account = accountDAO.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        double newBalance = account.getBalance();
        if ("DEPOSIT".equalsIgnoreCase(transaction.getTransactionType())) {
            newBalance += transaction.getAmount();
        } else if ("WITHDRAWAL".equalsIgnoreCase(transaction.getTransactionType())) {
            if (transaction.getAmount() > account.getBalance()) {
                return ResponseEntity.badRequest().body("Insufficient balance");
            }
            newBalance -= transaction.getAmount();
        }
        if (accountDAO.updateBalance(id, newBalance)) {
            transaction.setAccountId(id);
            transactionDAO.addTransaction(transaction);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }
}
