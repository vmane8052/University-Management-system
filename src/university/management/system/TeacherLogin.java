package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TeacherLogin extends JFrame implements ActionListener {

    private JTextField tfEmpId;
    private JPasswordField tfPassword;
    private JButton btnLogin, btnCancel;

    public TeacherLogin() {
        setTitle("Teacher Login");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Heading
        JLabel heading = new JLabel("Teacher Login");
        heading.setBounds(150, 40, 300, 40);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        add(heading);

        // Emp ID Label & Field
        JLabel lblEmpId = new JLabel("Employee ID:");
        lblEmpId.setBounds(80, 120, 150, 30);
        lblEmpId.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblEmpId);

        tfEmpId = new JTextField();
        tfEmpId.setBounds(240, 120, 180, 35);
        tfEmpId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(tfEmpId);

        // Password Label & Field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(80, 180, 150, 30);
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblPassword);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(240, 180, 180, 35);
        tfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(tfPassword);

        // Login Button
        btnLogin = new JButton("Login");
        btnLogin.setBounds(140, 260, 120, 45);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(40, 167, 69)); // Green
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(this);
        add(btnLogin);

        // Cancel Button
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(280, 260, 120, 45);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancel.setBackground(new Color(220, 53, 69)); // Red
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String empId = tfEmpId.getText().trim();
            String password = new String(tfPassword.getPassword()).trim();

            if (empId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Employee ID and Password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn c = new Conn();
                String query = "SELECT * FROM teacher WHERE empId = ? AND password = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, empId);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + rs.getString("name"), "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new TeacherDashboard(empId);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Employee ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new TeacherLogin();
    }
}