<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Appointment" %>
<%
    request.setAttribute("pageTitle", "Patient Dashboard");
    request.setAttribute("activePage", "dashboard");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    Integer appointmentCount = (Integer) request.getAttribute("appointmentCount");
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    String errorMsg = (String) request.getAttribute("error");
%>

<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<!-- Stat Cards -->
<div class="stat-cards">
    <div class="stat-card stat-card-purple" style="flex: 1;">
        <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
        </div>
        <div class="stat-info">
            <h3><%= appointmentCount != null ? appointmentCount : 0 %></h3>
            <p>My Appointments</p>
        </div>
    </div>
    
    <div class="card" style="flex: 2; margin: 0; display: flex; align-items: center; justify-content: space-between; padding: 2rem;">
        <div>
            <h2 style="margin: 0 0 0.5rem 0;">Need to see a doctor?</h2>
            <p style="margin: 0; color: #64748b;">Book a new appointment with our available specialists.</p>
        </div>
        <a href="<%= ctx %>/patient/appointments?action=book" class="btn btn-primary" style="padding: 1rem 2rem; font-size: 1.1rem;">+ Book Now</a>
    </div>
</div>

<!-- Recent Appointments -->
<div class="card" style="margin-top: 2rem;">
    <div class="card-header">
        <h2>Upcoming & Recent Appointments</h2>
        <a href="<%= ctx %>/patient/appointments" class="btn btn-sm btn-secondary">View All</a>
    </div>
    <div class="card-body">
        <% if (appointments != null && !appointments.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>Doctor</th>
                        <th>Specialization</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Appointment a : appointments) { %>
                    <tr>
                        <td><%= a.getDoctorName() %></td>
                        <td><%= a.getSpecialization() %></td>
                        <td><%= a.getAppointmentDate() %></td>
                        <td><%= a.getAppointmentTime() %></td>
                        <td><span class="badge badge-<%= a.getStatus() %>"><%= a.getStatus() %></span></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
            <div class="empty-state">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="2" style="margin-bottom: 1rem;"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                <p>You have no appointments yet.</p>
                <a href="<%= ctx %>/patient/appointments?action=book" class="btn btn-primary" style="margin-top: 1rem;">Book Your First Appointment</a>
            </div>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
