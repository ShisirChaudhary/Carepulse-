CarePulse - Community Health Appointment System

I built this project to manage healthcare appointments for local communities. It's a Java-based web app that handles everything from doctor schedules to patient bookings, with an admin back-office and public information pages.

What it does

For Admins
* Dashboard with live stats (patients, doctors, appointments, open inquiries).
* Full CRUD for the doctor list (add, edit, delete, mark availability).
* Full CRUD for patient accounts: create, edit, unlock locked accounts, or delete.
* Manage every appointment: confirm, cancel, complete, or search by patient/doctor name.
* Inbox for messages submitted via the public Contact form (resolve or delete).

For Patients
* Self-service registration, login, and password recovery via reset token.
* Profile page to update personal info or change password.
* Find a Doctor directory with search by doctor name or specialization.
* Book appointments — pick a doctor, date, and time slot.
* View, cancel, and track their own appointments.
* Recently added doctors highlighted on the dashboard.

Public (no login required)
* About page describing the system and its purpose.
* Contact page with an inquiry form that lands directly in the admin inbox.

Under the hood
* AES encryption for stored passwords (security matters!).
* Role-based access via servlet filter — patients can never reach admin URLs.
* Account auto-locks after 5 failed login attempts.
* Sessions expire after 30 minutes of inactivity.
* Input validation (regex-driven) plus duplicate email/phone detection at registration.

The Tech Stack
* Backend: Java 11 / Jakarta EE (Servlets and JSP)
* Server: Apache Tomcat 10.1
* Database: MySQL (set up through XAMPP)
* Frontend: Plain CSS — no Bootstrap, no heavy frameworks.

Getting it running

1. DB setup
   * Make sure MySQL is running (default port 3306).
   * Import `carepulse_db.sql` from the project root (phpMyAdmin makes it easy). It creates the database, tables, and seed data including a couple of sample contact inquiries.

2. IDE Setup (Eclipse)
   * Import as a Dynamic Web Project.
   * Crucial: check `WebContent/WEB-INF/lib`. The MySQL connector jar (`mysql-connector-j-9.6.0.jar`) belongs there. Don't add extra servlet jars — Tomcat handles that.
   * Hook it up to a Tomcat 10.1 server.

3. Run
   * Right-click and "Run on Server".
   * Available at http://localhost:8080/carepulse.

Test Accounts
* Admin: admin@carepulse.com / Admin@123
* Patient: john@carepulse.com / Patient@123

Project Structure (key folders)
* `src/com/carepulse/config` — DB connection helper.
* `src/com/carepulse/model` — JavaBeans (User, Doctor, Appointment, Inquiry).
* `src/com/carepulse/service` — business logic and JDBC for each entity.
* `src/com/carepulse/controllers` — servlets that handle requests and forward to JSP views.
* `src/com/carepulse/filters` — AuthFilter for session/role checks.
* `src/com/carepulse/util` — AES encryption and form validation helpers.
* `WebContent/WEB-INF/pages` — JSP views (admin, patient, common, error, plus public pages).
* `WebContent/css/style.css` — single stylesheet.

Feel free to poke around and let me know if something breaks!
