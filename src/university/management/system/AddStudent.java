package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;

public class AddStudent extends JFrame implements ActionListener {

    private JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfx, tfxii, tfaadhar;
    private JPasswordField tfpassword;
    private JLabel labelrollno;
    private JDateChooser dcdob;
    private JComboBox<String> cbClassName, cbSemester;
    private JButton submit, cancel;

    Random ran = new Random();
    long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);

    public AddStudent() {
        setTitle("Add New Student");
        setSize(900, 720);
        setLocation(350, 30);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Heading
        JLabel heading = new JLabel("New Student Admission");
        heading.setBounds(280, 20, 400, 45);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(new Color(30, 60, 120));
        add(heading);

        // Roll Number (auto-generated)
        addLabel("Roll Number", 50, 100);
        labelrollno = new JLabel("1533" + first4);
        labelrollno.setBounds(200, 100, 200, 30);
        labelrollno.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelrollno.setForeground(new Color(80, 80, 80));
        add(labelrollno);

        // Name
        addLabel("Name *", 50, 140);
        tfname = addTextField(200, 140);

        // Father's Name
        addLabel("Father's Name", 400, 140);
        tffname = addTextField(600, 140);

        // Date of Birth
        addLabel("Date of Birth *", 50, 180);
        dcdob = new JDateChooser();
        dcdob.setBounds(200, 180, 150, 30);
        dcdob.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(dcdob);

        // Address
        addLabel("Address", 400, 180);
        tfaddress = addTextField(600, 180);

        // Phone
        addLabel("Phone *", 50, 220);
        tfphone = addTextField(200, 220);

        // Email
        addLabel("Email", 400, 220);
        tfemail = addTextField(600, 220);

        // Class X %
        addLabel("Class X (%)", 50, 260);
        tfx = addTextField(200, 260);

        // Class XII %
        addLabel("Class XII (%)", 400, 260);
        tfxii = addTextField(600, 260);

        // Aadhar
        addLabel("Aadhar Number *", 50, 300);
        tfaadhar = addTextField(200, 300);

        // Class Name (from class table)
        addLabel("Class Name *", 400, 300);
        cbClassName = new JComboBox<>();
        cbClassName.setBounds(600, 300, 150, 30);
        cbClassName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadClassNames();
        add(cbClassName);

        // Semester (from semester table)
        addLabel("Semester *", 50, 340);
        cbSemester = new JComboBox<>();
        cbSemester.setBounds(200, 340, 150, 30);
        cbSemester.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadSemesters();
        add(cbSemester);

        // Password
        addLabel("Set Password *", 400, 340);
        tfpassword = new JPasswordField();
        tfpassword.setBounds(600, 340, 150, 30);
        tfpassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(tfpassword);

        // Buttons
        submit = new JButton("Submit");
        submit.setBounds(280, 420, 140, 45);
        submit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submit.setBackground(new Color(40, 167, 69));
        submit.setForeground(Color.WHITE);
        submit.setFocusPainted(false);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(480, 420, 140, 45);
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
        field.setBounds(x, y, 150, 30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(field);
        return field;
    }

    private void loadClassNames() {
        try {
            Conn con = new Conn();
            ResultSet rs = con.s.executeQuery("SELECT coursename FROM class ORDER BY coursename");
            while (rs.next()) {
                cbClassName.addItem(rs.getString("coursename"));
            }
            if (cbClassName.getItemCount() == 0) {
                cbClassName.addItem("No classes available");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading class names", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSemesters() {
        try {
            Conn con = new Conn();
            ResultSet rs = con.s.executeQuery("SELECT sem_name FROM semester ORDER BY sem_name");
            while (rs.next()) {
                cbSemester.addItem(rs.getString("sem_name"));
            }
            if (cbSemester.getItemCount() == 0) {
                cbSemester.addItem("No semesters available");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading semesters", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            // Basic validation
            if (tfname.getText().trim().isEmpty() ||
                tfphone.getText().trim().isEmpty() ||
                tfaadhar.getText().trim().isEmpty() ||
                new String(tfpassword.getPassword()).trim().isEmpty() ||
                dcdob.getDate() == null ||
                cbClassName.getSelectedItem() == null ||
                cbSemester.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this, "Please fill all required fields (*)", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                java.sql.Date sqlDob = new java.sql.Date(dcdob.getDate().getTime());
                String password = new String(tfpassword.getPassword()).trim();

                String query = "INSERT INTO student (name, fname, rollno, dob, address, phone, email, " +
                               "class_x, class_xii, aadhar, course, branch, class_name, semester_name, password) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                Conn con = new Conn();
                PreparedStatement ps = con.c.prepareStatement(query);

                ps.setString(1, tfname.getText().trim());
                ps.setString(2, tffname.getText().trim());
                ps.setString(3, labelrollno.getText());
                ps.setDate(4, sqlDob);
                ps.setString(5, tfaddress.getText().trim());
                ps.setString(6, tfphone.getText().trim());
                ps.setString(7, tfemail.getText().trim());
                ps.setString(8, tfx.getText().trim());
                ps.setString(9, tfxii.getText().trim());
                ps.setString(10, tfaadhar.getText().trim());

                // If you still want to save course & branch (even though not shown in form)
                // You can set them to empty or remove these columns if not needed
                ps.setString(11, "");   // course — removed from form
                ps.setString(12, "");   // branch — removed from form

                ps.setString(13, (String) cbClassName.getSelectedItem());
                ps.setString(14, (String) cbSemester.getSelectedItem());
                ps.setString(15, password);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student Added Successfully!\nRoll No: " + labelrollno.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
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
        new AddStudent();
    }
}