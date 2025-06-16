package com.banking.api.service;

import com.banking.api.model.Account;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public AccountService() {
        // sample account
        accounts.put("rich@gmail.com", new Account(1, "rich@gmail.com", "Rich", 1000.0));
    }

    public Account login(String email) {
        return accounts.get(email);
    }

    public Account deposit(String email, double amount) {
        Account acc = accounts.get(email);
        if (acc != null) {
            acc.setBalance(acc.getBalance() + amount);
        }
        return acc;
    }
}
