package com.carepulse.controllers;

import com.carepulse.service.UserService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet that handles the two-step forgot password flow:
// step 1 generates a reset token, step 2 verifies the token and sets the new password.
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
            // Step 1: generate the reset token for the supplied email.
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
            } catch (com.carepulse.util.CarePulseException e) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "An unexpected system error occurred. Please try again.");
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
            }

        } else if ("2".equals(step)) {
            // Step 2: validate the token and apply the new password.
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
            } catch (com.carepulse.util.CarePulseException e) {
                req.setAttribute("error", e.getMessage());
                req.setAttribute("step", "2");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "An unexpected system error occurred. Please try again.");
                req.setAttribute("step", "2");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp").forward(req, resp);
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
        }
    }
}
