package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class TeacherProfile extends JFrame {

    private JLabel profileImage;
    private JLabel nameLabel, fnameLabel, empIdLabel, departmentLabel, educationLabel, emailLabel, phoneLabel;
    private String empIdValue;

    public TeacherProfile(String empId) {
        this.empIdValue = empId.trim(); // safety trim

        setTitle("Teacher Profile - " + empId);
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBorder(new EmptyBorder(20, 40, 30, 40));
        mainPanel.setBackground(new Color(245, 245, 250));
        add(mainPanel, BorderLayout.CENTER);

        // Header with Gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 60, 120), getWidth(), getHeight(), new Color(60, 100, 180));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(850, 100));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 150, 255)));

        JLabel heading = new JLabel("Teacher Profile", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));
        heading.setForeground(Color.WHITE);
        headerPanel.add(heading, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Profile Card
        JPanel profileCard = new JPanel(new BorderLayout(30, 20));
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        profileCard.setPreferredSize(new Dimension(780, 480));
        mainPanel.add(profileCard, BorderLayout.CENTER);

        // Left: Profile Image
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(Color.WHITE);

        profileImage = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (getIcon() != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.clip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
                    super.paintComponent(g2);
                    g2.dispose();
                }
            }
        };
        profileImage.setPreferredSize(new Dimension(220, 220));
        profileImage.setOpaque(false);
        profileImage.setHorizontalAlignment(SwingConstants.CENTER);
        profileImage.setVerticalAlignment(SwingConstants.CENTER);
        profileImage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 255), 4),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        profileImage.setText("Loading...");
        profileImage.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        profileImage.setForeground(Color.GRAY);

        imagePanel.add(profileImage);
        profileCard.add(imagePanel, BorderLayout.WEST);

        // Right: Details Grid
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 12, 14, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 18);

        int row = 0;

        // Name
        nameLabel = addDetailRow(detailsPanel, gbc, row++, "Name:", "", labelFont, valueFont);

        // Father's Name
        fnameLabel = addDetailRow(detailsPanel, gbc, row++, "Father's Name:", "", labelFont, valueFont);

        // Employee ID
        empIdLabel = addDetailRow(detailsPanel, gbc, row++, "Employee ID:", "", labelFont, valueFont);

        // Department
        departmentLabel = addDetailRow(detailsPanel, gbc, row++, "Department:", "", labelFont, valueFont);

        // Education
        educationLabel = addDetailRow(detailsPanel, gbc, row++, "Education:", "", labelFont, valueFont);

        // Email
        emailLabel = addDetailRow(detailsPanel, gbc, row++, "Email:", "", labelFont, valueFont);

        // Phone
        phoneLabel = addDetailRow(detailsPanel, gbc, row++, "Phone:", "", labelFont, valueFont);

        profileCard.add(detailsPanel, BorderLayout.CENTER);

        // Load data from database
        loadProfileData();

        setVisible(true);
    }

    private JLabel addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String initialValue,
                               Font labelFont, Font valueFont) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(labelFont);
        lbl.setForeground(new Color(60, 60, 80));

        JLabel val = new JLabel(initialValue);
        val.setFont(valueFont);
        val.setForeground(new Color(30, 30, 50));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(val, gbc);

        return val; // return reference so we can update later
    }

    private void loadProfileData() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM teacher WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empIdValue);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameLabel.setText(rs.getString("name") != null ? rs.getString("name").trim() : "N/A");
                fnameLabel.setText(rs.getString("fname") != null ? rs.getString("fname").trim() : "N/A");
                empIdLabel.setText(rs.getString("empId") != null ? rs.getString("empId").trim() : "N/A");
                departmentLabel.setText(rs.getString("department") != null ? rs.getString("department").trim() : "N/A");
                educationLabel.setText(rs.getString("education") != null ? rs.getString("education").trim() : "N/A");
                emailLabel.setText(rs.getString("email") != null ? rs.getString("email").trim() : "N/A");
                phoneLabel.setText(rs.getString("phone") != null ? rs.getString("phone").trim() : "N/A");

                String imgPath = rs.getString("profile_pic");
                if (imgPath != null && !imgPath.trim().isEmpty()) {
                    try {
                        ImageIcon icon = new ImageIcon(imgPath);
                        Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                        profileImage.setIcon(new ImageIcon(img));
                        profileImage.setText(""); // clear "Loading..."
                    } catch (Exception imgEx) {
                        profileImage.setText("Image failed to load");
                    }
                } else {
                    profileImage.setText("No Profile Photo");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No teacher found with Employee ID: " + empIdValue,
                        "Profile Not Found",
                        JOptionPane.WARNING_MESSAGE);
                dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading profile:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new TeacherProfile("101455"); // change to your actual empId for testing
    }
}