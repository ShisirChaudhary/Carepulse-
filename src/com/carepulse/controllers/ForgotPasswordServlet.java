package com.carepulse.controllers;

import com.carepulse.service.UserService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles forgot password flow (2-step: generate token, then reset).
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String step = req.getParameter("step");

        if ("1".equals(step)) {
            // Step 1: Generate token
            String email = req.getParameter("email");
            if (ValidationUtil.isEmpty(email)) {
                req.setAttribute("error", "Please enter your email address.");
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
                return;
            }
            try {
                String token = userService.generateResetToken(email.trim());
                req.setAttribute("step", "2");
                req.setAttribute("email", email.trim());
                req.setAttribute("tokenGenerated", token);
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
            }

        } else if ("2".equals(step)) {
            // Step 2: Validate token and reset password
            String email = req.getParameter("email");
            String token = req.getParameter("token");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            if (ValidationUtil.isEmpty(token)) {
                req.setAttribute("error", "Please enter the reset token.");
                req.setAttribute("step", "2");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
                return;
            }
            if (!ValidationUtil.isValidPassword(newPassword)) {
                req.setAttribute("error", "Password must be at least 6 characters.");
                req.setAttribute("step", "2");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("error", "Passwords do not match.");
                req.setAttribute("step", "2");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
                return;
            }

            try {
                userService.resetPassword(email.trim(), token.trim(), newPassword.trim());
                resp.sendRedirect(req.getContextPath() + "/login?success=Password reset successful! Please login.");
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
                req.setAttribute("step", "2");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
        }
    }
}
