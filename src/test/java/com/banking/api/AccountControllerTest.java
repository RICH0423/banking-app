package com.banking.api;

import com.banking.Application;
import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDAO accountDAO;
    @MockBean
    private TransactionDAO transactionDAO;

    private Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setAccountId(1);
        account.setBalance(100.0);
        account.setCustomerName("Test");
        account.setAccountNumber("ACC001");
    }

    @Test
    void loginAndDeposit() throws Exception {
        when(accountDAO.login("rich@gmail.com", "123456")).thenReturn(account);
        when(accountDAO.getAccountById(1)).thenReturn(account);
        when(accountDAO.updateBalance(eq(1), anyDouble())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .param("email", "rich@gmail.com")
                .param("password", "123456"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .sessionAttr("account", account)
                .param("transactionType", "DEPOSIT")
                .param("amount", "50"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(transactionDAO).addTransaction(any());
    }
}
