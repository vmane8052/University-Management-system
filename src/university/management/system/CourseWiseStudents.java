package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CourseWiseStudents extends JFrame {

    CourseWiseStudents(String className) {
        setTitle("Students in " + className);
        setSize(900, 600);
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Heading
        JLabel heading = new JLabel("Students of Class: " + className, SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Table Model & Columns
        String[] columns = {"Name", "Roll No", "Branch", "Email", "Phone"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(30, 60, 120));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);

        // Auto-resize columns
        table.getColumnModel().getColumn(0).setPreferredWidth(180); // Name
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Roll No
        table.getColumnModel().getColumn(2).setPreferredWidth(140); // Branch
        table.getColumnModel().getColumn(3).setPreferredWidth(220); // Email
        table.getColumnModel().getColumn(4).setPreferredWidth(140); // Phone

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Load students
        try {
            Conn c = new Conn();

            // Use class_name instead of course
            String query = "SELECT name, rollno, branch, email, phone " +
                           "FROM student WHERE class_name = ? ORDER BY name";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, className);
            ResultSet rs = ps.executeQuery();

            boolean hasStudents = false;

            while (rs.next()) {
                hasStudents = true;
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("rollno"),
                    rs.getString("branch"),
                    rs.getString("email"),
                    rs.getString("phone")
                });
            }

            if (!hasStudents) {
                JLabel noData = new JLabel("No students found in this class.", SwingConstants.CENTER);
                noData.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                noData.setForeground(Color.GRAY);
                noData.setBorder(BorderFactory.createEmptyBorder(150, 0, 150, 0));
                add(noData, BorderLayout.CENTER);
                scrollPane.setVisible(false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading students: " + ex.getMessage(), SwingConstants.CENTER);
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            errorLabel.setForeground(Color.RED);
            errorLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 150, 0));
            add(errorLabel, BorderLayout.CENTER);
            scrollPane.setVisible(false);
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new CourseWiseStudents("B.Tech CSE"); // example
    }
}