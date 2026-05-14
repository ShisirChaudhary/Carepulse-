package com.carepulse.controllers;

import com.carepulse.model.Doctor;
import com.carepulse.service.DoctorService;
import com.carepulse.service.SpecializationService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet that handles doctor create, read, update and delete actions for the admin.
@WebServlet("/admin/doctors")
public class DoctorServlet extends HttpServlet {

    private final DoctorService doctorService = new DoctorService();
    private final SpecializationService specializationService = new SpecializationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if (action == null) {
                String sort = req.getParameter("sort");
                String dir = req.getParameter("dir");
                req.setAttribute("doctors", doctorService.getAll(sort, dir));
                req.setAttribute("sortKey", sort);
                req.setAttribute("sortDir", dir);
                req.getRequestDispatcher("/WEB-INF/pages/admin/manage-doctors.jsp").forward(req, resp);

            } else if ("add".equals(action)) {
                req.setAttribute("specializations", specializationService.getAll());
                req.getRequestDispatcher("/WEB-INF/pages/admin/doctor-form.jsp").forward(req, resp);

            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Doctor doctor = doctorService.getById(id);
                req.setAttribute("doctor", doctor);
                req.setAttribute("specializations", specializationService.getAll());
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
        String specializationIdStr = req.getParameter("specializationId");
        String contact = req.getParameter("contact");
        String email = req.getParameter("email");
        String availableStr = req.getParameter("available");

        // Name and specialization are required.
        if (ValidationUtil.isEmpty(fullName) || ValidationUtil.isEmpty(specializationIdStr)) {
            req.setAttribute("error", "Name and specialization are required.");
            try {
                req.setAttribute("specializations", specializationService.getAll());
            } catch (Exception ignored) {
            }
            req.getRequestDispatcher("/WEB-INF/pages/admin/doctor-form.jsp").forward(req, resp);
            return;
        }

        try {
            Doctor doctor = new Doctor();
            doctor.setFullName(fullName.trim());
            doctor.setSpecializationId(Integer.parseInt(specializationIdStr));
            doctor.setContact(contact != null ? contact.trim() : "");
            doctor.setEmail(email != null ? email.trim() : "");
            doctor.setAvailable(availableStr != null);

            if (ValidationUtil.isEmpty(idStr)) {
                doctorService.add(doctor);
                resp.sendRedirect(req.getContextPath() + "/admin/doctors?success=Doctor added successfully.");
            } else {
                doctor.setId(Integer.parseInt(idStr));
                doctorService.update(doctor);
                resp.sendRedirect(req.getContextPath() + "/admin/doctors?success=Doctor updated successfully.");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Operation failed: " + e.getMessage());
            try {
                req.setAttribute("specializations", specializationService.getAll());
            } catch (Exception ignored) {
            }
            req.getRequestDispatcher("/WEB-INF/pages/admin/doctor-form.jsp").forward(req, resp);
        }
    }
}
