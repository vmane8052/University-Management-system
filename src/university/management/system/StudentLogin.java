package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentLogin extends JFrame implements ActionListener {

    private JTextField txtRollNo;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;

    public StudentLogin() {
        setTitle("Student Login");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Heading
        JLabel heading = new JLabel("Student Login");
        heading.setBounds(150, 40, 300, 40);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        add(heading);

        // Roll Number
        JLabel lblRoll = new JLabel("Roll Number:");
        lblRoll.setBounds(80, 120, 150, 30);
        lblRoll.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblRoll);

        txtRollNo = new JTextField();
        txtRollNo.setBounds(240, 120, 180, 35);
        txtRollNo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(txtRollNo);

        // Password
        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(80, 180, 150, 30);
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(240, 180, 180, 35);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(txtPassword);

        // Buttons
        btnLogin = new JButton("Login");
        btnLogin.setBounds(140, 260, 120, 45);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(40, 167, 69));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(this);
        add(btnLogin);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(280, 260, 120, 45);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancel.setBackground(new Color(220, 53, 69));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String rollNo = txtRollNo.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (rollNo.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Roll Number and Password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn c = new Conn();
                String query = "SELECT * FROM student WHERE rollno = ? AND password = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, rollNo);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Login successful - open Student Dashboard
                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + rs.getString("name"), "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // close login
                    new StudentDashboard(rollNo); // Open student dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Roll Number or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new StudentLogin();
    }
}