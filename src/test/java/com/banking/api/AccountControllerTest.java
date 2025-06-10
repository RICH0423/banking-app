package com.banking.api;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDAO accountDAO;
    @MockBean
    private TransactionDAO transactionDAO;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId(1);
        account.setBalance(100);
        account.setCustomerName("Test");
        account.setEmail("test@test");
    }

    @Test
    void getAccountReturnsAccount() throws Exception {
        when(accountDAO.getAccountById(1)).thenReturn(account);
        mockMvc.perform(get("/api/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Test"));
    }

    @Test
    void createDepositTransaction() throws Exception {
        when(accountDAO.getAccountById(1)).thenReturn(account);
        when(accountDAO.updateBalance(1, 150.0)).thenReturn(true);
        when(transactionDAO.addTransaction(any(Transaction.class))).thenReturn(true);

        String body = "{\"transactionType\":\"DEPOSIT\",\"amount\":50}";
        mockMvc.perform(post("/api/account/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
        verify(accountDAO).updateBalance(1, 150.0);
    }
}
