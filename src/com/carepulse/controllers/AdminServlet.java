package com.carepulse.controllers;

import com.carepulse.model.Appointment;
import com.carepulse.model.User;
import com.carepulse.service.AppointmentService;
import com.carepulse.service.DoctorService;
import com.carepulse.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Admin dashboard and user management.
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final DoctorService doctorService = new DoctorService();
    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if (action == null) {
                // Dashboard
                List<User> patients = userService.getAllPatients();
                List<Appointment> recentAppointments = appointmentService.getAll();

                req.setAttribute("totalPatients", patients.size());
                req.setAttribute("totalDoctors", doctorService.getAll().size());
                req.setAttribute("totalAppointments", appointmentService.countAll());
                req.setAttribute("recentAppointments", recentAppointments.size() > 5 ?
                        recentAppointments.subList(0, 5) : recentAppointments);

                req.getRequestDispatcher("/WEB-INF/pages/admin/dashboard.jsp").forward(req, resp);

            } else if ("users".equals(action)) {
                req.setAttribute("users", userService.getAllPatients());
                req.getRequestDispatcher("/WEB-INF/pages/admin/manage-users.jsp").forward(req, resp);

            } else if ("unlock".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                userService.unlockUser(id);
                resp.sendRedirect(req.getContextPath() + "/admin?action=users&success=User unlocked successfully.");

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                userService.deleteUser(id);
                resp.sendRedirect(req.getContextPath() + "/admin?action=users&success=User deleted successfully.");

            } else {
                resp.sendRedirect(req.getContextPath() + "/admin");
            }
        } catch (com.carepulse.util.CarePulseException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An unexpected system error occurred. Please try again.");
            req.getRequestDispatcher("/WEB-INF/pages/admin/dashboard.jsp").forward(req, resp);
        }
    }
}
