package com.carepulse.controllers;

import com.carepulse.model.Appointment;
import com.carepulse.service.AppointmentService;
import com.carepulse.service.DoctorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

// Servlet for the patient dashboard.
@WebServlet("/patient")
public class PatientServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();
    private final DoctorService doctorService = new DoctorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        int patientId = (int) session.getAttribute("userId");

        try {
            List<Appointment> appointments = appointmentService.getByPatient(patientId);
            int count = appointmentService.countByPatient(patientId);

            req.setAttribute("appointments", appointments.size() > 5 ?
                    appointments.subList(0, 5) : appointments);
            req.setAttribute("appointmentCount", count);
            req.setAttribute("recentDoctors", doctorService.getRecent(4));

            req.getRequestDispatcher("/WEB-INF/pages/patient/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/patient/dashboard.jsp").forward(req, resp);
        }
    }
}
