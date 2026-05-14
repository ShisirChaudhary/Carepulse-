<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Appointment" %>
<%@ page import="com.carepulse.model.Doctor" %>
<%
    request.setAttribute("pageTitle", "Patient Dashboard");
    request.setAttribute("activePage", "dashboard");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    Integer appointmentCount = (Integer) request.getAttribute("appointmentCount");
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    List<Doctor> recentDoctors = (List<Doctor>) request.getAttribute("recentDoctors");
    String errorMsg = (String) request.getAttribute("error");
%>

<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<!-- Stat Cards -->
<div class="stat-cards">
    <div class="stat-card stat-card-purple stat-card-flex">
        <div class="stat-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
        </div>
        <div class="stat-info">
            <h3><%= appointmentCount != null ? appointmentCount : 0 %></h3>
            <p>My Appointments</p>
        </div>
    </div>
    
    <div class="card cta-card">
        <div>
            <h2>Need to see a doctor?</h2>
            <p>Book a new appointment with our available specialists.</p>
        </div>
        <a href="<%= ctx %>/patient/appointments?action=book" class="btn btn-primary btn-lg">+ Book Now</a>
    </div>
</div>

<!-- Recent Appointments -->
<div class="card mt-4">
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
                <svg class="empty-icon" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                <p>You have no appointments yet.</p>
                <a href="<%= ctx %>/patient/appointments?action=book" class="btn btn-primary mt-2">Book Your First Appointment</a>
            </div>
        <% } %>
    </div>
</div>

<!-- Recently Added Doctors -->
<div class="card mt-4">
    <div class="card-header">
        <h2>Recently Added Doctors</h2>
        <a href="<%= ctx %>/patient/doctors" class="btn btn-sm btn-secondary">Browse All</a>
    </div>
    <div class="card-body">
        <% if (recentDoctors != null && !recentDoctors.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>Doctor</th>
                        <th>Specialization</th>
                        <th>Contact</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Doctor d : recentDoctors) { %>
                    <tr>
                        <td>Dr. <%= d.getFullName() %></td>
                        <td><%= d.getSpecialization() %></td>
                        <td><%= d.getContact() != null ? d.getContact() : "—" %></td>
                        <td>
                            <a href="<%= ctx %>/patient/appointments?action=book&doctorId=<%= d.getId() %>" class="btn btn-primary btn-sm">Book</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
            <p class="empty-state">No doctors are currently available. Please check back soon.</p>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
