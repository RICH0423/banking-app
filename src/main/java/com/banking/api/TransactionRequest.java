package com.banking.api;

public record TransactionRequest(int accountId, String transactionType, double amount, String description) {}
