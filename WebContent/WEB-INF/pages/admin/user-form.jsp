<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.carepulse.model.User" %>
<%
    User userBean = (User) request.getAttribute("userBean");
    boolean isEdit = (userBean != null && userBean.getId() > 0);
    request.setAttribute("pageTitle", isEdit ? "Edit Patient" : "Add Patient");
    request.setAttribute("activePage", "users");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    String errorMsg = (String) request.getAttribute("error");
%>

<div class="card form-card">
    <div class="card-header">
        <h2><%= isEdit ? "Edit Patient" : "Add New Patient" %></h2>
        <a href="<%= ctx %>/admin?action=users" class="btn btn-secondary btn-sm">Back</a>
    </div>
    <div class="card-body">
        <% if (errorMsg != null) { %>
            <div class="alert alert-error"><%= errorMsg %></div>
        <% } %>

        <form method="post" action="<%= ctx %>/admin">
            <input type="hidden" name="action" value="saveUser">
            <% if (isEdit) { %>
                <input type="hidden" name="id" value="<%= userBean.getId() %>">
            <% } %>

            <div class="form-group">
                <label for="fullName">Full Name *</label>
                <input type="text" id="fullName" name="fullName" placeholder="Enter full name"
                       value="<%= userBean != null && userBean.getFullName() != null ? userBean.getFullName() : "" %>" required>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" placeholder="patient@example.com"
                           value="<%= userBean != null && userBean.getEmail() != null ? userBean.getEmail() : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="phone">Phone *</label>
                    <input type="text" id="phone" name="phone" placeholder="10-digit phone number"
                           value="<%= userBean != null && userBean.getPhone() != null ? userBean.getPhone() : "" %>" required>
                </div>
            </div>
            <div class="form-group">
                <label for="password"><%= isEdit ? "New Password (leave blank to keep current)" : "Password *" %></label>
                <input type="password" id="password" name="password" placeholder="Min 6 characters"
                       <%= isEdit ? "" : "required" %>>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <%= isEdit ? "Update Patient" : "Add Patient" %>
                </button>
                <a href="<%= ctx %>/admin?action=users" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
