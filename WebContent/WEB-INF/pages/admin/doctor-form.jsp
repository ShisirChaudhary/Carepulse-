<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.carepulse.model.Doctor" %>
<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    boolean isEdit = (doctor != null);
    request.setAttribute("pageTitle", isEdit ? "Edit Doctor" : "Add Doctor");
    request.setAttribute("activePage", "doctors");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    String errorMsg = (String) request.getAttribute("error");
%>

<div class="card form-card">
    <div class="card-header">
        <h2><%= isEdit ? "Edit Doctor" : "Add New Doctor" %></h2>
        <a href="<%= ctx %>/admin/doctors" class="btn btn-secondary btn-sm">Back</a>
    </div>
    <div class="card-body">
        <% if (errorMsg != null) { %>
            <div class="alert alert-error"><%= errorMsg %></div>
        <% } %>

        <form method="post" action="<%= ctx %>/admin/doctors">
            <% if (isEdit) { %>
                <input type="hidden" name="id" value="<%= doctor.getId() %>">
            <% } %>

            <div class="form-group">
                <label for="fullName">Full Name *</label>
                <input type="text" id="fullName" name="fullName" value="<%= isEdit ? doctor.getFullName() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="specialization">Specialization *</label>
                <input type="text" id="specialization" name="specialization" value="<%= isEdit ? doctor.getSpecialization() : "" %>" required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="contact">Contact Number</label>
                    <input type="text" id="contact" name="contact" value="<%= isEdit ? doctor.getContact() : "" %>">
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" value="<%= isEdit ? doctor.getEmail() : "" %>">
                </div>
            </div>

            <div class="form-group" style="flex-direction: row; align-items: center; gap: 0.5rem; margin-top: 1rem;">
                <input type="checkbox" id="available" name="available" <%= (!isEdit || doctor.isAvailable()) ? "checked" : "" %> style="width: auto;">
                <label for="available" style="margin-bottom: 0;">Currently Available for Appointments</label>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary"><%= isEdit ? "Update Doctor" : "Add Doctor" %></button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
