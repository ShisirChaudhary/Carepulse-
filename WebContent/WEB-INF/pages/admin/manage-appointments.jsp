<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Appointment" %>
<%
    request.setAttribute("pageTitle", "Manage Appointments");
    request.setAttribute("activePage", "appointments");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    String searchKeyword = (String) request.getAttribute("searchKeyword");
    String successMsg = request.getParameter("success");
    String errorMsg = (String) request.getAttribute("error");
%>

<% if (successMsg != null && !successMsg.isEmpty()) { %>
    <div class="alert alert-success"><%= successMsg %></div>
<% } %>
<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<!-- Search -->
<div class="card">
    <div class="card-header">
        <h2>All Appointments</h2>
        <form method="get" action="<%= ctx %>/admin/appointments" class="search-form" id="searchAppointmentsForm">
            <input type="text" name="search" placeholder="Search patient or doctor..." value="<%= searchKeyword != null ? searchKeyword : "" %>" id="searchInput">
            <button type="submit" class="btn btn-primary btn-sm" id="searchBtn">Search</button>
            <% if (searchKeyword != null) { %>
                <a href="<%= ctx %>/admin/appointments" class="btn btn-secondary btn-sm">Clear</a>
            <% } %>
        </form>
    </div>
    <div class="card-body">
        <% if (appointments != null && !appointments.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Patient</th>
                        <th>Doctor</th>
                        <th>Specialization</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% int i = 1; for (Appointment a : appointments) { %>
                    <tr>
                        <td><%= i++ %></td>
                        <td><%= a.getPatientName() %></td>
                        <td><%= a.getDoctorName() %></td>
                        <td><%= a.getSpecialization() %></td>
                        <td><%= a.getAppointmentDate() %></td>
                        <td><%= a.getAppointmentTime() %></td>
                        <td><span class="badge badge-<%= a.getStatus() %>"><%= a.getStatus() %></span></td>
                        <td class="action-btns">
                            <% if ("pending".equals(a.getStatus())) { %>
                                <a href="<%= ctx %>/admin/appointments?action=status&id=<%= a.getId() %>&value=confirmed" class="btn btn-success btn-sm">Confirm</a>
                                <a href="<%= ctx %>/admin/appointments?action=status&id=<%= a.getId() %>&value=cancelled" class="btn btn-danger btn-sm">Cancel</a>
                            <% } else if ("confirmed".equals(a.getStatus())) { %>
                                <a href="<%= ctx %>/admin/appointments?action=status&id=<%= a.getId() %>&value=completed" class="btn btn-primary btn-sm">Complete</a>
                            <% } else { %>
                                <span class="text-muted">—</span>
                            <% } %>
                        </td>
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
