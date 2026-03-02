package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class AdminFeeAssignment extends JFrame implements ActionListener {

    private JComboBox<CourseItem> cbCourse;
    private JComboBox<SemesterItem> cbSemester;
    private JTextField tfTotalFee;
    private JLabel lblAssignedDate;
    private JButton btnAssign, btnClear, btnViewAssigned;

    // Custom item classes
    private static class CourseItem {
        String courseId;
        String courseName;

        CourseItem(String id, String name) {
            this.courseId = id;
            this.courseName = name;
        }

        @Override
        public String toString() {
            return courseName;
        }
    }

    private static class SemesterItem {
        String semId;
        String semName;

        SemesterItem(String id, String name) {
            this.semId = id;
            this.semName = name;
        }

        @Override
        public String toString() {
            return semName;
        }
    }

    public AdminFeeAssignment() {
        setTitle("Admin - Assign Fee (Course + Semester Wise)");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("Assign Fee Structure to Course & Semester", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Fee Assignment Form"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Course
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Select Course:"), gbc);

        cbCourse = new JComboBox<>();
        cbCourse.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(cbCourse, gbc);

        // Semester
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Select Semester:"), gbc);

        cbSemester = new JComboBox<>();
        cbSemester.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(cbSemester, gbc);

        // Total Fee Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Total Fee Amount (₹):"), gbc);

        tfTotalFee = new JTextField();
        tfTotalFee.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        formPanel.add(tfTotalFee, gbc);

        // Assigned Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Assigned Date:"), gbc);

        lblAssignedDate = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        lblAssignedDate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 1;
        formPanel.add(lblAssignedDate, gbc);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnAssign = new JButton("Assign Fee");
        btnAssign.setBackground(new Color(40, 167, 69));
        btnAssign.setForeground(Color.WHITE);
        btnAssign.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAssign.addActionListener(this);
        btnPanel.add(btnAssign);

        btnClear = new JButton("Clear");
        btnClear.setBackground(new Color(255, 193, 7));
        btnClear.setForeground(Color.BLACK);
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClear.addActionListener(this);
        btnPanel.add(btnClear);

        btnViewAssigned = new JButton("View Assigned Fees");
        btnViewAssigned.setBackground(new Color(0, 123, 255));
        btnViewAssigned.setForeground(Color.WHITE);
        btnViewAssigned.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnViewAssigned.addActionListener(this);
        btnPanel.add(btnViewAssigned);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Load data
        loadCourses();
        loadSemesters();

        setVisible(true);
    }

    private void loadCourses() {
        cbCourse.removeAllItems();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT courseid, coursename FROM class ORDER BY coursename");
            while (rs.next()) {
                cbCourse.addItem(new CourseItem(rs.getString("courseid"), rs.getString("coursename")));
            }
            if (cbCourse.getItemCount() == 0) {
                cbCourse.addItem(new CourseItem("", "No courses available"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading courses", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSemesters() {
        cbSemester.removeAllItems();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT semid, sem_name FROM semester ORDER BY sem_name");
            while (rs.next()) {
                cbSemester.addItem(new SemesterItem(rs.getString("semid"), rs.getString("sem_name")));
            }
            if (cbSemester.getItemCount() == 0) {
                cbSemester.addItem(new SemesterItem("", "No semesters available"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading semesters", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAssign) {
            CourseItem courseItem = (CourseItem) cbCourse.getSelectedItem();
            SemesterItem semItem = (SemesterItem) cbSemester.getSelectedItem();
            String feeStr = tfTotalFee.getText().trim();

            if (courseItem == null || courseItem.courseId.isEmpty() ||
                semItem == null || semItem.semId.isEmpty() || feeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double totalFee = Double.parseDouble(feeStr);

                Conn c = new Conn();

                // Check if already assigned
                String checkQuery = "SELECT * FROM fee_assignment WHERE courseid = ? AND semid = ?";
                PreparedStatement psCheck = c.c.prepareStatement(checkQuery);
                psCheck.setString(1, courseItem.courseId);
                psCheck.setString(2, semItem.semId);
                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Fee already assigned for this course and semester!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert new fee assignment
                String insertQuery = "INSERT INTO fee_assignment (courseid, semid, total_fee, assigned_date) VALUES (?, ?, ?, CURDATE())";
                PreparedStatement ps = c.c.prepareStatement(insertQuery);
                ps.setString(1, courseItem.courseId);
                ps.setString(2, semItem.semId);
                ps.setDouble(3, totalFee);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Fee Assigned Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Fee amount must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error assigning fee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnClear) {
            clearForm();
        } else if (e.getSource() == btnViewAssigned) {
            new ViewAssignedFees();
        }
    }

    private void clearForm() {
        tfTotalFee.setText("");
        cbCourse.setSelectedIndex(0);
        cbSemester.setSelectedIndex(0);
    }

    // View Assigned Fees Window
    class ViewAssignedFees extends JFrame {
        JTable table;
        DefaultTableModel model;

        public ViewAssignedFees() {
            setTitle("Assigned Fee List");
            setSize(900, 600);
            setLocationRelativeTo(null);

            String[] columns = {"Course Name", "Semester Name", "Total Fee (₹)", "Assigned Date"};
            model = new DefaultTableModel(columns, 0);
            table = new JTable(model);
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
            table.getTableHeader().setBackground(new Color(30, 60, 120));
            table.getTableHeader().setForeground(Color.WHITE);

            loadAssignedFees();

            add(new JScrollPane(table));
            setVisible(true);
        }

        private void loadAssignedFees() {
            model.setRowCount(0);
            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(
                    "SELECT fa.courseid, fa.semid, fa.total_fee, fa.assigned_date, " +
                    "c.coursename, s.sem_name " +
                    "FROM fee_assignment fa " +
                    "JOIN class c ON fa.courseid = c.courseid " +
                    "JOIN semester s ON fa.semid = s.semid " +
                    "ORDER BY fa.assigned_date DESC"
                );
                while (rs.next()) {
                    // Use Object[] instead of Vector<String> to avoid type issues
                    Object[] row = new Object[4];
                    row[0] = rs.getString("coursename");
                    row[1] = rs.getString("sem_name");
                    row[2] = String.format("%,.2f", rs.getDouble("total_fee"));
                    row[3] = rs.getDate("assigned_date").toString();
                    model.addRow(row);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading assigned fees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new AdminFeeAssignment();
    }
}