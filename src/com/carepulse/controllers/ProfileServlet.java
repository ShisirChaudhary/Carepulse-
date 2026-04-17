package com.carepulse.controllers;

import com.carepulse.model.User;
import com.carepulse.service.UserService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles patient profile view and updates.
 */
@WebServlet("/patient/profile")
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");

        try {
            User user = userService.getById(userId);
            req.setAttribute("userBean", user);
            req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute("userId");
        String action = req.getParameter("action");

        try {
            if ("updateProfile".equals(action)) {
                String fullName = req.getParameter("fullName");
                String phone = req.getParameter("phone");

                if (!ValidationUtil.isValidName(fullName)) {
                    req.setAttribute("error", "Name must contain only letters and spaces (2-100 chars).");
                    req.setAttribute("userBean", userService.getById(userId));
                    req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
                    return;
                }
                if (!ValidationUtil.isValidPhone(phone)) {
                    req.setAttribute("error", "Phone must be exactly 10 digits.");
                    req.setAttribute("userBean", userService.getById(userId));
                    req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
                    return;
                }

                userService.updateProfile(userId, fullName.trim(), phone.trim());
                session.setAttribute("userName", fullName.trim());
                req.setAttribute("success", "Profile updated successfully.");
                req.setAttribute("userBean", userService.getById(userId));
                req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);

            } else if ("changePassword".equals(action)) {
                String currentPassword = req.getParameter("currentPassword");
                String newPassword = req.getParameter("newPassword");
                String confirmPassword = req.getParameter("confirmPassword");

                // Verify current password
                User user = userService.getById(userId);
                User loginCheck = userService.login(user.getEmail(), currentPassword);
                if (loginCheck == null) {
                    req.setAttribute("error", "Current password is incorrect.");
                    req.setAttribute("userBean", userService.getById(userId));
                    req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
                    return;
                }

                if (!ValidationUtil.isValidPassword(newPassword)) {
                    req.setAttribute("error", "New password must be at least 6 characters.");
                    req.setAttribute("userBean", userService.getById(userId));
                    req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    req.setAttribute("error", "New passwords do not match.");
                    req.setAttribute("userBean", userService.getById(userId));
                    req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
                    return;
                }

                userService.updatePassword(userId, newPassword.trim());
                req.setAttribute("success", "Password changed successfully.");
                req.setAttribute("userBean", userService.getById(userId));
                req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);

            } else {
                resp.sendRedirect(req.getContextPath() + "/patient/profile");
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            try {
                req.setAttribute("userBean", userService.getById(userId));
            } catch (Exception ex) {
                // ignore
            }
            req.getRequestDispatcher("/WEB-INF/pages/patient/profile.jsp").forward(req, resp);
        }
    }
}
