package com.carepulse.controllers;

import com.carepulse.model.Doctor;
import com.carepulse.service.DoctorService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles doctor CRUD operations (admin only).
 */
@WebServlet("/admin/doctors")
public class DoctorServlet extends HttpServlet {

    private final DoctorService doctorService = new DoctorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if (action == null) {
                // List all doctors
                req.setAttribute("doctors", doctorService.getAll());
                req.getRequestDispatcher("/WEB-INF/pages/admin/manage-doctors.jsp").forward(req, resp);

            } else if ("add".equals(action)) {
                req.getRequestDispatcher("/WEB-INF/pages/admin/doctor-form.jsp").forward(req, resp);

            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Doctor doctor = doctorService.getById(id);
                req.setAttribute("doctor", doctor);
                req.getRequestDispatcher("/WEB-INF/pages/admin/doctor-form.jsp").forward(req, resp);

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                doctorService.delete(id);
                resp.sendRedirect(req.getContextPath() + "/admin/doctors?success=Doctor deleted successfully.");

            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/doctors");
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("doctors", null);
            req.getRequestDispatcher("/WEB-INF/pages/admin/manage-doctors.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String fullName = req.getParameter("fullName");
        String specialization = req.getParameter("specialization");
        String contact = req.getParameter("contact");
        String email = req.getParameter("email");
        String availableStr = req.getParameter("available");

        // Validate required
        if (ValidationUtil.isEmpty(fullName) || ValidationUtil.isEmpty(specialization)) {
            req.setAttribute("error", "Name and specialization are required.");
            req.getRequestDispatcher("/WEB-INF/pages/admin/doctor-form.jsp").forward(req, resp);
            return;
        }

        try {
            Doctor doctor = new Doctor();
            doctor.setFullName(fullName.trim());
            doctor.setSpecialization(specialization.trim());
            doctor.setContact(contact != null ? contact.trim() : "");
            doctor.setEmail(email != null ? email.trim() : "");
            doctor.setAvailable(availableStr != null);

            if (ValidationUtil.isEmpty(idStr)) {
                // Add new doctor
                doctorService.add(doctor);
                resp.sendRedirect(req.getContextPath() + "/admin/doctors?success=Doctor added successfully.");
            } else {
                // Update existing doctor
                doctor.setId(Integer.parseInt(idStr));
                doctorService.update(doctor);
                resp.sendRedirect(req.getContextPath() + "/admin/doctors?success=Doctor updated successfully.");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Operation failed: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/admin/doctor-form.jsp").forward(req, resp);
        }
    }
}
