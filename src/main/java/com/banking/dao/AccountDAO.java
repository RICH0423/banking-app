package com.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.banking.model.Account;
import com.banking.util.DBConnection;

/**
 * Data Access Object for {@link Account} entities.
 * Provides JDBC based operations for authentication and balance management.
 */
public class AccountDAO {
    
    /**
     * Authenticate an account with the provided credentials.
     *
     * @return the {@link Account} if credentials match, otherwise {@code null}
     */
    public Account login(String email, String password) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE email = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                account = extractAccountFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    
    /**
     * Fetch an account by its ID.
     */
    public Account getAccountById(int accountId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                account = extractAccountFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    
    /**
     * Update the balance for an account.
     *
     * @return {@code true} if the update succeeded
     */
    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map a JDBC {@link ResultSet} row to an {@link Account} object.
     */
    private Account extractAccountFromResultSet(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setCustomerName(rs.getString("customer_name"));
        account.setEmail(rs.getString("email"));
        account.setPassword(rs.getString("password"));
        account.setBalance(rs.getDouble("balance"));
        account.setCreatedAt(rs.getTimestamp("created_at"));
        return account;
    }
}
