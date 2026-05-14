package com.carepulse.controllers;

import com.carepulse.service.DoctorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Servlet for the patient-facing Find a Doctor page with search and sort support.
@WebServlet("/patient/doctors")
public class PatientDoctorsServlet extends HttpServlet {

    private final DoctorService doctorService = new DoctorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String search = req.getParameter("search");
        String sort = req.getParameter("sort");
        String dir = req.getParameter("dir");
        try {
            req.setAttribute("doctors", doctorService.searchAvailable(search, sort, dir));
            req.setAttribute("searchKeyword", search != null ? search.trim() : "");
            req.setAttribute("sortKey", sort);
            req.setAttribute("sortDir", dir);
            req.getRequestDispatcher("/WEB-INF/pages/patient/find-doctor.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/patient/find-doctor.jsp").forward(req, resp);
        }
    }
}
