package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class ApplyLeavePage extends JFrame implements ActionListener {

    private String rollno;
    private JDateChooser startDateChooser, endDateChooser;
    private JTextArea reasonArea;
    private JButton submitBtn, backBtn;

    public ApplyLeavePage(String rollno) {
        this.rollno = rollno;

        setTitle("Apply for Leave - " + rollno);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 250));

        // Heading
        JLabel heading = new JLabel("Apply for Leave");
        heading.setBounds(200, 20, 300, 35);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(30, 60, 120));
        add(heading);

        // Roll No Label
        JLabel lblRoll = new JLabel("Roll No: " + rollno);
        lblRoll.setBounds(80, 80, 400, 25);
        lblRoll.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblRoll);

        // Start Date
        JLabel lblStart = new JLabel("Start Date:");
        lblStart.setBounds(80, 120, 150, 25);
        lblStart.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(lblStart);

        startDateChooser = new JDateChooser();
        startDateChooser.setBounds(220, 120, 300, 30);
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        add(startDateChooser);

        // End Date
        JLabel lblEnd = new JLabel("End Date:");
        lblEnd.setBounds(80, 170, 150, 25);
        lblEnd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(lblEnd);

        endDateChooser = new JDateChooser();
        endDateChooser.setBounds(220, 170, 300, 30);
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        add(endDateChooser);

        // Reason
        JLabel lblReason = new JLabel("Reason:");
        lblReason.setBounds(80, 220, 150, 25);
        lblReason.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(lblReason);

        reasonArea = new JTextArea();
        reasonArea.setBounds(220, 220, 300, 150);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reasonArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        add(reasonArea);

        // Submit Button
        submitBtn = new JButton("Submit Application");
        submitBtn.setBounds(150, 400, 200, 40);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitBtn.setBackground(new Color(40, 167, 69));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(this);
        add(submitBtn);

        // Back Button
        backBtn = new JButton("Back");
        backBtn.setBounds(370, 400, 150, 40);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.setBackground(new Color(220, 53, 69));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> dispose());
        add(backBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            Date startDate = startDateChooser.getDate();
            Date endDate = endDateChooser.getDate();
            String reason = reasonArea.getText().trim();

            // Validation 1: Required fields not empty
            if (startDate == null || endDate == null || reason.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate startLocal = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocal = endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();

            // Validation 2: Start date before end date
            if (startLocal.isAfter(endLocal)) {
                JOptionPane.showMessageDialog(this, "Start date must be before end date!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 3: Past dates not allowed
            if (startLocal.isBefore(today)) {
                JOptionPane.showMessageDialog(this, "Cannot apply for past dates!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 4: No overlapping leaves
            if (hasOverlappingLeaves(startLocal, endLocal)) {
                JOptionPane.showMessageDialog(this, "Leave overlaps with existing application!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 5: Max duration 7 days
            long days = ChronoUnit.DAYS.between(startLocal, endLocal) + 1;
            if (days > 7) {
                JOptionPane.showMessageDialog(this, "Maximum leave duration is 7 days!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 6: Min notice 1 day
            if (startLocal.equals(today)) {
                JOptionPane.showMessageDialog(this, "Leave must be applied at least 1 day in advance!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 7: Max 3 leaves per month
            if (exceedsMonthlyLeaves(startLocal)) {
                JOptionPane.showMessageDialog(this, "You have already applied for maximum 3 leaves this month!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 8: Reason min 10 chars
            if (reason.length() < 10) {
                JOptionPane.showMessageDialog(this, "Reason must be at least 10 characters!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 9: Future dates max 30 days ahead
            if (startLocal.isAfter(today.plusDays(30))) {
                JOptionPane.showMessageDialog(this, "Leave can be applied max 30 days in advance!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 10: End date not too far
            if (endLocal.isAfter(today.plusMonths(3))) {
                JOptionPane.showMessageDialog(this, "Leave cannot be applied for more than 3 months in advance!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation 11: Reason no special characters only (basic check)
            if (!reason.matches("^[a-zA-Z0-9\\s.,!?()-]+$")) {
                JOptionPane.showMessageDialog(this, "Reason contains invalid characters!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // All validations passed → save
            saveLeaveApplication(startLocal, endLocal, reason);
        }
    }

    private boolean hasOverlappingLeaves(LocalDate start, LocalDate end) {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM leave_application WHERE rollno = ? " +
                           "AND ((start_date <= ? AND end_date >= ?) OR " +
                           "(start_date <= ? AND end_date >= ?) OR " +
                           "(start_date >= ? AND end_date <= ?))";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ps.setDate(2, java.sql.Date.valueOf(end));
            ps.setDate(3, java.sql.Date.valueOf(start));
            ps.setDate(4, java.sql.Date.valueOf(start));
            ps.setDate(5, java.sql.Date.valueOf(end));
            ps.setDate(6, java.sql.Date.valueOf(start));
            ps.setDate(7, java.sql.Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return true; // Fail-safe: assume overlap on error
        }
    }

    private boolean exceedsMonthlyLeaves(LocalDate start) {
        try {
            Conn c = new Conn();
            LocalDate monthStart = start.withDayOfMonth(1);
            LocalDate monthEnd = start.withDayOfMonth(start.lengthOfMonth());
            String query = "SELECT COUNT(*) FROM leave_application WHERE rollno = ? " +
                           "AND start_date BETWEEN ? AND ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ps.setDate(2, java.sql.Date.valueOf(monthStart));
            ps.setDate(3, java.sql.Date.valueOf(monthEnd));
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) >= 3;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true; // Fail-safe
        }
    }

    private void saveLeaveApplication(LocalDate start, LocalDate end, String reason) {
        try {
            Conn c = new Conn();
            String query = "INSERT INTO leave_application (rollno, start_date, end_date, reason) " +
                           "VALUES (?, ?, ?, ?)";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ps.setDate(2, java.sql.Date.valueOf(start));
            ps.setDate(3, java.sql.Date.valueOf(end));
            ps.setString(4, reason);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Leave application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting application:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ApplyLeavePage("15333936"); // test rollno
    }
}