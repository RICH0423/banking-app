package com.banking.api.service;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();

    public Account login(String email, String password) {
        return accountDAO.login(email, password);
    }

    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }

    public boolean updateBalance(int accountId, double newBalance) {
        return accountDAO.updateBalance(accountId, newBalance);
    }
}
