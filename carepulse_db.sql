-- =====================================================
-- CarePulse - Community Health Appointment Management
-- Database: carepulse_db
-- Schema is in 3rd Normal Form (3NF):
--   * 1NF: every column holds atomic values, every table has a PK
--   * 2NF: all PKs are single columns, so no partial dependencies
--   * 3NF: redundant repeated values (specializations, roles) are
--          extracted into lookup tables and referenced by FK
-- =====================================================

DROP DATABASE IF EXISTS carepulse_db;
CREATE DATABASE carepulse_db;
USE carepulse_db;

-- ---------------------------------------------------
-- Lookup table: roles
-- Removes redundancy in users.role and lets us add
-- role-level metadata (description, permissions) later
-- without altering the users table.
-- ---------------------------------------------------
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(150)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Lookup table: specializations
-- Replaces the repeated free-text doctors.specialization
-- column. Many doctors share a single specialization
-- (e.g. multiple Cardiologists), so this eliminates
-- duplicated string data and enables consistent search.
-- ---------------------------------------------------
CREATE TABLE specializations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Table: users
-- role_id references roles.id (was previously an ENUM)
-- ---------------------------------------------------
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    role_id INT NOT NULL,
    failed_attempts INT DEFAULT 0,
    is_locked TINYINT DEFAULT 0,
    reset_token VARCHAR(100) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Table: doctors
-- specialization_id references specializations.id
-- ---------------------------------------------------
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    specialization_id INT NOT NULL,
    contact VARCHAR(15),
    email VARCHAR(100),
    available TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (specialization_id) REFERENCES specializations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Table: appointments
-- (status kept as ENUM - closed set, no redundancy
--  benefit from a separate lookup table)
-- ---------------------------------------------------
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time VARCHAR(20) NOT NULL,
    status ENUM('pending', 'confirmed', 'cancelled', 'completed') DEFAULT 'pending',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Table: contact_inquiries
-- Stores messages submitted via the public Contact form
-- ---------------------------------------------------
CREATE TABLE contact_inquiries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    status ENUM('open', 'resolved') NOT NULL DEFAULT 'open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Seed Data: Roles
-- ---------------------------------------------------
INSERT INTO roles (name, description) VALUES
('admin', 'Administrator with full system access'),
('patient', 'Patient with self-service appointment booking');

-- ---------------------------------------------------
-- Seed Data: Specializations
-- ---------------------------------------------------
INSERT INTO specializations (name, description) VALUES
('Cardiology', 'Heart and cardiovascular system'),
('Dermatology', 'Skin, hair, and nails'),
('Pediatrics', 'Children and adolescents'),
('Orthopedics', 'Bones, joints, and muscles'),
('General Practice', 'Primary care and routine checkups');

-- ---------------------------------------------------
-- Seed Data: Users
-- Passwords encrypted with AES/ECB/PKCS5Padding
-- Key: CarePulse@2024!! (16 chars)
-- ---------------------------------------------------
INSERT INTO users (full_name, email, password, phone, role_id) VALUES
('Admin User', 'admin@carepulse.com', 'ir27owCHJwgncE+ef5gWmA==', '9800000001',
    (SELECT id FROM roles WHERE name = 'admin')),
('John Doe', 'john@carepulse.com', 'bMS3KHz6yaoSiwXdRS/DZQ==', '9800000002',
    (SELECT id FROM roles WHERE name = 'patient'));

-- ---------------------------------------------------
-- Seed Data: Doctors
-- ---------------------------------------------------
INSERT INTO doctors (full_name, specialization_id, contact, email, available) VALUES
('Dr. Sarah Johnson',  (SELECT id FROM specializations WHERE name = 'Cardiology'),  '9801000001', 'sarah.johnson@carepulse.com', 1),
('Dr. Michael Chen',   (SELECT id FROM specializations WHERE name = 'Dermatology'), '9801000002', 'michael.chen@carepulse.com', 1),
('Dr. Emily Davis',    (SELECT id FROM specializations WHERE name = 'Pediatrics'),  '9801000003', 'emily.davis@carepulse.com', 1),
('Dr. Robert Wilson',  (SELECT id FROM specializations WHERE name = 'Orthopedics'), '9801000004', 'robert.wilson@carepulse.com', 0);

-- ---------------------------------------------------
-- Seed Data: Appointments
-- ---------------------------------------------------
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES
(2, 1, CURDATE() + INTERVAL 3 DAY, '10:00 AM', 'pending', 'Regular heart checkup'),
(2, 3, CURDATE() + INTERVAL 7 DAY, '02:00 PM', 'confirmed', 'Follow-up visit for child vaccination');

-- ---------------------------------------------------
-- Seed Data: Contact Inquiries
-- ---------------------------------------------------
INSERT INTO contact_inquiries (full_name, email, subject, message, status) VALUES
('Anita Sharma', 'anita.sharma@example.com', 'Question about specialists', 'Do you have a paediatrician available on weekends? My son needs a routine check-up.', 'open'),
('Ramesh Karki', 'ramesh.karki@example.com', 'Appointment confirmation', 'I booked an appointment yesterday but never received confirmation. Could you please check?', 'resolved');
