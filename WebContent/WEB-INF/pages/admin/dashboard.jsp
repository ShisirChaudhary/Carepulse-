<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Appointment" %>
<%
    request.setAttribute("pageTitle", "Admin Dashboard");
    request.setAttribute("activePage", "dashboard");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    Integer totalPatients = (Integer) request.getAttribute("totalPatients");
    Integer totalDoctors = (Integer) request.getAttribute("totalDoctors");
    Integer totalAppointments = (Integer) request.getAttribute("totalAppointments");
    List<Appointment> recentAppointments = (List<Appointment>) request.getAttribute("recentAppointments");
    String errorMsg = (String) request.getAttribute("error");
%>

<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<!-- Stat Cards -->
<div class="stat-cards">
    <div class="stat-card stat-card-blue">
        <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
        </div>
        <div class="stat-info">
            <h3><%= totalPatients != null ? totalPatients : 0 %></h3>
            <p>Total Patients</p>
        </div>
    </div>
    <div class="stat-card stat-card-green">
        <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
        </div>
        <div class="stat-info">
            <h3><%= totalDoctors != null ? totalDoctors : 0 %></h3>
            <p>Total Doctors</p>
        </div>
    </div>
    <div class="stat-card stat-card-purple">
        <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
        </div>
        <div class="stat-info">
            <h3><%= totalAppointments != null ? totalAppointments : 0 %></h3>
            <p>Appointments</p>
        </div>
    </div>
</div>

<!-- Recent Appointments -->
<div class="card">
    <div class="card-header">
        <h2>Recent Appointments</h2>
        <a href="<%= ctx %>/admin/appointments" class="btn btn-sm btn-primary">View All</a>
    </div>
    <div class="card-body">
        <% if (recentAppointments != null && !recentAppointments.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>Patient</th>
                        <th>Doctor</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Appointment a : recentAppointments) { %>
                    <tr>
                        <td><%= a.getPatientName() %></td>
                        <td><%= a.getDoctorName() %></td>
                        <td><%= a.getAppointmentDate() %></td>
                        <td><%= a.getAppointmentTime() %></td>
                        <td><span class="badge badge-<%= a.getStatus() %>"><%= a.getStatus() %></span></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
            <p class="empty-state">No appointments found.</p>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
