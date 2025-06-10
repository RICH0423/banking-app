package com.banking.api;

import com.banking.dao.TransactionDAO;
import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionController {
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    @GetMapping
    public List<Transaction> getTransactions(@PathVariable int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    @PostMapping
    public ResponseEntity<Void> addTransaction(@PathVariable int accountId,
                                               @RequestBody Transaction transaction) {
        transaction.setAccountId(accountId);
        boolean added = transactionDAO.addTransaction(transaction);
        if (!added) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Account account = accountDAO.getAccountById(accountId);
        if (account != null) {
            double newBalance = account.getBalance() + transaction.getAmount();
            accountDAO.updateBalance(accountId, newBalance);
        }
        return ResponseEntity.ok().build();
    }
}
