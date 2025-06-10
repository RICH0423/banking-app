package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBConnection {
    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/banking_db}")
    private String url;

    @Value("${spring.datasource.username:root}")
    private String user;

    @Value("${spring.datasource.password:password}")
    private String password;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
