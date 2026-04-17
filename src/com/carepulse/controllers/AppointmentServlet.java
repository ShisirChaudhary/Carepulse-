package com.carepulse.controllers;

import com.carepulse.model.Appointment;
import com.carepulse.service.AppointmentService;
import com.carepulse.service.DoctorService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

// Handles all the appointment stuff for admins and patients
@WebServlet({"/admin/appointments", "/patient/appointments"})
public class AppointmentServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();
    private final DoctorService doctorService = new DoctorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String role = (String) session.getAttribute("role");
        String action = req.getParameter("action");

        try {
            if ("admin".equals(role)) {
                handleAdminGet(req, resp, action);
            } else {
                handlePatientGet(req, resp, action, session);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            if ("admin".equals(role)) {
                req.getRequestDispatcher("/WEB-INF/pages/admin/manage-appointments.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/WEB-INF/pages/patient/my-appointments.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        int patientId = (int) session.getAttribute("userId");

        String doctorIdStr = req.getParameter("doctorId");
        String date = req.getParameter("appointmentDate");
        String time = req.getParameter("appointmentTime");
        String notes = req.getParameter("notes");

        // Check if anything is missing
        if (ValidationUtil.isEmpty(doctorIdStr) || ValidationUtil.isEmpty(date) || ValidationUtil.isEmpty(time)) {
            req.setAttribute("error", "Doctor, date, and time are required.");
            try {
                req.setAttribute("doctors", doctorService.getAvailable());
            } catch (Exception e) {
                // ignore
            }
            req.getRequestDispatcher("/WEB-INF/pages/patient/book-appointment.jsp").forward(req, resp);
            return;
        }

        try {
            Appointment a = new Appointment();
            a.setPatientId(patientId);
            a.setDoctorId(Integer.parseInt(doctorIdStr));
            a.setAppointmentDate(date.trim());
            a.setAppointmentTime(time.trim());
            a.setNotes(notes != null ? notes.trim() : "");
            appointmentService.book(a);
            resp.sendRedirect(req.getContextPath() + "/patient/appointments?success=Appointment booked successfully!");
        } catch (Exception e) {
            req.setAttribute("error", "Booking failed: " + e.getMessage());
            try {
                req.setAttribute("doctors", doctorService.getAvailable());
            } catch (Exception ex) {
                // ignore
            }
            req.getRequestDispatcher("/WEB-INF/pages/patient/book-appointment.jsp").forward(req, resp);
        }
    }

    private void handleAdminGet(HttpServletRequest req, HttpServletResponse resp, String action)
            throws Exception, ServletException, IOException {
        if ("status".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            String status = req.getParameter("value");
            appointmentService.updateStatus(id, status);
            resp.sendRedirect(req.getContextPath() + "/admin/appointments?success=Status updated successfully.");
            return;
        }

        // Search if keyword exists, otherwise list everything
        String search = req.getParameter("search");
        List<Appointment> appointments;
        if (!ValidationUtil.isEmpty(search)) {
            appointments = appointmentService.search(search.trim());
            req.setAttribute("searchKeyword", search.trim());
        } else {
            appointments = appointmentService.getAll();
        }
        req.setAttribute("appointments", appointments);
        req.getRequestDispatcher("/WEB-INF/pages/admin/manage-appointments.jsp").forward(req, resp);
    }

    private void handlePatientGet(HttpServletRequest req, HttpServletResponse resp, String action, HttpSession session)
            throws Exception, ServletException, IOException {
        int patientId = (int) session.getAttribute("userId");

        if ("book".equals(action)) {
            req.setAttribute("doctors", doctorService.getAvailable());
            req.getRequestDispatcher("/WEB-INF/pages/patient/book-appointment.jsp").forward(req, resp);
            return;
        }

        if ("cancel".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            appointmentService.cancel(id, patientId);
            resp.sendRedirect(req.getContextPath() + "/patient/appointments?success=Appointment cancelled.");
            return;
        }

        // Get the list of appointments for this patient
        req.setAttribute("appointments", appointmentService.getByPatient(patientId));
        req.getRequestDispatcher("/WEB-INF/pages/patient/my-appointments.jsp").forward(req, resp);
    }
}
