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

        // 🔵 CHANGED SIDEBAR COLOR (Blue Theme)
        sidebar.setBackground(new Color(0, 51, 102));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new GridLayout(8, 1, 0, 5));

        String menu[] = {
                "Home",
                "My Profile",
                "My Classes",
                "Add Marks",
                "Attendance",
                "Students",
                "Settings",
                "Logout"
        };

        for (String item : menu) {
            JButton btn = new JButton(item);
            btn.setFocusPainted(false);

            // 🔵 Button Color Changed
            btn.setBackground(new Color(0, 76, 153));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.addActionListener(this);
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(100, 70));
        topPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel(" Teacher Management System - Teacher Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(title, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        centerPanel.setBackground(new Color(240, 240, 240));

        centerPanel.add(createCard("Total Classes", "4", new Color(0, 123, 255)));
        centerPanel.add(createCard("Total Students", "120", new Color(40, 167, 69)));
        centerPanel.add(createCard("Subjects", "6", new Color(255, 193, 7)));
        centerPanel.add(createCard("Pending Work", "3", new Color(220, 53, 69)));

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

        if (text.equals("Logout")) {
            setVisible(false);
            new TeacherLogin();
        }
        if(text.equals("My Profile")) {
           new TeacherProfile(empId);
        }
        
        if (text.equals("Settings")) {
          new UploadTeacherPicture(empId);
          
}        if (text.equals("Students")) {
          new CourseListPage();
}
           if (text.equals("Add Marks")) {
          new AddMarksPage(empId);
}
        // You can add more actions later
        // if(text.equals("My Profile")) { new TeacherProfile(empId); }
    }

    public static void main(String[] args) {
        new TeacherDashboard("T101");
    }
}