package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TeacherDashboard extends JFrame implements ActionListener {

    private String empId;

    TeacherDashboard(String empId) {
        this.empId = empId;

        setTitle("Teacher Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0, 51, 102)); // Dark blue
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new GridLayout(0, 1, 0, 8)); // Vertical layout with spacing
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] menuItems = {
                "Home", "My Profile", "My Classes", "Add Marks", 
                "Attendance", "Students", "Settings", "Logout"
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

            // Hover effect
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

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 60, 120));
        topPanel.setPreferredSize(new Dimension(0, 70));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 255)));

        JLabel title = new JLabel("  Teacher Management System - Dashboard", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL - Dashboard Cards =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 40, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        centerPanel.setBackground(new Color(240, 240, 240));

        centerPanel.add(createCard("Total Classes", "4", new Color(0, 123, 255)));
        centerPanel.add(createCard("Total Students", "120", new Color(40, 167, 69)));
        centerPanel.add(createCard("Subjects", "6", new Color(255, 193, 7)));
        centerPanel.add(createCard("Pending Work", "3", new Color(220, 53, 69)));

        add(centerPanel, BorderLayout.CENTER);

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

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        // Optional hover effect
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
                    new TeacherLogin();
                }
                break;

            case "My Profile":
                new TeacherProfile(empId);
                break;

            case "My Classes":
                new CourseListPage(); // or your class list page
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

            case "Settings":
                new UploadTeacherPicture(empId);
                break;

            case "Home":
                // Refresh or do something
                JOptionPane.showMessageDialog(this, "Welcome back to Dashboard!");
                break;

            default:
                JOptionPane.showMessageDialog(this, "Feature coming soon: " + text);
                break;
        }
    }

    public static void main(String[] args) {
        new TeacherDashboard("101455"); // test with your teacher empId
    }
}