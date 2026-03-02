package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AttendancePercentageView extends JFrame {

    private String rollno;

    public AttendancePercentageView(String rollno) {
        super("Attendance Percentage");
        this.rollno = rollno.trim();

        setSize(480, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 245, 255));

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(240, 245, 255));

        // Title
        JLabel titleLabel = new JLabel("Attendance Summary", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 55, 110));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(4, 1, 12, 18));
        detailsPanel.setBackground(new Color(240, 245, 255));

        JLabel presentLabel = new JLabel("Present: Calculating...", SwingConstants.CENTER);
        JLabel absentLabel  = new JLabel("Absent: Calculating...", SwingConstants.CENTER);
        JLabel totalLabel   = new JLabel("Total Recorded: Calculating...", SwingConstants.CENTER);
        JLabel percentLabel = new JLabel("Percentage: Calculating...", SwingConstants.CENTER);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
        presentLabel.setFont(labelFont);
        absentLabel.setFont(labelFont);
        totalLabel.setFont(labelFont);
        percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        detailsPanel.add(presentLabel);
        detailsPanel.add(absentLabel);
        detailsPanel.add(totalLabel);
        detailsPanel.add(percentLabel);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Load data
        loadAttendanceData(presentLabel, absentLabel, totalLabel, percentLabel);

        setVisible(true);
    }

    private void loadAttendanceData(JLabel presentLabel, JLabel absentLabel,
                                    JLabel totalLabel, JLabel percentLabel) {

        int present = 0;
        int absent = 0;

        try {
            Conn conn = new Conn();
            String query = "SELECT status FROM attendance WHERE rollno = ?";
            PreparedStatement ps = conn.c.prepareStatement(query);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String status = rs.getString("status");
                if (status != null) {
                    String s = status.trim().toLowerCase();
                    if (s.contains("present") || s.contains("pre")) {
                        present++;
                    } else if (s.contains("absent") || s.contains("abs")) {
                        absent++;
                    }
                }
            }

            rs.close();
            ps.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            percentLabel.setText("Error loading attendance data");
            percentLabel.setForeground(Color.RED);
            return;
        }

        int total = present + absent;

        if (total == 0) {
            presentLabel.setText("Present: 0 days");
            absentLabel.setText("Absent: 0 days");
            totalLabel.setText("Total Recorded: 0 days");
            percentLabel.setText("Percentage: — (no records)");
            percentLabel.setForeground(Color.GRAY);
            return;
        }

        double percentage = (double) present / total * 100;
        String percentText = String.format("%.2f %%", percentage);

        presentLabel.setText("Present: " + present + " days");
        absentLabel.setText("Absent: " + absent + " days");
        totalLabel.setText("Total Recorded: " + total + " days");
        percentLabel.setText("Percentage: " + percentText);

        // Color coding
        if (percentage >= 75) {
            percentLabel.setForeground(new Color(0, 140, 0));      // green
        } else if (percentage >= 60) {
            percentLabel.setForeground(new Color(220, 140, 0));    // orange
        } else {
            percentLabel.setForeground(new Color(200, 0, 0));      // red
        }
    }

    // Test main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendancePercentageView("15333880"));
    }
}