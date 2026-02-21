package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CourseListPage extends JFrame {

    public CourseListPage() {
        setTitle("All Courses / Classes");
        setSize(780, 580);
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 60, 120));
        headerPanel.setPreferredSize(new Dimension(780, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JLabel heading = new JLabel("All Classes / Courses");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading);

        add(headerPanel, BorderLayout.NORTH);

        // Main content - scrollable panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        try {
            Conn c = new Conn();

            // Use class_name instead of course (since course column is deleted)
            String query = "SELECT DISTINCT class_name FROM student WHERE class_name IS NOT NULL AND class_name != '' ORDER BY class_name";
            ResultSet rs = c.s.executeQuery(query);

            boolean hasCourses = false;

            while (rs.next()) {
                hasCourses = true;
                String className = rs.getString("class_name").trim();

                // Course/Class row panel
                JPanel rowPanel = new JPanel();
                rowPanel.setLayout(new BorderLayout());
                rowPanel.setBackground(Color.WHITE);
                rowPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
                rowPanel.setMaximumSize(new Dimension(700, 60));
                rowPanel.setPreferredSize(new Dimension(700, 60));

                // Class name label
                JLabel courseLabel = new JLabel(className);
                courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                courseLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));
                rowPanel.add(courseLabel, BorderLayout.WEST);

                // View Students button
                JButton viewBtn = new JButton("View Students");
                viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                viewBtn.setBackground(new Color(40, 167, 69));
                viewBtn.setForeground(Color.WHITE);
                viewBtn.setFocusPainted(false);
                viewBtn.setPreferredSize(new Dimension(160, 40));
                viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Hover effect
                viewBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        viewBtn.setBackground(new Color(34, 139, 34));
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        viewBtn.setBackground(new Color(40, 167, 69));
                    }
                });

                viewBtn.addActionListener(e -> {
                    new CourseWiseStudents(className);
                });

                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                btnPanel.setBackground(Color.WHITE);
                btnPanel.add(viewBtn);
                rowPanel.add(btnPanel, BorderLayout.EAST);

                contentPanel.add(rowPanel);
            }

            if (!hasCourses) {
                JLabel noData = new JLabel("No classes found in the database.");
                noData.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                noData.setForeground(Color.GRAY);
                noData.setHorizontalAlignment(SwingConstants.CENTER);
                noData.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));
                contentPanel.add(noData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading classes: " + e.getMessage());
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            errorLabel.setForeground(Color.RED);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));
            contentPanel.add(errorLabel);
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new CourseListPage();
    }
}