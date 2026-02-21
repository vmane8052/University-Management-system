package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame implements ActionListener {

    private String rollno;  // ← Changed from email to rollno

    StudentDashboard(String rollno) {
        this.rollno = rollno;

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
            "Home",
            "My Profile",
            "My Courses",
            "Attendance",
            "Marks",
            "Fees Status",
            "Feedback",
            "Settings",
            "Logout"
        };

        for (String item : menu) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(52, 58, 64));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hover effect
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(66, 73, 80));
                }
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(52, 58, 64));
                }
            });

            btn.addActionListener(this);
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        // ===== TOP HEADER =====
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(30, 60, 120));
        topPanel.setPreferredSize(new Dimension(0, 80));
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 255)));

        JLabel title = new JLabel("  Student Management System - Dashboard", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        topPanel.add(title, BorderLayout.WEST);

        JLabel welcome = new JLabel("Welcome, Roll No: " + rollno + "   ", SwingConstants.RIGHT);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcome.setForeground(Color.WHITE);
        topPanel.add(welcome, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER DASHBOARD CARDS =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 40, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        centerPanel.setBackground(new Color(240, 240, 240));

        centerPanel.add(createCard("Total Subjects", "6", new Color(0, 123, 255)));
        centerPanel.add(createCard("Attendance", "87%", new Color(40, 167, 69)));
        centerPanel.add(createCard("Pending Fees", "₹ 15,000", new Color(255, 193, 7)));
        centerPanel.add(createCard("Total Credits", "24", new Color(220, 53, 69)));

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

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        // Hover effect on card
        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
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
                    new StudentLogin();
                }
                break;

            case "My Profile":
                new MyProfile(rollno);      // Pass rollno
                break;

            case "My Courses":
                new MyCourses(rollno);           // Pass rollno
                break;

//            case "Attendance":
//                new StudentAttendance(rollno);   // Pass rollno
//                break;
//
///           case "Marks":
//                new StudentMarks(rollno);        // Pass rollno
//                break;
////
//            case "Fees Status":
//                new FeesStatus(rollno);          // Pass rollno
//                break;
//
//            case "Feedback":
//                new FeedbackForm(rollno);        // Pass rollno
//                break;
                case "Marks":
    new StudentMarksView(rollno);  // ← Pass rollno here
    break;
    
    case "Attendance":
    new StudentAttendanceCalendar(rollno);
    break;

            case "Settings":
                new UploadPicture(rollno);       // Changed to rollno (assuming student picture upload)
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
        // For testing - normally called from login after successful authentication
        new StudentDashboard("15333936");
    }
}