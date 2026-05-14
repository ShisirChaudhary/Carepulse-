<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Use a separate name from the ctx variable that header.jsp declares.
    String ctxPath = request.getContextPath();
    boolean loggedIn = (session != null && session.getAttribute("userId") != null);

    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    String fullNameVal = (String) request.getAttribute("fullName");
    String emailVal = (String) request.getAttribute("email");
    String subjectVal = (String) request.getAttribute("subject");
    String messageVal = (String) request.getAttribute("message");

    if (loggedIn) {
        request.setAttribute("pageTitle", "Contact Us");
        request.setAttribute("activePage", "contact");
    }
%>
<% if (loggedIn) { %>
    <%@ include file="/WEB-INF/pages/common/header.jsp" %>
<% } else { %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Contact CarePulse support team">
    <title>Contact Us | CarePulse</title>
    <link rel="stylesheet" href="<%= ctxPath %>/css/style.css">
</head>
<body>
<div class="auth-wrapper auth-wrapper-page">
    <div class="auth-card auth-card-medium">
        <div class="auth-header">
            <div class="auth-logo">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#2563eb" stroke-width="2">
                    <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
                </svg>
            </div>
            <h1>Contact Us</h1>
            <p>We would love to hear from you</p>
        </div>
<% } %>

<% if (error != null) { %>
    <div class="alert alert-error"><%= error %></div>
<% } %>
<% if (success != null) { %>
    <div class="alert alert-success"><%= success %></div>
<% } %>

<div class="card">
    <div class="card-header">
        <h2>Get in Touch</h2>
    </div>
    <div class="card-body">
        <p class="mb-2">Have a question, suggestion, or need support? Send us a message
            and our team will respond as soon as possible.</p>

        <div class="info-grid">
            <div>
                <strong>Email:</strong><br>
                <span class="text-muted">support@carepulse.com</span>
            </div>
            <div>
                <strong>Phone:</strong><br>
                <span class="text-muted">+977-1-4000000</span>
            </div>
            <div>
                <strong>Address:</strong><br>
                <span class="text-muted">Kamalpokhari, Kathmandu, Nepal</span>
            </div>
            <div>
                <strong>Hours:</strong><br>
                <span class="text-muted">Sun - Fri, 9:00 AM &ndash; 6:00 PM</span>
            </div>
        </div>

        <form method="post" action="<%= ctxPath %>/contact" id="contactForm">
            <div class="form-row">
                <div class="form-group">
                    <label for="fullName">Full Name *</label>
                    <input type="text" id="fullName" name="fullName" placeholder="Your name"
                           value="<%= fullNameVal != null ? fullNameVal : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" placeholder="you@example.com"
                           value="<%= emailVal != null ? emailVal : "" %>" required>
                </div>
            </div>
            <div class="form-group">
                <label for="subject">Subject *</label>
                <input type="text" id="subject" name="subject" placeholder="What is this about?"
                       value="<%= subjectVal != null ? subjectVal : "" %>" required>
            </div>
            <div class="form-group">
                <label for="message">Message *</label>
                <textarea id="message" name="message" rows="5"
                          placeholder="Share the details of your inquiry..." required><%= messageVal != null ? messageVal : "" %></textarea>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary" id="submitInquiryBtn">Send Message</button>
            </div>
        </form>
    </div>
</div>

<% if (!loggedIn) { %>
        <div class="auth-links mt-3">
            <a href="<%= ctxPath %>/login">Back to Login</a>
            <a href="<%= ctxPath %>/about">About Us</a>
        </div>
    </div>
</div>
</body>
</html>
<% } else { %>
    <%@ include file="/WEB-INF/pages/common/footer.jsp" %>
<% } %>
