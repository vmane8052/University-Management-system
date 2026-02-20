package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TeacherLogin extends JFrame implements ActionListener {

    JButton login, cancel;
    JTextField tfempId;
    JPasswordField tfpassword;

    TeacherLogin() {

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblEmpId = new JLabel("Emp ID");
        lblEmpId.setBounds(40, 20, 100, 20);
        add(lblEmpId);

        tfempId = new JTextField();
        tfempId.setBounds(150, 20, 150, 25);
        add(tfempId);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(40, 70, 100, 20);
        add(lblPassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(150, 70, 150, 25);
        add(tfpassword);

        login = new JButton("Login");
        login.setBounds(40, 140, 120, 35);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Tahoma", Font.BOLD, 15));
        login.addActionListener(this);
        add(login);

        cancel = new JButton("Cancel");
        cancel.setBounds(180, 140, 120, 35);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
        cancel.addActionListener(this);
        add(cancel);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(350, 0, 200, 200);
        add(image);

        setSize(600, 300);
        setLocation(500, 250);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == login) {

            String empId = tfempId.getText();
            String password = new String(tfpassword.getPassword());

            String query = "SELECT * FROM teacher WHERE empId='" 
                    + empId + "' AND password='" + password + "'";

            try {
                Conn c = new Conn();
                ResultSet rs = c.s.executeQuery(query);

                if (rs.next()) {
                    setVisible(false);

                    // Pass empId to Teacher Dashboard
                    new TeacherDashboard(empId);

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Emp ID or Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new TeacherLogin();
    }
}