package com.banking.api;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private TransactionDAO transactionDAO;

    @PostMapping("/transaction")
    public String doTransaction(@RequestBody TransactionRequest req) {
        Account acc = accountDAO.getAccountById(req.accountId());
        if (acc == null) {
            return "Account not found";
        }
        double newBalance = acc.getBalance();
        if ("DEPOSIT".equals(req.transactionType())) {
            newBalance += req.amount();
        } else if ("WITHDRAWAL".equals(req.transactionType())) {
            if (req.amount() > newBalance) {
                return "Insufficient balance";
            }
            newBalance -= req.amount();
        }
        if (accountDAO.updateBalance(acc.getAccountId(), newBalance)) {
            Transaction t = new Transaction(acc.getAccountId(), req.transactionType(), req.amount(), req.description());
            transactionDAO.addTransaction(t);
            return "OK";
        }
        return "Transaction failed";
    }
}
