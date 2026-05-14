package com.carepulse.controllers;

import com.carepulse.service.InquiryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet for the admin Inquiries inbox.
@WebServlet("/admin/inquiries")
public class InquiryAdminServlet extends HttpServlet {

    private final InquiryService inquiryService = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("resolve".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                inquiryService.markResolved(id);
                resp.sendRedirect(req.getContextPath() + "/admin/inquiries?success=Inquiry marked as resolved.");
                return;
            }
            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                inquiryService.delete(id);
                resp.sendRedirect(req.getContextPath() + "/admin/inquiries?success=Inquiry deleted.");
                return;
            }
            String sort = req.getParameter("sort");
            String dir = req.getParameter("dir");
            req.setAttribute("inquiries", inquiryService.getAll(sort, dir));
            req.setAttribute("sortKey", sort);
            req.setAttribute("sortDir", dir);
            req.getRequestDispatcher("/WEB-INF/pages/admin/inquiries.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/admin/inquiries.jsp").forward(req, resp);
        }
    }
}
