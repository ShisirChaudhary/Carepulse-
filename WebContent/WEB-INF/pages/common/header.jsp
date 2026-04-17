<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) request.getAttribute("role");
    if (role == null) {
        role = (session != null) ? (String) session.getAttribute("role") : null;
    }
    String userName = (String) request.getAttribute("userName");
    if (userName == null) {
        userName = (session != null) ? (String) session.getAttribute("userName") : "User";
    }
    String active = (String) request.getAttribute("activePage");
    if (active == null) active = "";
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null) pageTitle = "CarePulse";
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="CarePulse - Community Health Appointment Management System">
    <title><%= pageTitle %> | CarePulse</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<div class="layout">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-brand">
            <div class="brand-icon">
                <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
                </svg>
            </div>
            <span class="brand-text">CarePulse</span>
        </div>

        <nav class="sidebar-nav">
            <% if ("admin".equals(role)) { %>
                <a href="<%= ctx %>/admin" class="nav-link <%= "dashboard".equals(active) ? "active" : "" %>" id="nav-dashboard">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
                    <span>Dashboard</span>
                </a>
                <a href="<%= ctx %>/admin/appointments" class="nav-link <%= "appointments".equals(active) ? "active" : "" %>" id="nav-appointments">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                    <span>Appointments</span>
                </a>
                <a href="<%= ctx %>/admin/doctors" class="nav-link <%= "doctors".equals(active) ? "active" : "" %>" id="nav-doctors">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                    <span>Doctors</span>
                </a>
                <a href="<%= ctx %>/admin?action=users" class="nav-link <%= "users".equals(active) ? "active" : "" %>" id="nav-users">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                    <span>Patients</span>
                </a>
            <% } else { %>
                <a href="<%= ctx %>/patient" class="nav-link <%= "dashboard".equals(active) ? "active" : "" %>" id="nav-dashboard">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
                    <span>Dashboard</span>
                </a>
                <a href="<%= ctx %>/patient/appointments" class="nav-link <%= "appointments".equals(active) ? "active" : "" %>" id="nav-appointments">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                    <span>My Appointments</span>
                </a>
                <a href="<%= ctx %>/patient/appointments?action=book" class="nav-link <%= "book".equals(active) ? "active" : "" %>" id="nav-book">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                    <span>Book Appointment</span>
                </a>
                <a href="<%= ctx %>/patient/profile" class="nav-link <%= "profile".equals(active) ? "active" : "" %>" id="nav-profile">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                    <span>My Profile</span>
                </a>
            <% } %>
        </nav>

        <div class="sidebar-footer">
            <a href="<%= ctx %>/logout" class="nav-link" id="nav-logout">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                <span>Logout</span>
            </a>
        </div>
    </aside>

    <!-- Main Content -->
    <div class="main">
        <!-- Topbar -->
        <header class="topbar">
            <h1 class="topbar-title"><%= pageTitle %></h1>
            <div class="topbar-user">
                <div class="user-avatar"><%= userName != null ? userName.charAt(0) : 'U' %></div>
                <div class="user-info">
                    <span class="user-name"><%= userName %></span>
                    <span class="user-role"><%= role != null ? role.substring(0,1).toUpperCase() + role.substring(1) : "" %></span>
                </div>
            </div>
        </header>

        <!-- Content Area -->
        <div class="content">
