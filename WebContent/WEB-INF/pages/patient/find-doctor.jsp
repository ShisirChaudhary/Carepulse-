<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.carepulse.model.Doctor" %>
<%@ page import="com.carepulse.util.TableSort" %>
<%
    request.setAttribute("pageTitle", "Find a Doctor");
    request.setAttribute("activePage", "doctors");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
    String searchKeyword = (String) request.getAttribute("searchKeyword");
    String errorMsg = (String) request.getAttribute("error");
    String sortKey = (String) request.getAttribute("sortKey");
    String sortDir = (String) request.getAttribute("sortDir");

    StringBuilder findDocBaseSb = new StringBuilder(ctx + "/patient/doctors?");
    if (searchKeyword != null && !searchKeyword.isEmpty()) {
        findDocBaseSb.append("search=").append(URLEncoder.encode(searchKeyword, "UTF-8")).append("&");
    }
    String findDocBase = findDocBaseSb.toString();
%>

<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<div class="card">
    <div class="card-header">
        <h2>Available Doctors</h2>
        <form method="get" action="<%= ctx %>/patient/doctors" class="search-form">
            <input type="text" name="search" placeholder="Search by name or specialization..."
                   value="<%= searchKeyword != null ? searchKeyword : "" %>">
            <button type="submit" class="btn btn-primary btn-sm">Search</button>
            <% if (searchKeyword != null && !searchKeyword.isEmpty()) { %>
                <a href="<%= ctx %>/patient/doctors" class="btn btn-secondary btn-sm">Clear</a>
            <% } %>
        </form>
    </div>
    <div class="card-body">
        <% if (doctors != null && !doctors.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th><a class="<%= TableSort.headerClass("name", sortKey) %>" href="<%= findDocBase %>sort=name&dir=<%= TableSort.nextDir("name", sortKey, sortDir) %>">Doctor<%= TableSort.indicator("name", sortKey, sortDir) %></a></th>
                        <th><a class="<%= TableSort.headerClass("specialization", sortKey) %>" href="<%= findDocBase %>sort=specialization&dir=<%= TableSort.nextDir("specialization", sortKey, sortDir) %>">Specialization<%= TableSort.indicator("specialization", sortKey, sortDir) %></a></th>
                        <th>Contact</th>
                        <th>Email</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% int i = 1; for (Doctor d : doctors) { %>
                    <tr>
                        <td><%= i++ %></td>
                        <td>Dr. <%= d.getFullName() %></td>
                        <td><%= d.getSpecialization() %></td>
                        <td><%= d.getContact() != null ? d.getContact() : "—" %></td>
                        <td><%= d.getEmail() != null ? d.getEmail() : "—" %></td>
                        <td>
                            <a href="<%= ctx %>/patient/appointments?action=book&doctorId=<%= d.getId() %>" class="btn btn-primary btn-sm">Book</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
            <div class="empty-state">
                <% if (searchKeyword != null && !searchKeyword.isEmpty()) { %>
                    No doctors match "<%= searchKeyword %>". Try a different keyword.
                <% } else { %>
                    No available doctors at this time. Please check back later.
                <% } %>
            </div>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
