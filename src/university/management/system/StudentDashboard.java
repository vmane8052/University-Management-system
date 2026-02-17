 //import javax.swing.*;
//import java.awt.*;
//
//public class StudentDashboard extends JFrame {
//
//    StudentDashboard() {
//
//        setTitle("Student Dashboard");
//
//        // FULL SCREEN
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        setLayout(new BorderLayout());
//
//        // ===== SIDEBAR =====
//        JPanel sidebar = new JPanel();
//        sidebar.setBackground(new Color(33, 37, 41));
//        sidebar.setPreferredSize(new Dimension(250, getHeight()));
//        sidebar.setLayout(new GridLayout(9,1,0,5));
//
//        String menu[] = {
//                "Home",
//                "My Profile",
//                "My Courses",
//                "Attendance",
//                "Marks",
//                "Fees Status",
//                "Feedback",
//                "Settings",
//                "Logout"
//        };
//
//        for(String item : menu){
//            JButton btn = new JButton(item);
//            btn.setFocusPainted(false);
//            btn.setBackground(new Color(52,58,64));
//            btn.setForeground(Color.WHITE);
//            btn.setFont(new Font("Arial", Font.BOLD, 16));
//            sidebar.add(btn);
//        }
//
//        add(sidebar, BorderLayout.WEST);
//
//        // ===== TOP PANEL =====
//        JPanel topPanel = new JPanel();
//        topPanel.setBackground(Color.WHITE);
//        topPanel.setPreferredSize(new Dimension(100, 70));
//        topPanel.setLayout(new BorderLayout());
//
//        JLabel title = new JLabel("   Student Management System - Student Dashboard");
//        title.setFont(new Font("Arial", Font.BOLD, 22));
//
//        topPanel.add(title, BorderLayout.WEST);
//
//        add(topPanel, BorderLayout.NORTH);
//
//        // ===== CENTER PANEL =====
//        JPanel centerPanel = new JPanel();
//        centerPanel.setLayout(new GridLayout(2,2,30,30));
//        centerPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
//        centerPanel.setBackground(new Color(240,240,240));
//
//        centerPanel.add(createCard("Total Subjects", "6", new Color(0,123,255)));
//        centerPanel.add(createCard("Attendance", "87%", new Color(40,167,69)));
//        centerPanel.add(createCard("Pending Fees", "₹ 15,000", new Color(255,193,7)));
//        centerPanel.add(createCard("Total Credits", "24", new Color(220,53,69)));
//
//        add(centerPanel, BorderLayout.CENTER);
//
//        setVisible(true);
//    }
//
//    JPanel createCard(String title, String value, Color color){
//
//        JPanel panel = new JPanel();
//        panel.setBackground(color);
//        panel.setLayout(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
//
//        JLabel lblTitle = new JLabel(title);
//        lblTitle.setForeground(Color.WHITE);
//        lblTitle.setFont(new Font("Arial", Font.PLAIN, 18));
//
//        JLabel lblValue = new JLabel(value);
//        lblValue.setForeground(Color.WHITE);
//        lblValue.setFont(new Font("Arial", Font.BOLD, 40));
//
//        panel.add(lblTitle, BorderLayout.NORTH);
//        panel.add(lblValue, BorderLayout.CENTER);
//
//        return panel;
//    }
//
//    public static void main(String[] args) {
//        new StudentDashboard();
//    }
//}
package university.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame implements ActionListener {

    private String studentEmail;

    StudentDashboard(String email) {
        this.studentEmail = email;

        setTitle("Student Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new GridLayout(9, 1, 0, 5));

        String menu[] = {
                "Home", "My Profile", "My Courses", "Attendance",
                "Marks", "Fees Status", "Feedback", "Settings", "Logout"
        };

        for (String item : menu) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(52, 58, 64));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.addActionListener(this);          // ← Added listener
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(100, 70));
        topPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel(" Student Management System - Student Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(title, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        centerPanel.setBackground(new Color(240, 240, 240));

        centerPanel.add(createCard("Total Subjects", "6", new Color(0, 123, 255)));
        centerPanel.add(createCard("Attendance", "87%", new Color(40, 167, 69)));
        centerPanel.add(createCard("Pending Fees", "₹ 15,000", new Color(255, 193, 7)));
        centerPanel.add(createCard("Total Credits", "24", new Color(220, 53, 69)));

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    JPanel createCard(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Arial", Font.BOLD, 40));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        String text = source.getText();

        if (text.equals("My Profile")) {
            new MyProfile(studentEmail);
        }
        else if (text.equals("Settings")) {
              new UploadPicture(studentEmail);
         }
        else if (text.equals("My Courses")) {
              new MyCourses(studentEmail);
         }
        // You can add more conditions later for other buttons
        // else if (text.equals("Attendance")) { ... }
        // else if (text.equals("Logout")) { setVisible(false); new StudentLogin(); }
    }

     public static void main(String[] args) {
        new StudentDashboard("test@example.com"); // for testing
     }
}