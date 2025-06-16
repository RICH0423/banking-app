package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @PostMapping("/login")
    public Account login(@RequestBody Account credentials) {
        return accountDAO.login(credentials.getEmail(), credentials.getPassword());
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> transactions(@PathVariable int id) {
        return transactionDAO.getTransactionsByAccountId(id);
    }
}
