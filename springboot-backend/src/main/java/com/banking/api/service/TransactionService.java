package com.banking.api.service;

import com.banking.dao.TransactionDAO;
import com.banking.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public boolean addTransaction(Transaction transaction) {
        return transactionDAO.addTransaction(transaction);
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }
}
