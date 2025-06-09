<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.banking.model.Account" %>
<%
    Account account = (Account) session.getAttribute("account");
    if (account == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>New Transaction - Banking App</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>New Transaction</h1>
        
        <div class="form-container">
            <p><strong>Current Balance:</strong> $<%= String.format("%.2f", account.getBalance()) %></p>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error">${error}</div>
            <% } %>
            
            <form action="transaction" method="post">
                <div class="form-group">
                    <label for="transactionType">Transaction Type:</label>
                    <select id="transactionType" name="transactionType" required>
                        <option value="">Select Type</option>
                        <option value="DEPOSIT">Deposit</option>
                        <option value="WITHDRAWAL">Withdrawal</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="amount">Amount:</label>
                    <input type="number" id="amount" name="amount" step="0.01" min="0.01" required>
                </div>
                
                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" rows="3"></textarea>
                </div>
                
                <button type="submit" class="btn btn-primary">Submit Transaction</button>
                <a href="dashboard" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>