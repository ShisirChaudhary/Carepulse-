<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.carepulse.model.Inquiry" %>
<%@ page import="com.carepulse.util.TableSort" %>
<%
    request.setAttribute("pageTitle", "Inquiries");
    request.setAttribute("activePage", "inquiries");
%>
<%@ include file="/WEB-INF/pages/common/header.jsp" %>

<%
    List<Inquiry> inquiries = (List<Inquiry>) request.getAttribute("inquiries");
    String successMsg = request.getParameter("success");
    String errorMsg = (String) request.getAttribute("error");
    String sortKey = (String) request.getAttribute("sortKey");
    String sortDir = (String) request.getAttribute("sortDir");
    String inqBase = ctx + "/admin/inquiries?";
%>

<% if (successMsg != null && !successMsg.isEmpty()) { %>
    <div class="alert alert-success"><%= successMsg %></div>
<% } %>
<% if (errorMsg != null) { %>
    <div class="alert alert-error"><%= errorMsg %></div>
<% } %>

<div class="card">
    <div class="card-header">
        <h2>Contact Inquiries</h2>
    </div>
    <div class="card-body">
        <% if (inquiries != null && !inquiries.isEmpty()) { %>
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th><a class="<%= TableSort.headerClass("name", sortKey) %>" href="<%= inqBase %>sort=name&dir=<%= TableSort.nextDir("name", sortKey, sortDir) %>">Name<%= TableSort.indicator("name", sortKey, sortDir) %></a></th>
                        <th><a class="<%= TableSort.headerClass("email", sortKey) %>" href="<%= inqBase %>sort=email&dir=<%= TableSort.nextDir("email", sortKey, sortDir) %>">Email<%= TableSort.indicator("email", sortKey, sortDir) %></a></th>
                        <th><a class="<%= TableSort.headerClass("subject", sortKey) %>" href="<%= inqBase %>sort=subject&dir=<%= TableSort.nextDir("subject", sortKey, sortDir) %>">Subject<%= TableSort.indicator("subject", sortKey, sortDir) %></a></th>
                        <th>Message</th>
                        <th><a class="<%= TableSort.headerClass("received", sortKey) %>" href="<%= inqBase %>sort=received&dir=<%= TableSort.nextDir("received", sortKey, sortDir) %>">Received<%= TableSort.indicator("received", sortKey, sortDir) %></a></th>
                        <th><a class="<%= TableSort.headerClass("status", sortKey) %>" href="<%= inqBase %>sort=status&dir=<%= TableSort.nextDir("status", sortKey, sortDir) %>">Status<%= TableSort.indicator("status", sortKey, sortDir) %></a></th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% int i = 1; for (Inquiry q : inquiries) { %>
                    <tr>
                        <td><%= i++ %></td>
                        <td><%= q.getFullName() %></td>
                        <td><%= q.getEmail() %></td>
                        <td><%= q.getSubject() %></td>
                        <td class="cell-truncate" title="<%= q.getMessage() %>"><%= q.getMessage() %></td>
                        <td><%= q.getCreatedAt() %></td>
                        <td>
                            <% if ("resolved".equals(q.getStatus())) { %>
                                <span class="badge badge-completed">Resolved</span>
                            <% } else { %>
                                <span class="badge badge-pending">Open</span>
                            <% } %>
                        </td>
                        <td class="action-btns">
                            <% if (!"resolved".equals(q.getStatus())) { %>
                                <a href="<%= ctx %>/admin/inquiries?action=resolve&id=<%= q.getId() %>" class="btn btn-success btn-sm">Resolve</a>
                            <% } %>
                            <a href="<%= ctx %>/admin/inquiries?action=delete&id=<%= q.getId() %>" class="btn btn-danger btn-sm" onclick="return confirm('Delete this inquiry permanently?')">Delete</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
            <p class="empty-state">No inquiries yet. Submissions from the public Contact page will appear here.</p>
        <% } %>
    </div>
</div>

<%@ include file="/WEB-INF/pages/common/footer.jsp" %>
