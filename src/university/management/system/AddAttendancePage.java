package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class AddAttendancePage extends JFrame {

    AddAttendancePage(String teacherEmpId) {
        setTitle("Mark Attendance");
        setSize(950, 650);
        setLocation(280, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Mark Attendance - Class Students");
        heading.setBounds(220, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(heading);

        int y = 130;

        try {
            Conn c = new Conn();

            // Get Teacher's assigned class
            String classQuery = "SELECT class_teacher FROM teacher WHERE empId=?";
            PreparedStatement ps1 = c.c.prepareStatement(classQuery);
            ps1.setString(1, teacherEmpId);
            ResultSet rs1 = ps1.executeQuery();
            String teacherClass = "";
            if (rs1.next()) {
                teacherClass = rs1.getString("class_teacher");
            }

            // Show Class Name
            JLabel classLabel = new JLabel("Class: " + teacherClass);
            classLabel.setBounds(100, 80, 400, 30);
            classLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
            add(classLabel);

            // Today's date
            String today = LocalDate.now().toString();

            // Get Students
            String studentQuery = "SELECT * FROM student WHERE class_name=? ORDER BY name";
            PreparedStatement ps2 = c.c.prepareStatement(studentQuery);
            ps2.setString(1, teacherClass);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                String studentName = rs2.getString("name");
                String rollno = rs2.getString("rollno");

                // Student Name
                JLabel nameLabel = new JLabel(studentName + " (" + rollno + ")");
                nameLabel.setBounds(100, y, 250, 30);
                nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
                add(nameLabel);

                // Present Button
                JButton presentBtn = new JButton("Present");
                presentBtn.setBounds(380, y, 110, 30);
                presentBtn.setBackground(new Color(40, 167, 69));
                presentBtn.setForeground(Color.WHITE);
                add(presentBtn);

                // Absent Button
                JButton absentBtn = new JButton("Absent");
                absentBtn.setBounds(500, y, 110, 30);
                absentBtn.setBackground(new Color(220, 53, 69));
                absentBtn.setForeground(Color.WHITE);
                add(absentBtn);

                // Status Label
                JLabel statusLabel = new JLabel("Not Marked");
                statusLabel.setBounds(620, y, 200, 30);
                statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
                statusLabel.setForeground(Color.ORANGE);
                add(statusLabel);

                // Check today's attendance
                String checkQuery = "SELECT status FROM attendance WHERE rollno=? AND teacher_empId=? AND date=?";
                PreparedStatement psCheck = c.c.prepareStatement(checkQuery);
                psCheck.setString(1, rollno);
                psCheck.setString(2, teacherEmpId);
                psCheck.setString(3, today);
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    String status = rsCheck.getString("status");
                    statusLabel.setText(status);
                    statusLabel.setForeground("Present".equalsIgnoreCase(status) ? Color.GREEN : Color.RED);

                    // Disable buttons if already marked
                    presentBtn.setEnabled(false);
                    absentBtn.setEnabled(false);
                }

                // Button Actions
                final String finalRoll = rollno;
                final JLabel finalStatus = statusLabel;

                presentBtn.addActionListener(e -> {
                    saveAttendance(finalRoll, teacherEmpId, today, "Present", finalStatus, presentBtn, absentBtn);
                });

                absentBtn.addActionListener(e -> {
                    saveAttendance(finalRoll, teacherEmpId, today, "Absent", finalStatus, presentBtn, absentBtn);
                });

                y += 50;
            }

            if (y == 130) {
                JLabel noStudent = new JLabel("No students found for this class.");
                noStudent.setBounds(300, 200, 400, 30);
                noStudent.setFont(new Font("Tahoma", Font.BOLD, 16));
                add(noStudent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    private void saveAttendance(String rollno, String teacherEmpId, String date, String status,
                                JLabel statusLabel, JButton presentBtn, JButton absentBtn) {
        try {
            Conn c = new Conn();

            String query = "REPLACE INTO attendance (rollno, teacher_empId, date, status) " +
                           "VALUES (?, ?, ?, ?)";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ps.setString(2, teacherEmpId);
            ps.setString(3, date);
            ps.setString(4, status);
            ps.executeUpdate();

            // Update UI
            statusLabel.setText(status);
            statusLabel.setForeground("Present".equals(status) ? Color.GREEN : Color.RED);

            // Disable buttons after marking
            presentBtn.setEnabled(false);
            absentBtn.setEnabled(false);

            JOptionPane.showMessageDialog(this, "Attendance marked as " + status + "!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving attendance:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AddAttendancePage("101455"); // test with your teacher empId
    }
}