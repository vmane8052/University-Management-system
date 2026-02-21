package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class MyProfile extends JFrame {

    private JLabel profileImage;
    private JLabel nameLbl, fnameLbl, rollLbl, classLbl, semLbl, branchLbl, emailLbl, phoneLbl;
    private String rollno;

    public MyProfile(String rollno) {
        this.rollno = rollno.trim();

        setTitle("My Profile - " + rollno);
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));
        mainPanel.setBackground(new Color(245, 245, 250));
        add(mainPanel, BorderLayout.CENTER);

        // Header
        JLabel heading = new JLabel("My Profile", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(heading, BorderLayout.NORTH);

        // Profile Card
        JPanel card = new JPanel(new BorderLayout(40, 20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        mainPanel.add(card, BorderLayout.CENTER);

        // Left: Profile Image (rounded)
        JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imgPanel.setBackground(Color.WHITE);

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
        profileImage.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 4));
        profileImage.setText("Loading...");
        profileImage.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        profileImage.setForeground(Color.GRAY);

        imgPanel.add(profileImage);
        card.add(imgPanel, BorderLayout.WEST);

        // Right: Details Grid
        JPanel details = new JPanel(new GridBagLayout());
        details.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 15, 14, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font lblFont = new Font("Segoe UI", Font.BOLD, 18);
        Font valFont = new Font("Segoe UI", Font.PLAIN, 18);

        int row = 0;

        nameLbl   = addRow(details, gbc, row++, "Name:", "", lblFont, valFont);
        fnameLbl  = addRow(details, gbc, row++, "Father's Name:", "", lblFont, valFont);
        rollLbl   = addRow(details, gbc, row++, "Roll Number:", "", lblFont, valFont);
        classLbl  = addRow(details, gbc, row++, "Class:", "", lblFont, valFont);
        semLbl    = addRow(details, gbc, row++, "Semester:", "", lblFont, valFont);
        branchLbl = addRow(details, gbc, row++, "Branch:", "", lblFont, valFont);
        emailLbl  = addRow(details, gbc, row++, "Email:", "", lblFont, valFont);
        phoneLbl  = addRow(details, gbc, row++, "Phone:", "", lblFont, valFont);

        card.add(details, BorderLayout.CENTER);

        // Load data using rollno
        loadProfile();

        setVisible(true);
    }

    private JLabel addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String initialValue,
                          Font lblFont, Font valFont) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(lblFont);
        lbl.setForeground(new Color(60, 60, 80));

        JLabel val = new JLabel(initialValue);
        val.setFont(valFont);
        val.setForeground(new Color(30, 30, 50));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(val, gbc);

        return val; // Return reference for later update
    }

    private void loadProfile() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM student WHERE rollno = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameLbl.setText(rs.getString("name") != null ? rs.getString("name").trim() : "N/A");
                fnameLbl.setText(rs.getString("fname") != null ? rs.getString("fname").trim() : "N/A");
                rollLbl.setText(rs.getString("rollno") != null ? rs.getString("rollno").trim() : "N/A");
                classLbl.setText(rs.getString("class_name") != null ? rs.getString("class_name").trim() : "N/A");
                semLbl.setText(rs.getString("semester_name") != null ? rs.getString("semester_name").trim() : "N/A");
                branchLbl.setText(rs.getString("branch") != null ? rs.getString("branch").trim() : "N/A");
                emailLbl.setText(rs.getString("email") != null ? rs.getString("email").trim() : "N/A");
                phoneLbl.setText(rs.getString("phone") != null ? rs.getString("phone").trim() : "N/A");

                String imgPath = rs.getString("profile_pic");
                if (imgPath != null && !imgPath.trim().isEmpty()) {
                    try {
                        ImageIcon icon = new ImageIcon(imgPath);
                        Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                        profileImage.setIcon(new ImageIcon(img));
                        profileImage.setText("");
                    } catch (Exception imgEx) {
                        profileImage.setText("Image failed to load");
                    }
                } else {
                    profileImage.setText("No Photo");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No profile found for Roll Number: " + rollno,
                        "Not Found",
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
        new MyProfile("15333936"); // test with your student rollno
    }
}