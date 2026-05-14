<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Appointment" %>
<%@ page import="com.carepulse.util.TableSort" %>
<%
    request.setAttribute("pageTitle", "My Appointments");
    request.setAttribute("activePage", "appointments");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    String successMsg = request.getParameter("success");
    String errorMsg = (String) request.getAttribute("error");
    String sortKey = (String) request.getAttribute("sortKey");
    String sortDir = (String) request.getAttribute("sortDir");
    String myApptBase = ctx + "/patient/appointments?";
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
                        <th><a class="<%= TableSort.headerClass("doctor", sortKey) %>" href="<%= myApptBase %>sort=doctor&dir=<%= TableSort.nextDir("doctor", sortKey, sortDir) %>">Doctor<%= TableSort.indicator("doctor", sortKey, sortDir) %></a></th>
                        <th><a class="<%= TableSort.headerClass("specialization", sortKey) %>" href="<%= myApptBase %>sort=specialization&dir=<%= TableSort.nextDir("specialization", sortKey, sortDir) %>">Specialization<%= TableSort.indicator("specialization", sortKey, sortDir) %></a></th>
                        <th><a class="<%= TableSort.headerClass("date", sortKey) %>" href="<%= myApptBase %>sort=date&dir=<%= TableSort.nextDir("date", sortKey, sortDir) %>">Date<%= TableSort.indicator("date", sortKey, sortDir) %></a></th>
                        <th>Time</th>
                        <th>Notes</th>
                        <th><a class="<%= TableSort.headerClass("status", sortKey) %>" href="<%= myApptBase %>sort=status&dir=<%= TableSort.nextDir("status", sortKey, sortDir) %>">Status<%= TableSort.indicator("status", sortKey, sortDir) %></a></th>
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
                        <td class="cell-truncate-sm" title="<%= a.getNotes() %>"><%= a.getNotes() %></td>
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
