package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddMarksPage extends JFrame {

    AddMarksPage(String teacherEmpId) {

        setTitle("Add Marks");
        setSize(800, 600);
        setLocation(350, 120);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Add Marks - Class Students");
        heading.setBounds(220, 20, 400, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(heading);

        int y = 80;

        try {
            Conn c = new Conn();

            // Step 1: Get Teacher Class
            String classQuery = "SELECT class_teacher FROM teacher WHERE empId=?";
            PreparedStatement ps1 = c.c.prepareStatement(classQuery);
            ps1.setString(1, teacherEmpId);
            ResultSet rs1 = ps1.executeQuery();

            String teacherClass = "";

            if (rs1.next()) {
                teacherClass = rs1.getString("class_teacher");
            }

            // Step 2: Get Students of That Class
            String studentQuery = "SELECT * FROM student WHERE class_name=?";
            PreparedStatement ps2 = c.c.prepareStatement(studentQuery);
            ps2.setString(1, teacherClass);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {

                String studentName = rs2.getString("name");
                String rollno = rs2.getString("rollno");

                JLabel nameLabel = new JLabel(studentName + " (" + rollno + ")");
                nameLabel.setBounds(120, y, 250, 30);
                add(nameLabel);

                JButton markBtn = new JButton("Fill Marks");
                markBtn.setBounds(400, y, 150, 30);
                add(markBtn);

                // Check if marks already filled
                String checkQuery = "SELECT * FROM marks WHERE rollno=?";
                PreparedStatement ps3 = c.c.prepareStatement(checkQuery);
                ps3.setString(1, rollno);
                ResultSet rs3 = ps3.executeQuery();

                if (rs3.next()) {
                    markBtn.setBackground(Color.GREEN);
                } else {
                    markBtn.setBackground(Color.RED);
                }

                markBtn.setForeground(Color.WHITE);

                String finalRoll = rollno;

                markBtn.addActionListener(e -> {
                    new FillMarksPage(finalRoll, teacherEmpId);
                    dispose();
                });

                y += 50;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }
}