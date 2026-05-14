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

// Servlet that handles user login.
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Redirect the user to their dashboard if they are already signed in.
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            String role = (String) session.getAttribute("role");
            if ("admin".equals(role)) {
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                resp.sendRedirect(req.getContextPath() + "/patient");
            }
            return;
        }
        req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Check that both fields were supplied.
        if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password)) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userService.login(email.trim(), password.trim());
            if (user == null) {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
                return;
            }

            // Create the session for the authenticated user.
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("role", user.getRole());
            session.setMaxInactiveInterval(1800);

            // Send the user to the dashboard for their role.
            if ("admin".equals(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                resp.sendRedirect(req.getContextPath() + "/patient");
            }

        } catch (com.carepulse.util.CarePulseException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An unexpected system error occurred. Please try again.");
            req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
        }
    }
}
