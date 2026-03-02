//package university.management.system;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class StudentDashboard extends JFrame implements ActionListener {
//
//    private String rollno;
//
//    StudentDashboard(String rollno) {
//        this.rollno = rollno;
//
//        setTitle("Student Dashboard - " + rollno);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        // ===== SIDEBAR =====
//        JPanel sidebar = new JPanel();
//        sidebar.setBackground(new Color(33, 37, 41));
//        sidebar.setPreferredSize(new Dimension(250, 0));
//        sidebar.setLayout(new GridLayout(0, 1, 0, 8));
//        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
//
//        String[] menu = {
//            "Home",
//            "My Profile",
//            "My Courses",
//            "Attendance",
//            "Marks",
//            "Apply Leave",
//            "My Leaves",      // ← New: My Leaves
//            "Fees & Payment",
//            "My Receipts",       // ← New: Feedback
//            "Settings",
//            "Logout"
//        };
//
//        for (String item : menu) {
//            JButton btn = new JButton(item);
//            btn.setFocusPainted(false);
//            btn.setBackground(new Color(52, 58, 64));
//            btn.setForeground(Color.WHITE);
//            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
//            btn.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
//            btn.setHorizontalAlignment(SwingConstants.LEFT);
//            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//            // Hover effect
//            btn.addMouseListener(new MouseAdapter() {
//                public void mouseEntered(MouseEvent e) {
//                    btn.setBackground(new Color(66, 73, 80));
//                }
//                public void mouseExited(MouseEvent e) {
//                    btn.setBackground(new Color(52, 58, 64));
//                }
//            });
//
//            btn.addActionListener(this);
//            sidebar.add(btn);
//        }
//
//        add(sidebar, BorderLayout.WEST);
//
//        // ===== TOP HEADER =====
//        JPanel topPanel = new JPanel();
//        topPanel.setBackground(new Color(30, 60, 120));
//        topPanel.setPreferredSize(new Dimension(0, 80));
//        topPanel.setLayout(new BorderLayout());
//        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 255)));
//
//        JLabel title = new JLabel("  Student Management System - Dashboard", SwingConstants.LEFT);
//        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
//        title.setForeground(Color.WHITE);
//        topPanel.add(title, BorderLayout.WEST);
//
//        JLabel welcome = new JLabel("Welcome, Roll No: " + rollno + "   ", SwingConstants.RIGHT);
//        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
//        welcome.setForeground(Color.WHITE);
//        topPanel.add(welcome, BorderLayout.EAST);
//
//        add(topPanel, BorderLayout.NORTH);
//
//        // ===== CENTER DASHBOARD CARDS =====
//        JPanel centerPanel = new JPanel();
//        centerPanel.setLayout(new GridLayout(2, 2, 40, 40));
//        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
//        centerPanel.setBackground(new Color(240, 240, 240));
//
//        centerPanel.add(createCard("Total Subjects", "6", new Color(0, 123, 255)));
//        centerPanel.add(createCard("Attendance", "87%", new Color(40, 167, 69)));
//        centerPanel.add(createCard("Pending Fees", "₹ 15,000", new Color(255, 193, 7)));
//        centerPanel.add(createCard("Total Credits", "24", new Color(220, 53, 69)));
//
//        add(centerPanel, BorderLayout.CENTER);
//
//        setVisible(true);
//    }
//
//    private JPanel createCard(String title, String value, Color color) {
//        JPanel panel = new JPanel();
//        panel.setBackground(color);
//        panel.setLayout(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
//        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//        JLabel lblTitle = new JLabel(title);
//        lblTitle.setForeground(Color.WHITE);
//        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
//
//        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
//        lblValue.setForeground(Color.WHITE);
//        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
//
//        panel.add(lblTitle, BorderLayout.NORTH);
//        panel.add(lblValue, BorderLayout.CENTER);
//
//        panel.addMouseListener(new MouseAdapter() {
//            public void mouseEntered(MouseEvent e) {
//                panel.setBackground(color.darker());
//            }
//            public void mouseExited(MouseEvent e) {
//                panel.setBackground(color);
//            }
//        });
//
//        return panel;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        JButton source = (JButton) e.getSource();
//        String text = source.getText();
//
//        switch (text) {
//            case "Logout":
//                int confirm = JOptionPane.showConfirmDialog(this,
//                        "Are you sure you want to logout?",
//                        "Logout Confirmation",
//                        JOptionPane.YES_NO_OPTION);
//                if (confirm == JOptionPane.YES_OPTION) {
//                    dispose();
//                    new StudentLogin();
//                }
//                break;
//
//            case "My Profile":
//                new MyProfile(rollno);
//                break;
//
//            case "My Courses":
//                new MyCourses(rollno);
//                break;
//
//            case "Attendance":
//                new StudentAttendanceCalendar(rollno);
//                break;
//
//            case "Marks":
//                new StudentMarksView(rollno);
//                break;
//
//            case "My Receipts":
//                new StudentReceipts(rollno);
//                break;
//
//            case "Apply Leave":
//                new ApplyLeavePage(rollno);
//                break;
//
//            case "My Leaves":  // ← New: My Leaves
//                new MyLeavesPage(rollno);
//                break;
//
//            case "Fees & Payment":
//                new StudentFeePayment(rollno);
//                break;
//
//            case "Settings":
//                new UploadPicture(rollno);
//                break;
//
//            case "Home":
//                JOptionPane.showMessageDialog(this, "You are already on Home!", "Home", JOptionPane.INFORMATION_MESSAGE);
//                break;
//
//            default:
//                JOptionPane.showMessageDialog(this, "Feature coming soon: " + text);
//                break;
//        }
//    }
//
//    public static void main(String[] args) {
//        new StudentDashboard("15333880");
//    }
//}


