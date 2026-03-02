package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TeacherDashboard extends JFrame implements ActionListener {

    private String empId;
    private JLabel lblTotalClasses, lblTotalStudents, lblSubjects, lblPendingWork;

    TeacherDashboard(String empId) {
        this.empId = empId;
        setTitle("Teacher Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar (same as before)
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0, 51, 102));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new GridLayout(0, 1, 0, 8));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] menuItems = {
                "Home",
                "My Profile",
                "My Classes",
                "Add Marks",
                "Attendance",
                "Students",
                "Leave Approvals",
                "Student Fee Status",
                "Settings",
                "Logout"
        };

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(0, 76, 153));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(0, 102, 204));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(0, 76, 153));
                }
            });

            btn.addActionListener(this);
            sidebar.add(btn);
        }
        add(sidebar, BorderLayout.WEST);

        // Top Panel (same)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 60, 120));
        topPanel.setPreferredSize(new Dimension(0, 70));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 255)));

        JLabel title = new JLabel(" Teacher Management System - Dashboard", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Center - Cards
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 40, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        centerPanel.setBackground(new Color(240, 240, 240));

        JPanel card1 = createCard("Total Classes", "0", new Color(0, 123, 255));
        lblTotalClasses = (JLabel) card1.getComponent(1);
        centerPanel.add(card1);

        JPanel card2 = createCard("Total Students", "0", new Color(40, 167, 69));
        lblTotalStudents = (JLabel) card2.getComponent(1);
        centerPanel.add(card2);

        JPanel card3 = createCard("Total Subjects", "0", new Color(255, 193, 7));
        lblSubjects = (JLabel) card3.getComponent(1);
        centerPanel.add(card3);

        JPanel card4 = createCard("Pending Work", "0", new Color(220, 53, 69));
        lblPendingWork = (JLabel) card4.getComponent(1);
        centerPanel.add(card4);

        add(centerPanel, BorderLayout.CENTER);

        // Load all dynamic counts
        loadDashboardCounts();

        setVisible(true);
    }

    private JPanel createCard(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(color);
            }
        });

        return panel;
    }

    private void loadDashboardCounts() {
        Conn conn = null;
        try {
            conn = new Conn();

            // 1. Total Classes (from class table)
            String qClasses = "SELECT COUNT(*) AS count FROM class";
            ResultSet rsClasses = conn.c.createStatement().executeQuery(qClasses);
            int totalClasses = 0;
            if (rsClasses.next()) totalClasses = rsClasses.getInt("count");
            lblTotalClasses.setText(String.valueOf(totalClasses));

            // 2. Total Students (from student table)
            String qStudents = "SELECT COUNT(*) AS count FROM student";
            ResultSet rsStudents = conn.c.createStatement().executeQuery(qStudents);
            int totalStudents = 0;
            if (rsStudents.next()) totalStudents = rsStudents.getInt("count");
            lblTotalStudents.setText(String.valueOf(totalStudents));

            // 3. Total Subjects (from subject table)
            String qSubjects = "SELECT COUNT(*) AS count FROM subject";
            ResultSet rsSubjects = conn.c.createStatement().executeQuery(qSubjects);
            int totalSubjects = 0;
            if (rsSubjects.next()) totalSubjects = rsSubjects.getInt("count");
            lblSubjects.setText(String.valueOf(totalSubjects));

            // 4. Pending Work - Pending Leaves (teacher's class only)
            int pendingLeaves = 0;
            String qTeacherClass = "SELECT class_teacher FROM teacher WHERE empId = ?";
            PreparedStatement psTeacher = conn.c.prepareStatement(qTeacherClass);
            psTeacher.setString(1, empId);
            ResultSet rsTeacher = psTeacher.executeQuery();
            String classTeacher = "";
            if (rsTeacher.next()) classTeacher = rsTeacher.getString("class_teacher");

            if (!classTeacher.isEmpty()) {
                String qLeaves = "SELECT COUNT(*) AS pending " +
                                 "FROM leave_application la " +
                                 "JOIN student s ON la.rollno = s.rollno " +
                                 "WHERE s.class_name = ? AND la.status = 'Pending'";
                PreparedStatement psLeaves = conn.c.prepareStatement(qLeaves);
                psLeaves.setString(1, classTeacher);
                ResultSet rsLeaves = psLeaves.executeQuery();
                if (rsLeaves.next()) pendingLeaves = rsLeaves.getInt("pending");
            }
            lblPendingWork.setText(String.valueOf(pendingLeaves));

        } catch (Exception ex) {
            ex.printStackTrace();
            lblTotalClasses.setText("Error");
            lblTotalStudents.setText("Error");
            lblSubjects.setText("Error");
            lblPendingWork.setText("Error");
        } finally {
            if (conn != null) {
                try {
                    if (conn.c != null) conn.c.close();
                } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        String text = source.getText();

        switch (text) {
            case "Logout":
                int confirm = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new TeacherLogin();
                }
                break;
            case "My Profile":
                new TeacherProfile(empId);
                break;
            case "My Classes":
                new CourseListPage();
                break;
            case "Add Marks":
                new AddMarksPage(empId);
                break;
            case "Attendance":
                new AddAttendancePage(empId);
                break;
            case "Students":
                new CourseListPage();
                break;
            case "Leave Approvals":
                new LeaveApprovalPage(empId);
                break;
            case "Student Fee Status":
                new TeacherStudentFeeStatus(empId);
                break;
            case "Settings":
                new UploadTeacherPicture(empId);
                break;
            case "Home":
                JOptionPane.showMessageDialog(this, "Welcome back!");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Coming soon: " + text);
                break;
        }
    }

    public static void main(String[] args) {
        new TeacherDashboard("1011321"); 
    }
}