import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.banking.dao.AccountDAO;
import com.banking.model.Account;
import com.banking.util.DBConnection;

public class AccountDAOTest {
    @Test
    public void loginReturnsAccountWhenFound() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("account_id")).thenReturn(1);
        when(rs.getString("account_number")).thenReturn("ACC001");
        when(rs.getString("customer_name")).thenReturn("Otto");
        when(rs.getString("email")).thenReturn("otto@gmail.com");
        when(rs.getString("password")).thenReturn("password123");
        when(rs.getDouble("balance")).thenReturn(5000.0);
        DBConnection dbc = mock(DBConnection.class);
        when(dbc.getConnection()).thenReturn(conn);
        AccountDAO dao = new AccountDAO();
        ReflectionTestUtils.setField(dao, "dbConnection", dbc);
        Account account = dao.login("otto@gmail.com", "password123");
        assertNotNull(account);
        assertEquals("ACC001", account.getAccountNumber());
    }

    @Test
    public void getAccountByIdReturnsAccount() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("account_id")).thenReturn(2);
        when(rs.getString("account_number")).thenReturn("ACC002");
        when(rs.getString("customer_name")).thenReturn("Rich");
        when(rs.getString("email")).thenReturn("rich@gmail.com");
        when(rs.getString("password")).thenReturn("123456");
        when(rs.getDouble("balance")).thenReturn(1000.0);
        DBConnection dbc = mock(DBConnection.class);
        when(dbc.getConnection()).thenReturn(conn);
        AccountDAO dao = new AccountDAO();
        ReflectionTestUtils.setField(dao, "dbConnection", dbc);
        Account account = dao.getAccountById(2);
        assertEquals("Rich", account.getCustomerName());
    }

    @Test
    public void updateBalanceReturnsTrueOnSuccess() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);
        DBConnection dbc = mock(DBConnection.class);
        when(dbc.getConnection()).thenReturn(conn);
        AccountDAO dao = new AccountDAO();
        ReflectionTestUtils.setField(dao, "dbConnection", dbc);
        assertTrue(dao.updateBalance(1, 200.0));
    }
}
