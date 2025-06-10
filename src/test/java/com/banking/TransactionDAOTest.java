import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.banking.dao.TransactionDAO;
import com.banking.model.Transaction;
import com.banking.util.DBConnection;

import java.util.List;

public class TransactionDAOTest {
    @Test
    public void addTransactionReturnsTrue() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);
        DBConnection dbc = mock(DBConnection.class);
        when(dbc.getConnection()).thenReturn(conn);
        TransactionDAO dao = new TransactionDAO();
        ReflectionTestUtils.setField(dao, "dbConnection", dbc);
        Transaction t = new Transaction(1, "DEPOSIT", 100.0, "test");
        assertTrue(dao.addTransaction(t));
    }

    @Test
    public void getTransactionsReturnsList() throws Exception {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("transaction_id")).thenReturn(1,2);
        when(rs.getInt("account_id")).thenReturn(1,1);
        when(rs.getString("transaction_type")).thenReturn("DEPOSIT","WITHDRAWAL");
        when(rs.getDouble("amount")).thenReturn(100.0,50.0);
        when(rs.getString("description")).thenReturn("test1","test2");
        DBConnection dbc = mock(DBConnection.class);
        when(dbc.getConnection()).thenReturn(conn);
        TransactionDAO dao = new TransactionDAO();
        ReflectionTestUtils.setField(dao, "dbConnection", dbc);
        List<Transaction> list = dao.getTransactionsByAccountId(1);
        assertEquals(2, list.size());
    }
}
