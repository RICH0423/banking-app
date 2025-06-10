package com.banking.dao;

import com.banking.model.Transaction;
import com.banking.util.DBConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAO {
    @Autowired
    private DBConnection dbConnection;
    
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, transaction.getAccountId());
            ps.setString(2, transaction.getTransactionType());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getDescription());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setDescription(rs.getString("description"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
        return transaction;
    }
}
