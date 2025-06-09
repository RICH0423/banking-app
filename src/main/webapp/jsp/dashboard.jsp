<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.banking.model.Account" %>
<%@ page import="com.banking.model.Transaction" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    Account account = (Account) session.getAttribute("account");
    if (account == null) {
        response.sendRedirect("login");
        return;
    }
    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Banking App</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Banking Dashboard</h1>
            <div class="user-info">
                Welcome, <%= account.getCustomerName() %> | 
                <a href="logout">Logout</a>
            </div>
        </header>
        
        <div class="account-info">
            <h2>Account Information</h2>
            <p><strong>Account Number:</strong> <%= account.getAccountNumber() %></p>
            <p><strong>Current Balance:</strong> $<%= String.format("%.2f", account.getBalance()) %></p>
            <a href="transaction" class="btn btn-primary">New Transaction</a>
        </div>
        
        <div class="transaction-history">
            <h2>Recent Transactions</h2>
            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (transactions != null && !transactions.isEmpty()) {
                        for (Transaction t : transactions) { %>
                    <tr>
                        <td><%= sdf.format(t.getTransactionDate()) %></td>
                        <td><%= t.getTransactionType() %></td>
                        <td class="<%= t.getTransactionType().equals("DEPOSIT") ? "credit" : "debit" %>">
                            $<%= String.format("%.2f", t.getAmount()) %>
                        </td>
                        <td><%= t.getDescription() %></td>
                    </tr>
                    <% }} else { %>
                    <tr>
                        <td colspan="4">No transactions found</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>