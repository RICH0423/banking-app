package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class providing JDBC connections to the application database.
 * Connection parameters can be configured via environment variables.
 */
public class DBConnection {
    private static final String URL = System.getenv().getOrDefault("DB_URL",
            "jdbc:mysql://localhost:3306/banking_db");
    private static final String USER = System.getenv().getOrDefault("DB_USER",
            "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD",
            "password");
    
    // Load MySQL JDBC driver when the class is first used
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtain a new database connection using the configured credentials.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
