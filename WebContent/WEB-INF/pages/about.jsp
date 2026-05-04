<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Renamed from "ctx" so it does not collide with the same-named variable
    // declared inside header.jsp's scriptlet when that page is statically included.
    String ctxPath = request.getContextPath();
    boolean loggedIn = (session != null && session.getAttribute("userId") != null);

    if (loggedIn) {
        request.setAttribute("pageTitle", "About CarePulse");
        request.setAttribute("activePage", "about");
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
    <meta name="description" content="About CarePulse - Community Health Appointment Management">
    <title>About | CarePulse</title>
    <link rel="stylesheet" href="<%= ctxPath %>/css/style.css">
</head>
<body>
<div class="auth-wrapper auth-wrapper-page">
    <div class="auth-card auth-card-wide">
        <div class="auth-header">
            <div class="auth-logo">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#2563eb" stroke-width="2">
                    <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
                </svg>
            </div>
            <h1>About CarePulse</h1>
            <p>Community Health Appointment Management System</p>
        </div>
<% } %>

<div class="card">
    <div class="card-header">
        <h2>Who We Are</h2>
    </div>
    <div class="card-body">
        <p>CarePulse is a community-focused healthcare appointment management platform built to make
            it easier for patients to connect with the right doctors and for clinics to coordinate
            their schedules. We believe access to timely medical care should be simple, transparent,
            and respectful of patients' time.</p>
        <p>The system is part of an academic project for the Data Structures and Specialist Programming
            module, demonstrating real-world web application design with Java, Jakarta EE, JSP and MySQL.</p>
    </div>
</div>

<div class="card mt-3">
    <div class="card-header">
        <h2>Our Mission</h2>
    </div>
    <div class="card-body">
        <p>To streamline the healthcare booking experience by providing a unified platform where
            patients can discover doctors by specialization, schedule appointments at convenient times,
            and track their visit history, while administrators retain full control over clinic operations.</p>
    </div>
</div>

<div class="card mt-3">
    <div class="card-header">
        <h2>What We Offer</h2>
    </div>
    <div class="card-body">
        <ul class="feature-list">
            <li><strong>Patient self-service:</strong> Register, book, reschedule and cancel appointments online.</li>
            <li><strong>Doctor directory:</strong> Browse available specialists by name or specialization.</li>
            <li><strong>Secure account management:</strong> Encrypted credentials, account locking, and password recovery.</li>
            <li><strong>Admin oversight:</strong> Manage doctors, patients, and the entire appointment pipeline.</li>
            <li><strong>Audit trail:</strong> Every appointment is timestamped and tracked through its lifecycle.</li>
        </ul>
    </div>
</div>

<div class="card mt-3">
    <div class="card-header">
        <h2>Technology</h2>
    </div>
    <div class="card-body">
        <p>Built on a clean Model-View-Controller architecture with Jakarta Servlets and JSP for the
            web tier, AES encryption for sensitive credentials, and MySQL as the relational data
            store. The application follows defensive programming practices with input validation,
            duplicate detection, and graceful exception handling throughout.</p>
    </div>
</div>

<% if (!loggedIn) { %>
        <div class="auth-links mt-3">
            <a href="<%= ctxPath %>/login">Back to Login</a>
            <a href="<%= ctxPath %>/contact">Contact Us</a>
        </div>
    </div>
</div>
</body>
</html>
<% } else { %>
    <%@ include file="/WEB-INF/pages/common/footer.jsp" %>
<% } %>
