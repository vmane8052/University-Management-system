package university.management.system;

import javax.swing.*;
import java.awt.*;

public class Project1 extends JFrame {

    Project1() {

        setTitle("University Management System");
        setSize(1540, 850);
        setLocation(0, 0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ===== Background Image =====
        ImageIcon bgIcon = new ImageIcon(
                ClassLoader.getSystemResource("icons/third.jpg")
        );

        Image bgImg = bgIcon.getImage().getScaledInstance(1540, 850, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setLayout(null);
        setContentPane(background);

        // ===== Center Title =====
        JLabel title = new JLabel("UNIVERSITY MANAGEMENT SYSTEM");
        title.setBounds(250, 250, 1000, 60);
        title.setFont(new Font("Serif", Font.BOLD, 50));
        title.setForeground(Color.yellow);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(title);

        // ===== Top Right Login Dropdown Button =====
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(1350, 20, 150, 40);
        loginBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        loginBtn.setBackground(new Color(20, 33, 61));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        background.add(loginBtn);

        JPopupMenu loginMenu = new JPopupMenu();

        JMenuItem studentLogin = new JMenuItem("Student Login");
        JMenuItem teacherLogin = new JMenuItem("Teacher Login");
        JMenuItem adminLogin = new JMenuItem("Admin Login");

        loginMenu.add(studentLogin);
        loginMenu.add(teacherLogin);
        loginMenu.add(adminLogin);

        loginBtn.addActionListener(e ->
                loginMenu.show(loginBtn, 0, loginBtn.getHeight())
        );

        studentLogin.addActionListener(e ->
                JOptionPane.showMessageDialog(null, "Student Login Clicked"));

        teacherLogin.addActionListener(e ->
                JOptionPane.showMessageDialog(null, "Teacher Login Clicked"));

        adminLogin.addActionListener(e -> {
                 //new Login();
});

        // ===== Center 3 Login Boxes =====
        JPanel boxPanel = new JPanel();
        boxPanel.setBounds(420, 400, 700, 200);
        boxPanel.setLayout(new GridLayout(1, 3, 40, 0));
        boxPanel.setOpaque(false);
        background.add(boxPanel);

        JButton studentBox = new JButton("STUDENT LOGIN");
        studentBox.setFont(new Font("Tahoma", Font.BOLD, 16));
        studentBox.setBackground(new Color(0, 102, 204));
        studentBox.setForeground(Color.WHITE);
        studentBox.setFocusPainted(false);
        boxPanel.add(studentBox);

        JButton teacherBox = new JButton("TEACHER LOGIN");
        teacherBox.setFont(new Font("Tahoma", Font.BOLD, 16));
        teacherBox.setBackground(new Color(0, 153, 76));
        teacherBox.setForeground(Color.WHITE);
        teacherBox.setFocusPainted(false);
        boxPanel.add(teacherBox);

        JButton adminBox = new JButton("ADMIN LOGIN");
        adminBox.setFont(new Font("Tahoma", Font.BOLD, 16));
        adminBox.setBackground(new Color(204, 0, 0));
        adminBox.setForeground(Color.WHITE);
        adminBox.setFocusPainted(false);
        boxPanel.add(adminBox);

        studentBox.addActionListener(e ->{
                new StudentLogin();
           });
        teacherBox.addActionListener(e ->
                JOptionPane.showMessageDialog(null, "Teacher Login Page"));

       adminBox.addActionListener(e -> {
                new Login();
           });

        setVisible(true);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {}

        new Project1();
    }
}