package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.UUID;

public class StudentFeePayment extends JFrame {

    private String rollno;
    private JLabel lblCourse, lblSemester, lblFeeAmount, lblStatus;
    private JButton btnPayNow, btnBack;
    private String currentCourseId;
    private String currentSemId;
    private double totalFee = 0;  // Store total fee for comparison

    public StudentFeePayment(String rollno) {
        this.rollno = rollno.trim();
        setTitle("Fees & Payment - " + rollno);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel heading = new JLabel("Fee Payment", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 18);

        int row = 0;

        // Course
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblCourseTitle = new JLabel("Course:");
        lblCourseTitle.setFont(labelFont);
        content.add(lblCourseTitle, gbc);
        gbc.gridx = 1;
        lblCourse = new JLabel("Loading...");
        lblCourse.setFont(valueFont);
        content.add(lblCourse, gbc);
        row++;

        // Semester
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSemesterTitle = new JLabel("Semester:");
        lblSemesterTitle.setFont(labelFont);
        content.add(lblSemesterTitle, gbc);
        gbc.gridx = 1;
        lblSemester = new JLabel("Loading...");
        lblSemester.setFont(valueFont);
        content.add(lblSemester, gbc);
        row++;

        // Total Fee
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblFeeTitle = new JLabel("Total Fee (₹):");
        lblFeeTitle.setFont(labelFont);
        content.add(lblFeeTitle, gbc);
        gbc.gridx = 1;
        lblFeeAmount = new JLabel("—");
        lblFeeAmount.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblFeeAmount.setForeground(new Color(40, 167, 69));
        content.add(lblFeeAmount, gbc);
        row++;

        // Status
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblStatusTitle = new JLabel("Status:");
        lblStatusTitle.setFont(labelFont);
        content.add(lblStatusTitle, gbc);
        gbc.gridx = 1;
        lblStatus = new JLabel("Pending");
        lblStatus.setFont(valueFont);
        lblStatus.setForeground(new Color(220, 53, 69));
        content.add(lblStatus, gbc);
        row++;

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        btnPayNow = new JButton("Pay Now");
        btnPayNow.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPayNow.setBackground(new Color(40, 167, 69));
        btnPayNow.setForeground(Color.WHITE);
        btnPayNow.setPreferredSize(new Dimension(180, 50));
        btnPayNow.addActionListener(e -> performDemoPayment());
        btnPanel.add(btnPayNow);

        btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBackground(new Color(108, 117, 125));
        btnBack.setForeground(Color.WHITE);
        btnBack.setPreferredSize(new Dimension(120, 45));
        btnBack.addActionListener(e -> dispose());
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        content.add(btnPanel, gbc);

        add(content, BorderLayout.CENTER);

        loadStudentFeeDetails();  // Load data + check paid status

        setVisible(true);
    }

