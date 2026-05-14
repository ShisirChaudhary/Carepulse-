package com.carepulse.controllers;

import com.carepulse.model.Appointment;
import com.carepulse.model.User;
import com.carepulse.service.AppointmentService;
import com.carepulse.service.DoctorService;
import com.carepulse.service.InquiryService;
import com.carepulse.service.UserService;
import com.carepulse.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Servlet for the admin dashboard and patient management actions.
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final DoctorService doctorService = new DoctorService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final InquiryService inquiryService = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if (action == null) {
                List<User> patients = userService.getAllPatients();
                List<Appointment> recentAppointments = appointmentService.getAll();

                req.setAttribute("totalPatients", patients.size());
                req.setAttribute("totalDoctors", doctorService.getAll().size());
                req.setAttribute("totalAppointments", appointmentService.countAll());
                req.setAttribute("openInquiries", inquiryService.countOpen());
                req.setAttribute("recentAppointments", recentAppointments.size() > 5 ?
                        recentAppointments.subList(0, 5) : recentAppointments);

                req.getRequestDispatcher("/WEB-INF/pages/admin/dashboard.jsp").forward(req, resp);

            } else if ("users".equals(action)) {
                String sort = req.getParameter("sort");
                String dir = req.getParameter("dir");
                req.setAttribute("users", userService.getAllPatients(sort, dir));
                req.setAttribute("sortKey", sort);
                req.setAttribute("sortDir", dir);
                req.getRequestDispatcher("/WEB-INF/pages/admin/manage-users.jsp").forward(req, resp);

            } else if ("addUser".equals(action)) {
                req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);

            } else if ("editUser".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                User user = userService.getById(id);
                if (user == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin?action=users");
                    return;
                }
                req.setAttribute("userBean", user);
                req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);

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
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/admin/dashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("saveUser".equals(action)) {
            saveUser(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin");
    }

    // Handles both add and edit submissions from the patient form.
    private void saveUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");

        boolean isEdit = !ValidationUtil.isEmpty(idStr);

        // Echo the submitted values back so the form retains the user's input on error.
        User formBean = new User();
        if (isEdit) formBean.setId(Integer.parseInt(idStr));
        formBean.setFullName(fullName);
        formBean.setEmail(email);
        formBean.setPhone(phone);
        req.setAttribute("userBean", formBean);

        if (!ValidationUtil.isValidName(fullName)) {
            req.setAttribute("error", "Name must contain only letters and spaces (2-100 chars).");
            req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("error", "Please enter a valid email address.");
            req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            req.setAttribute("error", "Phone must be exactly 10 digits.");
            req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
            return;
        }
        if (!isEdit && !ValidationUtil.isValidPassword(password)) {
            req.setAttribute("error", "Password must be at least 6 characters.");
            req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
            return;
        }

        try {
            if (isEdit) {
                int id = Integer.parseInt(idStr);
                if (userService.emailExistsForOtherUser(email.trim(), id)) {
                    req.setAttribute("error", "Email is already used by another user.");
                    req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
                    return;
                }
                if (userService.phoneExistsForOtherUser(phone.trim(), id)) {
                    req.setAttribute("error", "Phone number is already used by another user.");
                    req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
                    return;
                }
                userService.updateByAdmin(id, fullName.trim(), email.trim(), phone.trim());
                if (!ValidationUtil.isEmpty(password)) {
                    if (!ValidationUtil.isValidPassword(password)) {
                        req.setAttribute("error", "Password must be at least 6 characters.");
                        req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
                        return;
                    }
                    userService.updatePassword(id, password.trim());
                }
                resp.sendRedirect(req.getContextPath() + "/admin?action=users&success=Patient updated successfully.");
            } else {
                if (userService.emailExists(email.trim())) {
                    req.setAttribute("error", "Email is already registered.");
                    req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
                    return;
                }
                if (userService.phoneExists(phone.trim())) {
                    req.setAttribute("error", "Phone number is already registered.");
                    req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
                    return;
                }
                User user = new User();
                user.setFullName(fullName.trim());
                user.setEmail(email.trim());
                user.setPhone(phone.trim());
                user.setPassword(password.trim());
                userService.addByAdmin(user);
                resp.sendRedirect(req.getContextPath() + "/admin?action=users&success=Patient added successfully.");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Operation failed: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/admin/user-form.jsp").forward(req, resp);
        }
    }
}
