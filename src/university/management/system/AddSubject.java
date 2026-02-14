package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddSubject extends JFrame implements ActionListener {

    private JTextField tfSubName;
    private JTextField tfIntMin, tfIntMax, tfExtMin, tfExtMax;

    private JComboBox<String> cbTeacher;
    private JComboBox<String> cbSem;
    private JComboBox<String> cbCourse;
    private JComboBox<String> cbType;

    private JButton submit, cancel;

    public AddSubject() {

        setTitle("Add Subject");
        setSize(900, 650);
        setLocation(350, 100);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel heading = new JLabel("New Subject Details");
        heading.setBounds(280, 30, 400, 40);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // Subject Name
        addLabel("Subject Name", 100, 120);
        tfSubName = addTextField(300, 120);

        // Teacher Name Dropdown
        addLabel("Teacher Name", 500, 120);
        cbTeacher = new JComboBox<>();
        cbTeacher.setBounds(700, 120, 150, 30);
        add(cbTeacher);

        // Semester Dropdown
        addLabel("Semester ID", 100, 180);
        cbSem = new JComboBox<>();
        cbSem.setBounds(300, 180, 150, 30);
        add(cbSem);

        // Course Dropdown
        addLabel("Course ID", 500, 180);
        cbCourse = new JComboBox<>();
        cbCourse.setBounds(700, 180, 150, 30);
        add(cbCourse);

        // Internal Min
        addLabel("Internal Min Mark", 100, 240);
        tfIntMin = addTextField(300, 240);

        // Internal Max
        addLabel("Internal Max Mark", 500, 240);
        tfIntMax = addTextField(700, 240);

        // External Min
        addLabel("External Min Mark", 100, 300);
        tfExtMin = addTextField(300, 300);

        // External Max
        addLabel("External Max Mark", 500, 300);
        tfExtMax = addTextField(700, 300);

        // Subject Type
        addLabel("Subject Type", 100, 360);
        String types[] = {"Theory", "Practical"};
        cbType = new JComboBox<>(types);
        cbType.setBounds(300, 360, 150, 30);
        add(cbType);

        // Buttons
        submit = new JButton("Submit");
        submit.setBounds(300, 450, 150, 40);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(500, 450, 150, 40);
        cancel.addActionListener(this);
        add(cancel);

        loadTeachers();
        loadSemesters();
        loadCourses();

        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 180, 30);
        label.setFont(new Font("serif", Font.BOLD, 18));
        add(label);
    }

    private JTextField addTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 150, 30);
        add(field);
        return field;
    }

    // Load Teacher Names
    private void loadTeachers() {
        try {
            Conn con = new Conn();
            String query = "SELECT name FROM teacher";
            ResultSet rs = con.s.executeQuery(query);

            while (rs.next()) {
                cbTeacher.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load Semester IDs
    private void loadSemesters() {
        try {
            Conn con = new Conn();
            String query = "SELECT semid FROM semester";
            ResultSet rs = con.s.executeQuery(query);

            while (rs.next()) {
                cbSem.addItem(rs.getString("semid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load Course IDs
    private void loadCourses() {
        try {
            Conn con = new Conn();
            String query = "SELECT courseid FROM class";
            ResultSet rs = con.s.executeQuery(query);

            while (rs.next()) {
                cbCourse.addItem(rs.getString("courseid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {

            try {

                Conn con = new Conn();

                // Get teacher empId using name
                String teacherName = (String) cbTeacher.getSelectedItem();
                String teacherId = "";

                String teacherQuery = "SELECT empId FROM teacher WHERE name=?";
                PreparedStatement pstTeacher = con.c.prepareStatement(teacherQuery);
                pstTeacher.setString(1, teacherName);
                ResultSet rs = pstTeacher.executeQuery();

                if (rs.next()) {
                    teacherId = rs.getString("empId");
                }

                String query = "INSERT INTO subject1 " +
                        "(subject_name, teacher_id, sem_id, course_id, " +
                        "internal_min, internal_max, external_min, external_max, subject_type) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.c.prepareStatement(query);

                ps.setString(1, tfSubName.getText());
                ps.setString(2, teacherId);
                ps.setString(3, (String) cbSem.getSelectedItem());
                ps.setString(4, (String) cbCourse.getSelectedItem());
                ps.setInt(5, Integer.parseInt(tfIntMin.getText()));
                ps.setInt(6, Integer.parseInt(tfIntMax.getText()));
                ps.setInt(7, Integer.parseInt(tfExtMin.getText()));
                ps.setInt(8, Integer.parseInt(tfExtMax.getText()));
                ps.setString(9, (String) cbType.getSelectedItem());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Subject Added Successfully");
                dispose();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Adding Subject");
            }

        } else {
            dispose();
        }
    }

    public static void main(String[] args) {
        new AddSubject();
    }
}