<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Doctor" %>
<%@ page import="com.carepulse.util.TableSort" %>
<%
    request.setAttribute("pageTitle", "Manage Doctors");
    request.setAttribute("activePage", "doctors");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
    String successMsg = request.getParameter("success");
    String errorMsg = (String) request.getAttribute("error");
    String sortKey = (String) request.getAttribute("sortKey");
    String sortDir = (String) request.getAttribute("sortDir");
%>

<% if (successMsg != null && !successMsg.isEmpty()) { %>
    <div class="alert alert-success"><%= successMsg %></div>
<% } %>
<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<div class="card">
    <div class="card-header">
        <h2>All Doctors</h2>
        <a href="<%= ctx %>/admin/doctors?action=add" class="btn btn-primary btn-sm" id="addDoctorBtn">+ Add Doctor</a>
    </div>
    <div class="card-body">
        <% if (doctors != null && !doctors.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th><a class="<%= TableSort.headerClass("name", sortKey) %>" href="<%= ctx %>/admin/doctors?sort=name&dir=<%= TableSort.nextDir("name", sortKey, sortDir) %>">Name<%= TableSort.indicator("name", sortKey, sortDir) %></a></th>
                        <th><a class="<%= TableSort.headerClass("specialization", sortKey) %>" href="<%= ctx %>/admin/doctors?sort=specialization&dir=<%= TableSort.nextDir("specialization", sortKey, sortDir) %>">Specialization<%= TableSort.indicator("specialization", sortKey, sortDir) %></a></th>
                        <th>Contact</th>
                        <th>Email</th>
                        <th><a class="<%= TableSort.headerClass("status", sortKey) %>" href="<%= ctx %>/admin/doctors?sort=status&dir=<%= TableSort.nextDir("status", sortKey, sortDir) %>">Status<%= TableSort.indicator("status", sortKey, sortDir) %></a></th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% int i = 1; for (Doctor d : doctors) { %>
                    <tr>
                        <td><%= i++ %></td>
                        <td><%= d.getFullName() %></td>
                        <td><%= d.getSpecialization() %></td>
                        <td><%= d.getContact() %></td>
                        <td><%= d.getEmail() %></td>
                        <td>
                            <% if (d.isAvailable()) { %>
                                <span class="badge badge-confirmed">Available</span>
                            <% } else { %>
                                <span class="badge badge-cancelled">Unavailable</span>
                            <% } %>
                        </td>
                        <td class="action-btns">
                            <a href="<%= ctx %>/admin/doctors?action=edit&id=<%= d.getId() %>" class="btn btn-warning btn-sm">Edit</a>
                            <a href="<%= ctx %>/admin/doctors?action=delete&id=<%= d.getId() %>" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this doctor?')">Delete</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
            <p class="empty-state">No doctors found.</p>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
