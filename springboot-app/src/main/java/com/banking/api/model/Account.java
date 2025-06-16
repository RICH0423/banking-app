package com.banking.api.model;

public class Account {
    private Integer id;
    private String email;
    private String customerName;
    private double balance;

    public Account(Integer id, String email, String customerName, double balance) {
        this.id = id;
        this.email = email;
        this.customerName = customerName;
        this.balance = balance;
    }

    public Integer getId() { return id; }
    public String getEmail() { return email; }
    public String getCustomerName() { return customerName; }
    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }
}
