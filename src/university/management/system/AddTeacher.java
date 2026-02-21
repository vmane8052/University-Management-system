package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;

public class AddTeacher extends JFrame implements ActionListener {

    private JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfx, tfxii, tfaadhar;
    private JPasswordField tfPassword;
    private JLabel labelempId;
    private JDateChooser dcdob;
    private JComboBox<String> cbeducation, cbdepartment, cbClassTeacher, cbRole;
    private JButton submit, cancel;

    Random ran = new Random();
    long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);

    public AddTeacher() {
        setTitle("Add New Teacher");
        setSize(920, 740);
        setLocation(340, 30);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Heading
        JLabel heading = new JLabel("Teacher Registration");
        heading.setBounds(280, 20, 400, 45);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));
        heading.setForeground(new Color(30, 60, 120));
        add(heading);

        // Name
        addLabel("Name *", 50, 100);
        tfname = addTextField(220, 100);

        // Father's Name
        addLabel("Father's Name", 450, 100);
        tffname = addTextField(620, 100);

        // Employee ID (auto-generated)
        addLabel("Employee ID", 50, 140);
        labelempId = new JLabel("101" + first4);
        labelempId.setBounds(220, 140, 200, 30);
        labelempId.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelempId.setForeground(new Color(80, 80, 80));
        add(labelempId);

        // Date of Birth
        addLabel("Date of Birth *", 450, 140);
        dcdob = new JDateChooser();
        dcdob.setBounds(620, 140, 150, 30);
        dcdob.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(dcdob);

        // Address
        addLabel("Address", 50, 180);
        tfaddress = addTextField(220, 180);

        // Phone
        addLabel("Phone *", 450, 180);
        tfphone = addTextField(620, 180);

        // Email
        addLabel("Email", 50, 220);
        tfemail = addTextField(220, 220);

        // Password
        addLabel("Password *", 450, 220);
        tfPassword = new JPasswordField();
        tfPassword.setBounds(620, 220, 150, 30);
        tfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(tfPassword);

        // 10th %
        addLabel("10th (%)", 50, 260);
        tfx = addTextField(220, 260);

        // 12th %
        addLabel("12th (%)", 450, 260);
        tfxii = addTextField(620, 260);

        // Aadhar
        addLabel("Aadhar Number *", 50, 300);
        tfaadhar = addTextField(220, 300);

        // Qualification / Education
        addLabel("Qualification", 450, 300);
        String education[] = {"B.Tech", "B.E.", "M.Tech", "M.E.", "MSc", "MBA", "MCA", "PhD", "Others"};
        cbeducation = new JComboBox<>(education);
        cbeducation.setBounds(620, 300, 150, 30);
        add(cbeducation);

        // Department
        addLabel("Department *", 50, 340);
        String dept[] = {"Computer Science", "Information Technology", "Electronics", "Mechanical", "Civil", "Electrical", "AI & DS", "Others"};
        cbdepartment = new JComboBox<>(dept);
        cbdepartment.setBounds(220, 340, 180, 30);
        add(cbdepartment);

        // Class Teacher (loaded from DB - same as student class_name)
        addLabel("Class Teacher Of", 450, 340);
        cbClassTeacher = new JComboBox<>();
        cbClassTeacher.setBounds(620, 340, 180, 30);
        loadClassNames();   // ← dynamic from database
        add(cbClassTeacher);

        // Role
        addLabel("Role *", 50, 380);
        String roles[] = {"Professor", "Associate Professor", "Assistant Professor", "Lab Instructor", "HOD", "Principal", "Dean"};
        cbRole = new JComboBox<>(roles);
        cbRole.setBounds(220, 380, 180, 30);
        add(cbRole);

        // Buttons
        submit = new JButton("Add Teacher");
        submit.setBounds(280, 460, 160, 45);
        submit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submit.setBackground(new Color(40, 167, 69));
        submit.setForeground(Color.WHITE);
        submit.setFocusPainted(false);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(480, 460, 160, 45);
        cancel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancel.setBackground(new Color(220, 53, 69));
        cancel.setForeground(Color.WHITE);
        cancel.setFocusPainted(false);
        cancel.addActionListener(this);
        add(cancel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(label);
    }

    private JTextField addTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 180, 30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(field);
        return field;
    }

    private void loadClassNames() {
        try {
            Conn con = new Conn();
            ResultSet rs = con.s.executeQuery("SELECT coursename FROM class ORDER BY coursename");
            while (rs.next()) {
                cbClassTeacher.addItem(rs.getString("coursename"));
            }
            if (cbClassTeacher.getItemCount() == 0) {
                cbClassTeacher.addItem("No classes available");
                cbClassTeacher.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading class names from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            // Basic validation
            if (tfname.getText().trim().isEmpty() ||
                tfPassword.getPassword().length == 0 ||
                tfphone.getText().trim().isEmpty() ||
                tfaadhar.getText().trim().isEmpty() ||
                dcdob.getDate() == null ||
                cbdepartment.getSelectedItem() == null ||
                cbRole.getSelectedItem() == null ||
                cbClassTeacher.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields (*)", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                java.sql.Date sqlDob = new java.sql.Date(dcdob.getDate().getTime());
                String password = new String(tfPassword.getPassword()).trim();

                String query = "INSERT INTO teacher " +
                               "(name, fname, empId, dob, address, phone, email, password, " +
                               "class_x, class_xii, aadhar, education, department, class_teacher, role) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                Conn con = new Conn();
                PreparedStatement ps = con.c.prepareStatement(query);

                ps.setString(1, tfname.getText().trim());
                ps.setString(2, tffname.getText().trim());
                ps.setString(3, labelempId.getText());
                ps.setDate(4, sqlDob);
                ps.setString(5, tfaddress.getText().trim());
                ps.setString(6, tfphone.getText().trim());
                ps.setString(7, tfemail.getText().trim());
                ps.setString(8, password);
                ps.setString(9, tfx.getText().trim());
                ps.setString(10, tfxii.getText().trim());
                ps.setString(11, tfaadhar.getText().trim());
                ps.setString(12, (String) cbeducation.getSelectedItem());
                ps.setString(13, (String) cbdepartment.getSelectedItem());
                ps.setString(14, (String) cbClassTeacher.getSelectedItem());
                ps.setString(15, (String) cbRole.getSelectedItem());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, 
                    "Teacher Added Successfully!\nEmployee ID: " + labelempId.getText(), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                dispose();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            dispose();
        }
    }

    public static void main(String[] args) {
        new AddTeacher();
    }
}