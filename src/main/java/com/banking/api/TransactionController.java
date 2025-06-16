package com.banking.api;

import com.banking.api.dto.TransactionRequest;
import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost")
public class TransactionController {
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @PostMapping("/accounts/{id}/transactions")
    public ResponseEntity<String> createTransaction(@PathVariable int id,
                                                    @RequestBody TransactionRequest request) {
        Account account = accountDAO.getAccountById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        double amount = request.getAmount();
        String type = request.getTransactionType();
        double newBalance = account.getBalance();

        if ("WITHDRAWAL".equalsIgnoreCase(type)) {
            if (amount > newBalance) {
                return ResponseEntity.badRequest().body("Insufficient balance");
            }
            newBalance -= amount;
        } else if ("DEPOSIT".equalsIgnoreCase(type)) {
            newBalance += amount;
        } else {
            return ResponseEntity.badRequest().body("Invalid transaction type");
        }

        if (accountDAO.updateBalance(id, newBalance)) {
            Transaction tx = new Transaction(id, type.toUpperCase(), amount, request.getDescription());
            transactionDAO.addTransaction(tx);
            return ResponseEntity.ok("Transaction successful");
        } else {
            return ResponseEntity.internalServerError().body("Transaction failed");
        }
    }
}
