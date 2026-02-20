package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CourseListPage extends JFrame {

    CourseListPage() {

        setTitle("All Courses");
        setSize(700, 500);
        setLocation(400, 150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Course List");
        heading.setBounds(250, 20, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(heading);

        int y = 80;

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT DISTINCT course FROM student");

            while (rs.next()) {

                String courseName = rs.getString("course");

                JLabel courseLabel = new JLabel(courseName);
                courseLabel.setBounds(150, y, 200, 30);
                courseLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
                add(courseLabel);

                JButton viewBtn = new JButton("View Students");
                viewBtn.setBounds(350, y, 150, 30);
                add(viewBtn);

                viewBtn.addActionListener(e -> {
                    new CourseWiseStudents(courseName);
                });

                y += 50;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new CourseListPage();
    }
}