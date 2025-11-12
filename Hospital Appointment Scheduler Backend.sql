CREATE DATABASE hospital_appointment;
USE hospital_appointment;
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100),
    doctor_name VARCHAR(100),
    department VARCHAR(50),
    appointment_date DATE,
    appointment_time TIME,
    contact_no VARCHAR(15),
    status VARCHAR(20) DEFAULT 'Scheduled'
);
SELECT*FROM appointments;
