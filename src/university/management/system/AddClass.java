package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class AddClass extends JFrame implements ActionListener {

    JTextField tffCourseName, tfStudentLimit;
    JComboBox<String> cbcourse;
    JDateChooser dcdob;
    JButton submit, cancel;

    AddClass() {

        setSize(900, 600);
        setLocation(350, 100);
        setLayout(null);

        JLabel heading = new JLabel("New Class Details");
        heading.setBounds(280, 30, 400, 40);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // Course Id
        JLabel lblCourseId = new JLabel("Course Id");
        lblCourseId.setBounds(100, 120, 150, 30);
        lblCourseId.setFont(new Font("serif", Font.BOLD, 20));
        add(lblCourseId);

        String course[] = {"B.Tech", "BBA", "BCA", "BSc", "MSc", "MBA", "MCA", "MCom", "MA", "BA"};
        cbcourse = new JComboBox<>(course);
        cbcourse.setBounds(300, 120, 200, 30);
        add(cbcourse);

        // Course Name
        JLabel lblCourseName = new JLabel("Course Name");
        lblCourseName.setBounds(100, 180, 150, 30);
        lblCourseName.setFont(new Font("serif", Font.BOLD, 20));
        add(lblCourseName);

        tffCourseName = new JTextField();
        tffCourseName.setBounds(300, 180, 200, 30);
        add(tffCourseName);

        // Student Limit
        JLabel lblStudentLimit = new JLabel("Student Limit");
        lblStudentLimit.setBounds(100, 240, 150, 30);
        lblStudentLimit.setFont(new Font("serif", Font.BOLD, 20));
        add(lblStudentLimit);

        tfStudentLimit = new JTextField();
        tfStudentLimit.setBounds(300, 240, 200, 30);
        add(tfStudentLimit);

        // Effective Date
        JLabel lblDate = new JLabel("Effective From");
        lblDate.setBounds(100, 300, 150, 30);
        lblDate.setFont(new Font("serif", Font.BOLD, 20));
        add(lblDate);

        dcdob = new JDateChooser();
        dcdob.setBounds(300, 300, 200, 30);
        add(dcdob);

        // Buttons
        submit = new JButton("Submit");
        submit.setBounds(250, 420, 120, 40);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(400, 420, 120, 40);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {

            String courseid = (String) cbcourse.getSelectedItem();
            String coursename = tffCourseName.getText();
            String studentlimit = tfStudentLimit.getText();

            if (coursename.isEmpty() || studentlimit.isEmpty() || dcdob.getDate() == null) {
                JOptionPane.showMessageDialog(null, "All Fields Are Required");
                return;
            }

            try {

                java.util.Date utilDate = dcdob.getDate();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                Conn con = new Conn();

                PreparedStatement ps = con.c.prepareStatement(
                        "INSERT INTO class(courseid, coursename, studentlimit, effective_from) VALUES (?, ?, ?, ?)");

                ps.setString(1, courseid);
                ps.setString(2, coursename);
                ps.setInt(3, Integer.parseInt(studentlimit));
                ps.setDate(4, sqlDate);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Class Added Successfully");

                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddClass();
    }
}