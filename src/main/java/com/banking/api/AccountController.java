package com.banking.api;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password,
                                   HttpSession session) {
        Account account = accountDAO.login(email, password);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Error("Invalid credentials"));
        }
        session.setAttribute("account", account);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/account")
    public ResponseEntity<Account> account(HttpSession session) {
        Account acc = (Account) session.getAttribute("account");
        if (acc == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // refresh info
        Account fresh = accountDAO.getAccountById(acc.getAccountId());
        session.setAttribute("account", fresh);
        return ResponseEntity.ok(fresh);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> transactions(HttpSession session) {
        Account acc = (Account) session.getAttribute("account");
        if (acc == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Transaction> list = transactionDAO.getTransactionsByAccountId(acc.getAccountId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> newTransaction(@RequestParam String transactionType,
                                            @RequestParam double amount,
                                            @RequestParam(required = false) String description,
                                            HttpSession session) {
        Account acc = (Account) session.getAttribute("account");
        if (acc == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if ("WITHDRAWAL".equals(transactionType) && amount > acc.getBalance()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error("Insufficient balance"));
        }
        double newBalance = acc.getBalance();
        if ("DEPOSIT".equals(transactionType)) {
            newBalance += amount;
        } else if ("WITHDRAWAL".equals(transactionType)) {
            newBalance -= amount;
        }
        if (accountDAO.updateBalance(acc.getAccountId(), newBalance)) {
            Transaction t = new Transaction(acc.getAccountId(), transactionType, amount, description);
            transactionDAO.addTransaction(t);
            acc.setBalance(newBalance);
            session.setAttribute("account", acc);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Error("Transaction failed"));
    }

    // simple error wrapper
    record Error(String message) {}
}
