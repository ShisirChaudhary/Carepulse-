<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Join CarePulse and start booking health appointments online.">
    <title>Register | CarePulse</title>
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
            <h1>Create Account</h1>
            <p>Join CarePulse as a Patient</p>
        </div>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <form method="post" action="<%= ctx %>/register" id="registerForm">
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" placeholder="Enter your full name" required>
            </div>
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" placeholder="Enter your email" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone Number</label>
                <input type="text" id="phone" name="phone" placeholder="Enter 10-digit phone number" required>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Min 6 characters" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm password" required>
                </div>
            </div>
            <button type="submit" class="btn btn-primary btn-full" id="registerBtn">Create Account</button>
        </form>

        <div class="auth-links">
            <a href="<%= ctx %>/login">Already have an account? Sign In</a>
        </div>
        <div class="auth-links">
            <a href="<%= ctx %>/about">About Us</a>
            <a href="<%= ctx %>/contact">Contact Us</a>
        </div>
    </div>
</div>
</body>
</html>
