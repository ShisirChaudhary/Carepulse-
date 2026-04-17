<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.User" %>
<%
    request.setAttribute("pageTitle", "Manage Patients");
    request.setAttribute("activePage", "users");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<User> users = (List<User>) request.getAttribute("users");
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
        <h2>All Patients</h2>
    </div>
    <div class="card-body">
        <% if (users != null && !users.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% int i = 1; for (User u : users) { %>
                    <tr>
                        <td><%= i++ %></td>
                        <td><%= u.getFullName() %></td>
                        <td><%= u.getEmail() %></td>
                        <td><%= u.getPhone() %></td>
                        <td>
                            <% if (u.isLocked()) { %>
                                <span class="badge badge-cancelled" title="<%= u.getFailedAttempts() %> failed attempts">Locked</span>
                            <% } else { %>
                                <span class="badge badge-confirmed">Active</span>
                            <% } %>
                        </td>
                        <td class="action-btns">
                            <% if (u.isLocked()) { %>
                                <a href="<%= ctx %>/admin?action=unlock&id=<%= u.getId() %>" class="btn btn-success btn-sm">Unlock</a>
                            <% } %>
                            <a href="<%= ctx %>/admin?action=delete&id=<%= u.getId() %>" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this patient? All their appointments will also be deleted.')">Delete</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
            <p class="empty-state">No patients found.</p>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
