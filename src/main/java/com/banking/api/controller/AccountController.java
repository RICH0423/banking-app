package com.banking.api.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @PostMapping("/login")
    public Account login(@RequestBody Account credentials) {
        return accountDAO.login(credentials.getEmail(), credentials.getPassword());
    }

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable("id") int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    @GetMapping("/account/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable("id") int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    @PostMapping("/account/{id}/transaction")
    public void createTransaction(@PathVariable("id") int accountId, @RequestBody Transaction tx) {
        // update balance
        Account acc = accountDAO.getAccountById(accountId);
        double newBalance = acc.getBalance();
        if ("DEPOSIT".equals(tx.getTransactionType())) {
            newBalance += tx.getAmount();
        } else if ("WITHDRAWAL".equals(tx.getTransactionType())) {
            newBalance -= tx.getAmount();
        }
        accountDAO.updateBalance(accountId, newBalance);

        tx.setAccountId(accountId);
        transactionDAO.addTransaction(tx);
    }
}
