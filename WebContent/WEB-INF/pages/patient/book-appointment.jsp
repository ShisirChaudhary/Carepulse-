<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Doctor" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    request.setAttribute("pageTitle", "Book Appointment");
    request.setAttribute("activePage", "book");
    
    // Pre-compute today's date for 'min' attribute validation
    String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
    String errorMsg = (String) request.getAttribute("error");
    String preSelectedDoctorId = request.getParameter("doctorId");
%>

<div class="card form-card">
    <div class="card-header">
        <h2>Book New Appointment</h2>
    </div>
    <div class="card-body">
        <% if (errorMsg != null) { %>
            <div class="alert alert-error"><%= errorMsg %></div>
        <% } %>

        <form method="post" action="<%= ctx %>/patient/appointments">
            <div class="form-group">
                <label for="doctorId">Select Doctor *</label>
                <select id="doctorId" name="doctorId" required>
                    <option value="">-- Choose an available doctor --</option>
                    <% if (doctors != null) {
                        for (Doctor d : doctors) {
                            String idStr = String.valueOf(d.getId());
                            boolean selected = idStr.equals(preSelectedDoctorId); %>
                            <option value="<%= idStr %>" <%= selected ? "selected" : "" %>>Dr. <%= d.getFullName() %> (<%= d.getSpecialization() %>)</option>
                    <%  }
                       } %>
                </select>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="appointmentDate">Date *</label>
                    <input type="date" id="appointmentDate" name="appointmentDate" min="<%= todayDate %>" required>
                </div>
                <div class="form-group">
                    <label for="appointmentTime">Time *</label>
                    <select id="appointmentTime" name="appointmentTime" required>
                        <option value="">-- Select Time --</option>
                        <option value="09:00 AM">09:00 AM</option>
                        <option value="10:00 AM">10:00 AM</option>
                        <option value="11:00 AM">11:00 AM</option>
                        <option value="12:00 PM">12:00 PM</option>
                        <option value="02:00 PM">02:00 PM</option>
                        <option value="03:00 PM">03:00 PM</option>
                        <option value="04:00 PM">04:00 PM</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="notes">Symptom Notes / Reason for Visit</label>
                <textarea id="notes" name="notes" rows="4" placeholder="Briefly describe why you are visiting..."></textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary" id="submitBookBtn">Book Appointment</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
