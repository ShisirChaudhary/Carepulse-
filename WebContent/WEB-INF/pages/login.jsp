<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    String error = (String) request.getAttribute("error");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Sign in to your CarePulse account to manage appointments.">
    <title>Login | CarePulse</title>
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
            <h1>CarePulse</h1>
            <p>Community Health Appointment Management</p>
        </div>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        <% if (success != null && !success.isEmpty()) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>

        <form method="post" action="<%= ctx %>/login" id="loginForm">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" placeholder="Enter your email" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            <button type="submit" class="btn btn-primary btn-full" id="loginBtn">Sign In</button>
        </form>

        <div class="auth-links">
            <a href="<%= ctx %>/forgot-password">Forgot Password?</a>
            <a href="<%= ctx %>/register">Create an Account</a>
        </div>
    </div>
</div>
</body>
</html>
