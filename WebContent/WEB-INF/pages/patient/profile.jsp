<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userBean" type="com.carepulse.model.User" scope="request" />
<%
    request.setAttribute("pageTitle", "My Profile");
    request.setAttribute("activePage", "profile");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    String successMsg = (String) request.getAttribute("success");
    String errorMsg = (String) request.getAttribute("error");
%>

<% if (successMsg != null) { %>
    <div class="alert alert-success"><%= successMsg %></div>
<% } %>
<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<div style="display: flex; gap: 2rem; flex-wrap: wrap;">
    
    <!-- Update Profile Form -->
    <div class="card form-card" style="flex: 1; min-width: 300px;">
        <div class="card-header">
            <h2>Personal Information</h2>
        </div>
        <div class="card-body">
            <form method="post" action="<%= ctx %>/patient/profile">
                <input type="hidden" name="action" value="updateProfile">
                
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" value="<jsp:getProperty name="userBean" property="email" />" disabled style="background-color: #f1f5f9; color: #64748b; cursor: not-allowed;">
                    <small style="color: #64748b;">Email address cannot be changed.</small>
                </div>

                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" id="fullName" name="fullName" value="<jsp:getProperty name="userBean" property="fullName" />" required>
                </div>

                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="text" id="phone" name="phone" value="<jsp:getProperty name="userBean" property="phone" />" required>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary" id="updateProfileBtn">Save Changes</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Change Password Form -->
    <div class="card form-card" style="flex: 1; min-width: 300px;">
        <div class="card-header">
            <h2>Change Password</h2>
        </div>
        <div class="card-body">
            <form method="post" action="<%= ctx %>/patient/profile">
                <input type="hidden" name="action" value="changePassword">
                
                <div class="form-group">
                    <label for="currentPassword">Current Password</label>
                    <input type="password" id="currentPassword" name="currentPassword" required>
                </div>

                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password" id="newPassword" name="newPassword" placeholder="Min 6 characters" required>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm New Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-secondary" id="changePasswordBtn">Change Password</button>
                </div>
            </form>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
