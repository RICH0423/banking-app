package com.banking.controller;

import com.banking.controller.TransactionServlet;
import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TransactionServletTest {
    private TransactionServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    @Before
    public void setUp() throws Exception {
        servlet = new TransactionServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        accountDAO = mock(AccountDAO.class);
        transactionDAO = mock(TransactionDAO.class);
        when(request.getSession()).thenReturn(session);

        Field accountField = TransactionServlet.class.getDeclaredField("accountDAO");
        accountField.setAccessible(true);
        accountField.set(servlet, accountDAO);
        Field transField = TransactionServlet.class.getDeclaredField("transactionDAO");
        transField.setAccessible(true);
        transField.set(servlet, transactionDAO);
    }

    @Test
    public void testDoPostRedirectsIfNotLoggedIn() throws Exception {
        when(session.getAttribute("account")).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).sendRedirect("login");
        verify(accountDAO, never()).updateBalance(anyInt(), anyDouble());
        verify(transactionDAO, never()).addTransaction(any(Transaction.class));
    }

    @Test
    public void testDoPostInsufficientFunds() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(100.0);
        when(session.getAttribute("account")).thenReturn(account);
        when(request.getParameter("transactionType")).thenReturn("WITHDRAWAL");
        when(request.getParameter("amount")).thenReturn("150");
        when(request.getParameter("description")).thenReturn("test");
        when(request.getRequestDispatcher("/jsp/transaction.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Insufficient balance");
        verify(dispatcher).forward(request, response);
        verify(accountDAO, never()).updateBalance(anyInt(), anyDouble());
        verify(transactionDAO, never()).addTransaction(any(Transaction.class));
    }

    @Test
    public void testDoPostDepositSuccess() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(100.0);
        when(session.getAttribute("account")).thenReturn(account);
        when(request.getParameter("transactionType")).thenReturn("DEPOSIT");
        when(request.getParameter("amount")).thenReturn("50");
        when(request.getParameter("description")).thenReturn("test");
        when(accountDAO.updateBalance(1, 150.0)).thenReturn(true);

        servlet.doPost(request, response);

        verify(accountDAO).updateBalance(1, 150.0);
        verify(transactionDAO).addTransaction(any(Transaction.class));
        verify(session).setAttribute("account", account);
        verify(response).sendRedirect("dashboard");
        assertEquals(150.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDoPostDepositFailure() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setBalance(100.0);
        when(session.getAttribute("account")).thenReturn(account);
        when(request.getParameter("transactionType")).thenReturn("DEPOSIT");
        when(request.getParameter("amount")).thenReturn("50");
        when(request.getParameter("description")).thenReturn("test");
        when(accountDAO.updateBalance(1, 150.0)).thenReturn(false);
        when(request.getRequestDispatcher("/jsp/transaction.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(accountDAO).updateBalance(1, 150.0);
        verify(transactionDAO, never()).addTransaction(any(Transaction.class));
        verify(request).setAttribute("error", "Transaction failed");
        verify(dispatcher).forward(request, response);
    }
}
