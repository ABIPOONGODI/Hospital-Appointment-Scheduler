package com.hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HospitalAppointmentScheduler extends JFrame implements ActionListener {

    // GUI components
    JTextField txtPatientName, txtDoctorName, txtDepartment, txtDate, txtTime, txtContact;
    JButton btnBook, btnView, btnCancel;
    Connection con;

    // Database credentials
    final String URL = "jdbc:mysql://localhost:3306/hospital_appointment";
    final String USER = "root";
    final String PASS = "Harini@2006";  // 🔹 Replace with your actual MySQL password

    public HospitalAppointmentScheduler() {
        setTitle("Hospital Appointment Scheduler");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));

        // Labels and TextFields
        add(new JLabel("Patient Name:"));
        txtPatientName = new JTextField();
        add(txtPatientName);

        add(new JLabel("Doctor Name:"));
        txtDoctorName = new JTextField();
        add(txtDoctorName);

        add(new JLabel("Department:"));
        txtDepartment = new JTextField();
        add(txtDepartment);

        add(new JLabel("Appointment Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        add(txtDate);

        add(new JLabel("Appointment Time (HH:MM:SS):"));
        txtTime = new JTextField();
        add(txtTime);

        add(new JLabel("Contact No:"));
        txtContact = new JTextField();
        add(txtContact);

        // Buttons
        btnBook = new JButton("Book Appointment");
        btnView = new JButton("View All Appointments");
        btnCancel = new JButton("Cancel Appointment");

        add(btnBook);
        add(btnView);
        add(new JLabel(""));  // Spacer
        add(btnCancel);

        // Button listeners
        btnBook.addActionListener(this);
        btnView.addActionListener(this);
        btnCancel.addActionListener(this);

        connectDB();
        setVisible(true);
    }

    // Database connection
    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            JOptionPane.showMessageDialog(this, "Database Connected Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Error: " + e.getMessage());
        }
    }

    // Button actions
    public void actionPerformed(ActionEvent e) {

        // ✅ BOOK APPOINTMENT
        if (e.getSource() == btnBook) {
            try {
                String name = txtPatientName.getText();
                String doctor = txtDoctorName.getText();
                String dept = txtDepartment.getText();
                String date = txtDate.getText();
                String time = txtTime.getText();
                String contact = txtContact.getText();

                String sql = "INSERT INTO appointments(patient_name, doctor_name, department, appointment_date, appointment_time, contact_no, status) VALUES (?,?,?,?,?,?, 'Scheduled')";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, doctor);
                pst.setString(3, dept);
                pst.setString(4, date);
                pst.setString(5, time);
                pst.setString(6, contact);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        // ✅ VIEW ALL APPOINTMENTS
        if (e.getSource() == btnView) {
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM appointments");

                StringBuilder data = new StringBuilder("ID\tPatient\tDoctor\tDept\tDate\tTime\tStatus\n");
                while (rs.next()) {
                    data.append(rs.getInt("appointment_id")).append("\t")
                        .append(rs.getString("patient_name")).append("\t")
                        .append(rs.getString("doctor_name")).append("\t")
                        .append(rs.getString("department")).append("\t")
                        .append(rs.getDate("appointment_date")).append("\t")
                        .append(rs.getTime("appointment_time")).append("\t")
                        .append(rs.getString("status")).append("\n");
                }

                JTextArea area = new JTextArea(data.toString(), 12, 40);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "All Appointments", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        // ✅ CANCEL APPOINTMENT
        if (e.getSource() == btnCancel) {
            try {
                String name = txtPatientName.getText();
                String sql = "UPDATE appointments SET status='Cancelled' WHERE patient_name=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, name);

                int rows = pst.executeUpdate();
                if (rows > 0)
                    JOptionPane.showMessageDialog(this, "Appointment cancelled for " + name);
                else
                    JOptionPane.showMessageDialog(this, "No matching appointment found!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new HospitalAppointmentScheduler();
    }
}