    private void performDemoPayment() {
        // Double-check before allowing payment
        if (!btnPayNow.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Fee already paid for this semester.", "Already Paid", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String feeText = lblFeeAmount.getText()
                .replace("₹", "")
                .replace(" ", "")
                .replace(",", "")
                .trim();

        if (feeText.isEmpty() || feeText.equals("—") ||
            feeText.toLowerCase().contains("not") ||
            feeText.toLowerCase().contains("no") ||
            feeText.toLowerCase().contains("error")) {
            JOptionPane.showMessageDialog(this, "No valid fee amount available to pay.", "Cannot Proceed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(feeText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid fee amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
            "You are about to pay ₹ " + String.format("%,.2f", amount) + "\n" +
            "This is a demo payment. Proceed?",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Conn c = new Conn();

            // Final check: Is fee already fully paid?
            String checkQuery = "SELECT COALESCE(SUM(amount_paid), 0) AS total_paid " +
                               "FROM fee_payment WHERE rollno = ? AND courseid = ? AND semid = ?";
            PreparedStatement psCheck = c.c.prepareStatement(checkQuery);
            psCheck.setString(1, rollno);
            psCheck.setString(2, currentCourseId);
            psCheck.setString(3, currentSemId);
            ResultSet rsCheck = psCheck.executeQuery();

            double alreadyPaid = 0;
            if (rsCheck.next()) {
                alreadyPaid = rsCheck.getDouble("total_paid");
            }

            if (alreadyPaid >= totalFee) {
                JOptionPane.showMessageDialog(this, "Fee already fully paid!", "Already Paid", JOptionPane.INFORMATION_MESSAGE);
                disablePaymentButton();
                return;
            }

            // Proceed with payment
            String transactionId = "DEMO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String paymentMode = "Online (Demo)";

            String insertQuery = "INSERT INTO fee_payment (rollno, courseid, semid, amount_paid, payment_mode, transaction_id, status) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, 'Success')";
            PreparedStatement ps = c.c.prepareStatement(insertQuery);
            ps.setString(1, rollno);
            ps.setString(2, currentCourseId);
            ps.setString(3, currentSemId);
            ps.setDouble(4, amount);
            ps.setString(5, paymentMode);
            ps.setString(6, transactionId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                    "Payment Successful!\n" +
                    "Amount: ₹ " + String.format("%,.2f", amount) + "\n" +
                    "Transaction ID: " + transactionId + "\n" +
                    "Saved to database successfully.",
                    "Payment Completed",
                    JOptionPane.INFORMATION_MESSAGE);

                // Update UI permanently
                disablePaymentButton();

                // Optional: Refresh status
                loadStudentFeeDetails();
            } else {
                JOptionPane.showMessageDialog(this, "Payment recorded but database insert failed.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void disablePaymentButton() {
        lblStatus.setText("Paid");
        lblStatus.setForeground(new Color(40, 167, 69));
        btnPayNow.setEnabled(false);
        btnPayNow.setText("Paid ✓");
        btnPayNow.setBackground(new Color(200, 200, 200));
    }

    private void loadStudentFeeDetails() {
        try {
            Conn c = new Conn();

            // Student details
            String studentQuery = 
                "SELECT s.class_name, s.semester_name, c.courseid " +
                "FROM student s " +
                "LEFT JOIN class c ON s.class_name = c.coursename " +
                "WHERE s.rollno = ?";
            PreparedStatement ps = c.c.prepareStatement(studentQuery);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            String courseName = "Not Found";
            String semesterName = "Not Found";
            currentCourseId = null;
            currentSemId = null;

            if (rs.next()) {
                courseName = rs.getString("class_name") != null ? rs.getString("class_name").trim() : "N/A";
                semesterName = rs.getString("semester_name") != null ? rs.getString("semester_name").trim() : "N/A";
                currentCourseId = rs.getString("courseid");
            }

            lblCourse.setText(courseName);
            lblSemester.setText(semesterName);

            if (currentCourseId == null || currentCourseId.trim().isEmpty()) {
                lblFeeAmount.setText("Course not mapped");
                lblFeeAmount.setForeground(Color.RED);
                btnPayNow.setEnabled(false);
                return;
            }

            // Get total fee + semid
            String feeQuery = 
                "SELECT fa.total_fee, fa.semid " +
                "FROM fee_assignment fa " +
                "JOIN semester s ON fa.semid = s.semid " +
                "WHERE fa.courseid = ? AND s.sem_name = ? LIMIT 1";
            PreparedStatement psFee = c.c.prepareStatement(feeQuery);
            psFee.setString(1, currentCourseId);
            psFee.setString(2, semesterName);
            ResultSet rsFee = psFee.executeQuery();

            if (rsFee.next()) {
                totalFee = rsFee.getDouble("total_fee");
                currentSemId = rsFee.getString("semid");

                lblFeeAmount.setText(String.format("₹ %,.2f", totalFee));

                // Check if already fully paid
                String paidQuery = "SELECT COALESCE(SUM(amount_paid), 0) AS total_paid " +
                                  "FROM fee_payment WHERE rollno = ? AND courseid = ? AND semid = ?";
                PreparedStatement psPaid = c.c.prepareStatement(paidQuery);
                psPaid.setString(1, rollno);
                psPaid.setString(2, currentCourseId);
                psPaid.setString(3, currentSemId);
                ResultSet rsPaid = psPaid.executeQuery();

                double paidAmount = 0;
                if (rsPaid.next()) {
                    paidAmount = rsPaid.getDouble("total_paid");
                }

                if (paidAmount >= totalFee) {
                    disablePaymentButton();
                    lblStatus.setText("Paid (Full)");
                } else if (paidAmount > 0) {
                    lblStatus.setText("Partially Paid");
                    lblStatus.setForeground(new Color(255, 193, 7));
                    btnPayNow.setEnabled(true);
                } else {
                    lblStatus.setText("Pending");
                    lblStatus.setForeground(new Color(220, 53, 69));
                    btnPayNow.setEnabled(true);
                }
            } else {
                lblFeeAmount.setText("No fee assigned yet");
                lblFeeAmount.setForeground(new Color(255, 193, 7));
                lblStatus.setText("Not Assigned");
                lblStatus.setForeground(new Color(255, 193, 7));
                btnPayNow.setEnabled(false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading fee details:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            lblFeeAmount.setText("Error");
            lblFeeAmount.setForeground(Color.RED);
            btnPayNow.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentFeePayment("15333880"); // your test roll number
        });
    }
}