package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentDashboard extends JFrame implements ActionListener {

    private String rollno;

    private JLabel lblSubjects;
    private JLabel lblAttendance;
    private JLabel lblFees;

    StudentDashboard(String rollno) {
        this.rollno = rollno.trim();

        setTitle("Student Dashboard - " + rollno);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new GridLayout(0, 1, 0, 8));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        String[] menu = {
                "Home", "My Profile", "My Courses", "Attendance",
                "Marks", "Apply Leave", "My Leaves",
                "Fees & Payment", "My Receipts",
                "Settings", "Logout"
        };

        for (String item : menu) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(52, 58, 64));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.addActionListener(this);
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 60, 120));
        topPanel.setPreferredSize(new Dimension(0, 80));

        JLabel title = new JLabel("  Student Management System - Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome, Roll No: " + rollno + "   ");
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcome.setForeground(Color.WHITE);
        welcome.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(welcome, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER CARDS =====
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 40, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        centerPanel.setBackground(new Color(240, 240, 240));

        lblSubjects = createCard("Total Subjects", "0", new Color(0, 123, 255));
        lblAttendance = createCard("Attendance", "0%", new Color(40, 167, 69));
        lblFees = createCard("Pending Fees", "₹ 0", new Color(255, 193, 7));
        createCard("Total Credits", "24", new Color(220, 53, 69));

        centerPanel.add(lblSubjects.getParent());
        centerPanel.add(lblAttendance.getParent());
        centerPanel.add(lblFees.getParent());
        centerPanel.add(createCard("Total Credits", "24", new Color(220, 53, 69)).getParent());

        add(centerPanel, BorderLayout.CENTER);

        loadDashboardData();

        setVisible(true);
    }

    private JLabel createCard(String title, String value, Color color) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return lblValue;
    }

    private void loadDashboardData() {

        try {
            Conn c = new Conn();

            // ===== STEP 1: Get class & semester =====
            String studentQuery = "SELECT class_name, semester_name FROM student WHERE rollno = ?";
            PreparedStatement ps1 = c.c.prepareStatement(studentQuery);
            ps1.setString(1, rollno);
            ResultSet rs1 = ps1.executeQuery();

            String className = null;
            String semesterName = null;

            if (rs1.next()) {
                className = rs1.getString("class_name");
                semesterName = rs1.getString("semester_name");
            }

            if (className == null || semesterName == null) return;

            // ===== STEP 2: COUNT SUBJECTS =====
            String subQuery =
                    "SELECT COUNT(*) FROM subject1 " +
                    "WHERE REPLACE(UPPER(TRIM(course_id)),' ','') = REPLACE(UPPER(TRIM(?)),' ','') " +
                    "AND REPLACE(UPPER(TRIM(sem_id)),' ','') = REPLACE(UPPER(TRIM(?)),' ', '')";

            PreparedStatement ps2 = c.c.prepareStatement(subQuery);
            ps2.setString(1, className);
            ps2.setString(2, semesterName);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                lblSubjects.setText(String.valueOf(rs2.getInt(1)));
            }

            // ===== STEP 3: ATTENDANCE =====
            String attQuery = "SELECT status FROM attendance WHERE rollno = ?";
            PreparedStatement ps3 = c.c.prepareStatement(attQuery);
            ps3.setString(1, rollno);
            ResultSet rs3 = ps3.executeQuery();

            int present = 0, total = 0;

            while (rs3.next()) {
                total++;
                String status = rs3.getString("status");
                if (status != null && status.toLowerCase().contains("pre"))
                    present++;
            }

            if (total > 0) {
                double percent = (double) present / total * 100;
                lblAttendance.setText(String.format("%.1f %%", percent));
            }

            // ===== STEP 4: PENDING FEES =====
String feeQuery =
        "SELECT fa.total_fee, COALESCE(SUM(fp.amount_paid),0) as paid " +
        "FROM student s " +
        "JOIN class c1 ON s.class_name = c1.coursename " +
        "JOIN fee_assignment fa ON c1.courseid = fa.courseid " +
        "JOIN semester sem ON fa.semid = sem.semid " +
        "LEFT JOIN fee_payment fp ON fp.rollno = s.rollno " +
        "AND fp.courseid = fa.courseid " +
        "AND fp.semid = fa.semid " +
        "WHERE s.rollno = ? " +
        "AND sem.sem_name = s.semester_name " +
        "GROUP BY fa.total_fee";

PreparedStatement ps4 = c.c.prepareStatement(feeQuery);
ps4.setString(1, rollno);
ResultSet rs4 = ps4.executeQuery();

if (rs4.next()) {

    double totalFee = rs4.getDouble("total_fee");
    double paid = rs4.getDouble("paid");

    double pending = totalFee - paid;

    // 🔥 IMPORTANT LOGIC
    if (pending <= 0) {
        lblFees.setText("₹ 0");
    } else {
        lblFees.setText("₹ " + String.format("%,.0f", pending));
    }

} else {
    lblFees.setText("₹ 0");
}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
   
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        String text = source.getText();

        switch (text) {
            case "Logout":
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to logout?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new StudentLogin();
                }
                break;

            case "My Profile":
                new MyProfile(rollno);
                break;

            case "My Courses":
                new MyCourses(rollno);
                break;

            case "Attendance":
                new StudentAttendanceCalendar(rollno);
                break;

            case "Marks":
                new StudentMarksView(rollno);
                break;

            case "My Receipts":
                new StudentReceipts(rollno);
                break;

            case "Apply Leave":
                new ApplyLeavePage(rollno);
                break;

            case "My Leaves":  // ← New: My Leaves
                new MyLeavesPage(rollno);
                break;

            case "Fees & Payment":
                new StudentFeePayment(rollno);
                break;

            case "Settings":
                new UploadPicture(rollno);
                break;

            case "Home":
                JOptionPane.showMessageDialog(this, "You are already on Home!", "Home", JOptionPane.INFORMATION_MESSAGE);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Feature coming soon: " + text);
                break;
        }
    }

    public static void main(String[] args) {
        new StudentDashboard("15333880");
    }
}
