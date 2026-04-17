CarePulse - Community Health Appointment System

I built this project to manage healthcare appointments for local communities. It's a Java-based web app that handles everything from doctor schedules to patient bookings.

What it does
* For Admins: I added a dashboard to track stats, plus tools to manage the doctor list and keep an eye on all appointments.
* For Patients: You can sign up, pick a doctor, and book a slot. There's also an option to cancel things if you need to and update your profile.
* Under the hood: I used AES encryption for passwords (security matters!) and implemented role-based access through filters so people can't just access pages they aren't supposed to.

The Tech Stack
* Backend: Java 11 / Jakarta EE (using Servlets and JSP)
* Server: Running on Apache Tomcat 10.1
* Database: MySQL (set up through XAMPP)
* Frontend: Just plain CSS — no heavy frameworks like Bootstrap here.

Getting it running

1. DB setup:
   * Make sure MySQL is running (default port 3306).
   * I've included carepulse_db.sql in the root. Import that into your database (phpMyAdmin makes it easy).

2. IDE Setup (Eclipse):
   * Import it as a "Dynamic Web Project".
   * Crucial: Check WebContent/WEB-INF/lib. I've added mysql-connector-j-9.6.0.jar there. Don't add extra servlet jars; let the Tomcat runtime handle that.
   * Hook it up to a Tomcat 10.1 server.

3. Run:
   * Just right-click and "Run on Server". It should be available at http://localhost:8080/carepulse.

Test Accounts
* Admin: admin@carepulse.com / Admin@123
* Patient: john@carepulse.com / Patient@123

Feel free to poke around and let me know if something breaks!
