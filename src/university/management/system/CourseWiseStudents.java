package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CourseWiseStudents extends JFrame {

    CourseWiseStudents(String courseName) {

        setTitle("Students - " + courseName);
        setSize(800, 500);
        setLocation(350, 150);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Students of " + courseName, JLabel.CENTER);
        heading.setFont(new Font("Tahoma", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        String[] column = {"Name", "Roll No", "Branch", "Email", "Phone"};
        DefaultTableModel model = new DefaultTableModel(column, 0);

        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        try {
            Conn c = new Conn();
            String query = "SELECT * FROM student WHERE course = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, courseName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("rollno"),
                        rs.getString("branch"),
                        rs.getString("email"),
                        rs.getString("phone")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }
}