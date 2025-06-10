package com.banking.service;

import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
public class AccountService {
    private final Map<Integer, Account> accountStore = new HashMap<>();
    private int transactionSeq = 1;

    @PostConstruct
    public void init() {
        Account a1 = new Account();
        a1.setAccountId(1);
        a1.setAccountNumber("ACC001");
        a1.setCustomerName("Otto");
        a1.setEmail("otto@gmail.com");
        a1.setPassword("password123");
        a1.setBalance(5000.00);
        accountStore.put(a1.getAccountId(), a1);

        Account a2 = new Account();
        a2.setAccountId(2);
        a2.setAccountNumber("ACC002");
        a2.setCustomerName("Rich");
        a2.setEmail("rich@gmail.com");
        a2.setPassword("123456");
        a2.setBalance(1000.00);
        accountStore.put(a2.getAccountId(), a2);
    }

    public Account login(String email, String password) {
        return accountStore.values().stream()
                .filter(a -> a.getEmail().equals(email) && a.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public Account getAccount(int id) {
        return accountStore.get(id);
    }

    public List<Transaction> getTransactions(int accountId) {
        Account account = accountStore.get(accountId);
        return account == null ? Collections.emptyList() : account.getTransactions();
    }

    public Account addTransaction(int accountId, Transaction tx) {
        Account account = accountStore.get(accountId);
        if (account == null) {
            return null;
        }
        tx.setTransactionId(transactionSeq++);
        tx.setAccountId(accountId);
        account.getTransactions().add(tx);
        if ("DEPOSIT".equalsIgnoreCase(tx.getTransactionType())) {
            account.setBalance(account.getBalance() + tx.getAmount());
        } else if ("WITHDRAWAL".equalsIgnoreCase(tx.getTransactionType())) {
            account.setBalance(account.getBalance() - tx.getAmount());
        }
        return account;
    }
}
