package com.banking.controller;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
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
        
        // Get updated account info
        account = accountDAO.getAccountById(account.getAccountId());
        session.setAttribute("account", account);
        
        // Get transaction history
        List<Transaction> transactions = transactionDAO.getTransactionsByAccountId(account.getAccountId());
        request.setAttribute("transactions", transactions);
        
        request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }
}
