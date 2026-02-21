package university.management.system;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddMarksPage extends JFrame {
    AddMarksPage(String teacherEmpId) {
        setTitle("Add Marks");
        setSize(900, 650);
        setLocation(300, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Add Marks - Course Students");
        heading.setBounds(250, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(heading);

        int y = 130;

        try {
            Conn c = new Conn();

            // Get Teacher's assigned class
            String classQuery = "SELECT class_teacher FROM teacher WHERE empId=?";
            PreparedStatement ps1 = c.c.prepareStatement(classQuery);
            ps1.setString(1, teacherEmpId);
            ResultSet rs1 = ps1.executeQuery();
            String teacherCourse = "";
            if (rs1.next()) {
                teacherCourse = rs1.getString("class_teacher");
            }

            // Show Class Name
            JLabel classLabel = new JLabel("Class: " + teacherCourse);
            classLabel.setBounds(100, 80, 400, 30);
            classLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
            add(classLabel);

            // Get Students of this class
            String studentQuery = "SELECT * FROM student WHERE class_name=?";
            PreparedStatement ps2 = c.c.prepareStatement(studentQuery);
            ps2.setString(1, teacherCourse);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                String studentName = rs2.getString("name");
                String rollno = rs2.getString("rollno");

                // Student Name
                JLabel nameLabel = new JLabel(studentName + " (" + rollno + ")");
                nameLabel.setBounds(100, y, 250, 30);
                nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
                add(nameLabel);

                // Add Marks Button
                JButton addBtn = new JButton("Add Marks");
                addBtn.setBounds(380, y, 150, 30);
                addBtn.setBackground(Color.BLACK);
                addBtn.setForeground(Color.WHITE);
                add(addBtn);

                // Status Label
                JLabel statusLabel = new JLabel();
                statusLabel.setBounds(560, y, 200, 30);
                statusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
                add(statusLabel);

                // Check if marks already filled for this student by this teacher
                // (Agar koi bhi subject ka mark entry hai to "Mark Filled" dikhao)
                String checkQuery = "SELECT COUNT(*) FROM marks WHERE rollno=? AND teacher_empId=?";
                PreparedStatement ps3 = c.c.prepareStatement(checkQuery);
                ps3.setString(1, rollno);
                ps3.setString(2, teacherEmpId);
                ResultSet rs3 = ps3.executeQuery();

                if (rs3.next()) {
                    int count = rs3.getInt(1);
                    if (count > 0) {
                        statusLabel.setText("Mark Filled");
                        statusLabel.setForeground(Color.GREEN);
                    } else {
                        statusLabel.setText("Mark Not Filled");
                        statusLabel.setForeground(Color.RED);
                    }
                } else {
                    statusLabel.setText("Mark Not Filled");
                    statusLabel.setForeground(Color.RED);
                }

                String finalRoll = rollno;
                addBtn.addActionListener(e -> {
                    new FillMarksPage(finalRoll, teacherEmpId);
                    dispose();
                });

                y += 50;
            }

            if (y == 130) {
                JLabel noStudent = new JLabel("No students found for this class.");
                noStudent.setBounds(300, 200, 400, 30);
                noStudent.setFont(new Font("Tahoma", Font.BOLD, 16));
                add(noStudent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }
}