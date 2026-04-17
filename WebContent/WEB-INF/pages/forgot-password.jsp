<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    String error = (String) request.getAttribute("error");
    String stepAttr = (String) request.getAttribute("step");
    String step = (stepAttr != null) ? stepAttr : "1";
    String email = (String) request.getAttribute("email");
    String tokenGenerated = (String) request.getAttribute("tokenGenerated");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Reset your CarePulse password">
    <title>Forgot Password | CarePulse</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-header">
            <div class="auth-logo">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#2563eb" stroke-width="2">
                    <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
                </svg>
            </div>
            <h1>Reset Password</h1>
            <p><%= "2".equals(step) ? "Enter your reset token and new password" : "Enter your email to receive a reset token" %></p>
        </div>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <% if (tokenGenerated != null) { %>
            <div class="alert alert-success">
                Your reset token: <strong><%= tokenGenerated %></strong><br>
                <small>(In production, this would be sent via email)</small>
            </div>
        <% } %>

        <% if ("2".equals(step)) { %>
            <!-- Step 2: Enter token and new password -->
            <form method="post" action="<%= ctx %>/forgot-password" id="resetStep2Form">
                <input type="hidden" name="step" value="2">
                <input type="hidden" name="email" value="<%= email %>">
                <div class="form-group">
                    <label for="token">Reset Token</label>
                    <input type="text" id="token" name="token" placeholder="Enter the reset token" required>
                </div>
                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" placeholder="Min 6 characters" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password" required>
                </div>
                <button type="submit" class="btn btn-primary btn-full" id="resetPasswordBtn">Reset Password</button>
            </form>
        <% } else { %>
            <!-- Step 1: Enter email -->
            <form method="post" action="<%= ctx %>/forgot-password" id="resetStep1Form">
                <input type="hidden" name="step" value="1">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" placeholder="Enter your registered email" required>
                </div>
                <button type="submit" class="btn btn-primary btn-full" id="generateTokenBtn">Generate Reset Token</button>
            </form>
        <% } %>

        <div class="auth-links">
            <a href="<%= ctx %>/login">Back to Login</a>
        </div>
    </div>
</div>
</body>
</html>
