package com.banking.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;

@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        
        request.getRequestDispatcher("/jsp/transaction.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        
        String transactionType = request.getParameter("transactionType");
        double amount = Double.parseDouble(request.getParameter("amount"));
        String description = request.getParameter("description");
        
        // Validate transaction
        if (transactionType.equals("WITHDRAWAL") && amount > account.getBalance()) {
            request.setAttribute("error", "Insufficient balance");
            request.getRequestDispatcher("/jsp/transaction.jsp").forward(request, response);
            return;
        }
        
        // Update balance
        double newBalance = account.getBalance();
        if (transactionType.equals("DEPOSIT")) {
            newBalance += amount;
        } else if (transactionType.equals("WITHDRAWAL")) {
            newBalance -= amount;
        }
        
        // Update account balance in database
        if (accountDAO.updateBalance(account.getAccountId(), newBalance)) {
            // Add transaction record
            Transaction transaction = new Transaction(account.getAccountId(), transactionType, amount, description);
            transactionDAO.addTransaction(transaction);
            
            // Update session
            account.setBalance(newBalance);
            session.setAttribute("account", account);
            
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Transaction failed");
            request.getRequestDispatcher("/jsp/transaction.jsp").forward(request, response);
        }
    }
}
