package com.banking.model;

import java.sql.Timestamp;

public class Account {
    private int accountId;
    private String accountNumber;
    private String customerName;
    private String email;
    private String password;
    private double balance;
    private Timestamp createdAt;
    
    // Constructors
    public Account() {}
    
    public Account(String accountNumber, String customerName, String email, String password) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
