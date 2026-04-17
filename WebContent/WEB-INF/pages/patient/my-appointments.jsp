<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Appointment" %>
<%
    request.setAttribute("pageTitle", "My Appointments");
    request.setAttribute("activePage", "appointments");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    String successMsg = request.getParameter("success");
    String errorMsg = (String) request.getAttribute("error");
%>

<% if (successMsg != null && !successMsg.isEmpty()) { %>
    <div class="alert alert-success"><%= successMsg %></div>
<% } %>
<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<div class="card">
    <div class="card-header">
        <h2>My Appointment History</h2>
        <a href="<%= ctx %>/patient/appointments?action=book" class="btn btn-primary btn-sm">+ Book New</a>
    </div>
    <div class="card-body">
        <% if (appointments != null && !appointments.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Doctor</th>
                        <th>Specialization</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Notes</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% int i = 1; for (Appointment a : appointments) { %>
                    <tr>
                        <td><%= i++ %></td>
                        <td><%= a.getDoctorName() %></td>
                        <td><%= a.getSpecialization() %></td>
                        <td><%= a.getAppointmentDate() %></td>
                        <td><%= a.getAppointmentTime() %></td>
                        <td style="max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="<%= a.getNotes() %>"><%= a.getNotes() %></td>
                        <td><span class="badge badge-<%= a.getStatus() %>"><%= a.getStatus() %></span></td>
                        <td>
                            <% if ("pending".equals(a.getStatus())) { %>
                                <a href="<%= ctx %>/patient/appointments?action=cancel&id=<%= a.getId() %>" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to cancel this appointment?')">Cancel</a>
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
            <p class="empty-state">You have no appointment history.</p>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
