package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyCourses extends JFrame {

    MyCourses(String email) {

        setTitle("My Courses");
        setSize(700, 700);  // 🔥 Increased Size
        setLocationRelativeTo(null); // Center screen
        setLayout(null);
        getContentPane().setBackground(Color.WHITE); // ✅ Full white background

        // ===== MAIN CARD PANEL =====
        JPanel card = new JPanel();
        card.setBounds(120, 40, 450, 520); // Center adjusted for bigger screen
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(card);

        JLabel heading = new JLabel("Your Course and Semester", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBounds(50, 20, 350, 30);
        card.add(heading);

        // ===== COURSE =====
        JLabel courseLbl = new JLabel("Course Name");
        courseLbl.setBounds(40, 80, 200, 25);
        courseLbl.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(courseLbl);

        JTextField courseField = new JTextField();
        courseField.setBounds(40, 110, 360, 40);
        courseField.setEditable(false);
        courseField.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(courseField);

        // ===== SEMESTER =====
        JLabel semLbl = new JLabel("Semester Name");
        semLbl.setBounds(40, 170, 200, 25);
        semLbl.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(semLbl);

        JTextField semField = new JTextField();
        semField.setBounds(40, 200, 360, 40);
        semField.setEditable(false);
        semField.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(semField);

        // ===== SUBJECTS =====
        JLabel subLbl = new JLabel("Subjects");
        subLbl.setBounds(40, 260, 200, 25);
        subLbl.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(subLbl);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> subjectList = new JList<>(model);
        subjectList.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(subjectList);
        scrollPane.setBounds(40, 290, 360, 140);
        card.add(scrollPane);

        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setBounds(130, 450, 190, 40);
        backBtn.setBackground(new Color(108, 117, 125));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 15));
        card.add(backBtn);

        // ===== DATABASE LOGIC =====
        try {
            Conn c = new Conn();

            String studentQuery = "SELECT course, semester_name FROM student WHERE email = '" + email + "'";
            ResultSet rs1 = c.s.executeQuery(studentQuery);

            if (rs1.next()) {

                String course = rs1.getString("course");
                String semesterName = rs1.getString("semester_name");

                courseField.setText(course);
                semField.setText(semesterName);

                String semQuery = "SELECT semid FROM semester WHERE sem_name = '" + semesterName + "'";
                ResultSet rs2 = c.s.executeQuery(semQuery);

                if (rs2.next()) {

                    String semId = rs2.getString("semid");

                    String subQuery = "SELECT subject_name FROM subject1 WHERE sem_id = '" 
                            + semId + "' AND course_id = '" + course + "'";

                    ResultSet rs3 = c.s.executeQuery(subQuery);

                    int count = 1;
                    while (rs3.next()) {
                        model.addElement(count + " - " + rs3.getString("subject_name"));
                        count++;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading subjects");
        }

        setVisible(true);
    }
}