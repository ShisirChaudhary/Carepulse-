package com.carepulse.controllers;

import com.carepulse.model.User;
import com.carepulse.service.UserService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet that handles patient registration.
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Validate each field before attempting any database work.
        if (!ValidationUtil.isValidName(fullName)) {
            req.setAttribute("error", "Name must contain only letters and spaces (2-100 chars).");
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("error", "Please enter a valid email address.");
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            req.setAttribute("error", "Phone must be exactly 10 digits.");
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            req.setAttribute("error", "Password must be at least 6 characters.");
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }
        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }

        try {
            // Reject the registration if the email or phone is already in use.
            if (userService.emailExists(email.trim())) {
                req.setAttribute("error", "Email is already registered.");
                req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
                return;
            }
            if (userService.phoneExists(phone.trim())) {
                req.setAttribute("error", "Phone number is already registered.");
                req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
                return;
            }

            // Create the new patient account.
            User user = new User();
            user.setFullName(fullName.trim());
            user.setEmail(email.trim());
            user.setPhone(phone.trim());
            user.setPassword(password.trim());
            userService.register(user);

            resp.sendRedirect(req.getContextPath() + "/login?success=Registration successful! Please login.");

        } catch (com.carepulse.util.CarePulseException e) {
            req.setAttribute("error", "Registration failed: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An unexpected system error occurred. Please try again.");
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
        }
    }
}
