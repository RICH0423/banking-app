package com.banking.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AccountDAO accountDAO = new AccountDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        Account account = accountDAO.login(email, password);
        
        if (account != null) {
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}