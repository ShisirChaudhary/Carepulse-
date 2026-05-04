<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Doctor" %>
<%@ page import="com.carepulse.model.Specialization" %>
<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    boolean isEdit = (doctor != null);
    request.setAttribute("pageTitle", isEdit ? "Edit Doctor" : "Add Doctor");
    request.setAttribute("activePage", "doctors");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    String errorMsg = (String) request.getAttribute("error");
    List<Specialization> specializations = (List<Specialization>) request.getAttribute("specializations");
    int currentSpecId = isEdit ? doctor.getSpecializationId() : 0;
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
                <label for="specializationId">Specialization *</label>
                <select id="specializationId" name="specializationId" required>
                    <option value="">-- Choose a specialization --</option>
                    <% if (specializations != null) {
                        for (Specialization s : specializations) {
                            boolean selected = (s.getId() == currentSpecId); %>
                            <option value="<%= s.getId() %>" <%= selected ? "selected" : "" %>><%= s.getName() %></option>
                    <%  }
                       } %>
                </select>
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

            <div class="form-group checkbox-row">
                <input type="checkbox" id="available" name="available" <%= (!isEdit || doctor.isAvailable()) ? "checked" : "" %>>
                <label for="available">Currently Available for Appointments</label>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary"><%= isEdit ? "Update Doctor" : "Add Doctor" %></button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
