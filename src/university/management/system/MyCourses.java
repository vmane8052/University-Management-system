package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyCourses extends JFrame {

    private JTextField courseField, semField;
    private DefaultListModel<String> subjectModel;
    private String rollno;

    public MyCourses(String rollno) {
        this.rollno = rollno.trim();

        setTitle("My Courses - " + rollno);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Main Card Panel (centered)
        JPanel card = new JPanel();
        card.setBounds(120, 40, 500, 550);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(card);

        // Heading
        JLabel heading = new JLabel("My Courses & Semester", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setBounds(50, 20, 400, 35);
        card.add(heading);

        // Course
        JLabel courseLbl = new JLabel("Course / Class:");
        courseLbl.setBounds(50, 80, 200, 30);
        courseLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(courseLbl);

        courseField = new JTextField("Loading...");
        courseField.setBounds(50, 115, 400, 40);
        courseField.setEditable(false);
        courseField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(courseField);

        // Semester
        JLabel semLbl = new JLabel("Semester:");
        semLbl.setBounds(50, 180, 200, 30);
        semLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(semLbl);

        semField = new JTextField("Loading...");
        semField.setBounds(50, 215, 400, 40);
        semField.setEditable(false);
        semField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        card.add(semField);

        // Subjects List
        JLabel subLbl = new JLabel("Enrolled Subjects:");
        subLbl.setBounds(50, 280, 200, 30);
        subLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(subLbl);

        subjectModel = new DefaultListModel<>();
        JList<String> subjectList = new JList<>(subjectModel);
        subjectList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(subjectList);
        scrollPane.setBounds(50, 315, 400, 180);
        card.add(scrollPane);

        // Back Button
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setBounds(150, 520, 200, 45);
        backBtn.setBackground(new Color(108, 117, 125));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.addActionListener(e -> dispose());
        card.add(backBtn);

        // Load data using rollno
        loadCourses();

        setVisible(true);
    }

    private void loadCourses() {
        try {
            Conn c = new Conn();

            // Step 1: Get student's class_name and semester_name
            String studentQuery = "SELECT class_name, semester_name FROM student WHERE rollno = ?";
            PreparedStatement ps1 = c.c.prepareStatement(studentQuery);
            ps1.setString(1, rollno);
            ResultSet rs1 = ps1.executeQuery();

            String className = "N/A";
            String semesterName = "N/A";

            if (rs1.next()) {
                className = rs1.getString("class_name") != null ? rs1.getString("class_name").trim() : "N/A";
                semesterName = rs1.getString("semester_name") != null ? rs1.getString("semester_name").trim() : "N/A";
            }

            courseField.setText(className);
            semField.setText(semesterName);

            // Step 2: Get subjects for this class/semester
            String subQuery = "SELECT subject_name FROM subject1 " +
                             "WHERE REPLACE(UPPER(TRIM(course_id)), ' ', '') = REPLACE(UPPER(TRIM(?)), ' ', '') " +
                             "  AND REPLACE(UPPER(TRIM(sem_id)), ' ', '') = REPLACE(UPPER(TRIM(?)), ' ', '') " +
                             "ORDER BY subject_name";

            PreparedStatement psSub = c.c.prepareStatement(subQuery);
            psSub.setString(1, className);
            psSub.setString(2, semesterName);
            ResultSet rsSub = psSub.executeQuery();

            subjectModel.clear();
            int count = 1;
            boolean hasSubjects = false;

            while (rsSub.next()) {
                hasSubjects = true;
                subjectModel.addElement(count + ". " + rsSub.getString("subject_name").trim());
                count++;
            }

            if (!hasSubjects) {
                subjectModel.addElement("No subjects found for this class/semester.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading courses:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new MyCourses("15333936"); // Test with your student rollno
    }
}