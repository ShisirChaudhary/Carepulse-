package com.carepulse.controllers;

import com.carepulse.model.Inquiry;
import com.carepulse.service.InquiryService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet for the public Contact page. GET shows the form and POST stores the inquiry.
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private final InquiryService inquiryService = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String subject = req.getParameter("subject");
        String message = req.getParameter("message");

        // Echo the submitted values back so the form retains them on validation errors.
        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);
        req.setAttribute("subject", subject);
        req.setAttribute("message", message);

        if (!ValidationUtil.isValidName(fullName)) {
            req.setAttribute("error", "Please enter a valid full name (letters and spaces, 2-100 chars).");
            req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("error", "Please enter a valid email address.");
            req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
            return;
        }
        if (ValidationUtil.isEmpty(subject) || subject.trim().length() < 3) {
            req.setAttribute("error", "Subject must be at least 3 characters.");
            req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
            return;
        }
        if (ValidationUtil.isEmpty(message) || message.trim().length() < 10) {
            req.setAttribute("error", "Message must be at least 10 characters.");
            req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
            return;
        }

        try {
            Inquiry inquiry = new Inquiry();
            inquiry.setFullName(fullName.trim());
            inquiry.setEmail(email.trim());
            inquiry.setSubject(subject.trim());
            inquiry.setMessage(message.trim());
            inquiryService.submit(inquiry);

            req.removeAttribute("fullName");
            req.removeAttribute("email");
            req.removeAttribute("subject");
            req.removeAttribute("message");
            req.setAttribute("success", "Thanks for reaching out! We will respond to your inquiry shortly.");
            req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Could not submit your inquiry: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
        }
    }
}
