package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDAO accountDAO;

    @MockBean
    private TransactionDAO transactionDAO;

    @Test
    void loginSuccess() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("ACC001");
        account.setCustomerName("Otto");
        account.setEmail("otto@gmail.com");
        account.setBalance(100.0);
        when(accountDAO.login("otto@gmail.com","password123")).thenReturn(account);

        String json = "{\"email\":\"otto@gmail.com\",\"password\":\"password123\"}";
        mockMvc.perform(post("/api/login")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Otto"));
    }

    @Test
    void getTransactions() throws Exception {
        when(transactionDAO.getTransactionsByAccountId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
