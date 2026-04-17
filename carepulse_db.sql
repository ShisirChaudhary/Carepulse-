-- =====================================================
-- CarePulse - Community Health Appointment Management
-- Database: carepulse_db
-- =====================================================

DROP DATABASE IF EXISTS carepulse_db;
CREATE DATABASE carepulse_db;
USE carepulse_db;

-- ---------------------------------------------------
-- Table: users
-- ---------------------------------------------------
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    role ENUM('admin', 'patient') NOT NULL DEFAULT 'patient',
    failed_attempts INT DEFAULT 0,
    is_locked TINYINT DEFAULT 0,
    reset_token VARCHAR(100) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Table: doctors
-- ---------------------------------------------------
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    contact VARCHAR(15),
    email VARCHAR(100),
    available TINYINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------
-- Table: appointments
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
-- Seed Data: Users
-- Passwords encrypted with AES/ECB/PKCS5Padding
-- Key: CarePulse@2024!! (16 chars)
-- ---------------------------------------------------
INSERT INTO users (full_name, email, password, phone, role) VALUES
('Admin User', 'admin@carepulse.com', 'ir27owCHJwgncE+ef5gWmA==', '9800000001', 'admin'),
('John Doe', 'john@carepulse.com', 'bMS3KHz6yaoSiwXdRS/DZQ==', '9800000002', 'patient');

-- ---------------------------------------------------
-- Seed Data: Doctors
-- ---------------------------------------------------
INSERT INTO doctors (full_name, specialization, contact, email, available) VALUES
('Dr. Sarah Johnson', 'Cardiology', '9801000001', 'sarah.johnson@carepulse.com', 1),
('Dr. Michael Chen', 'Dermatology', '9801000002', 'michael.chen@carepulse.com', 1),
('Dr. Emily Davis', 'Pediatrics', '9801000003', 'emily.davis@carepulse.com', 1),
('Dr. Robert Wilson', 'Orthopedics', '9801000004', 'robert.wilson@carepulse.com', 0);

-- ---------------------------------------------------
-- Seed Data: Appointments
-- ---------------------------------------------------
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES
(2, 1, CURDATE() + INTERVAL 3 DAY, '10:00 AM', 'pending', 'Regular heart checkup'),
(2, 3, CURDATE() + INTERVAL 7 DAY, '02:00 PM', 'confirmed', 'Follow-up visit for child vaccination');
