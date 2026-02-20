package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TeacherProfile extends JFrame {

    TeacherProfile(String empIdValue) {

        setTitle("My Profile");
        setSize(750, 600);
        setLocation(400, 120);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Teacher Profile");
        heading.setBounds(250, 20, 300, 35);
        heading.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(heading);

        // ===== PROFILE IMAGE =====
        JLabel profileImage = new JLabel();
        profileImage.setBounds(480, 120, 200, 200);
        profileImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(profileImage);

        Font labelFont = new Font("Tahoma", Font.BOLD, 18);
        Font valueFont = new Font("Tahoma", Font.PLAIN, 18);

        JLabel lblname = new JLabel("Name:");
        lblname.setBounds(80, 120, 180, 30);
        lblname.setFont(labelFont);
        add(lblname);

        JLabel nameLabel = new JLabel();
        nameLabel.setBounds(260, 120, 220, 30);
        nameLabel.setFont(valueFont);
        add(nameLabel);

        JLabel lblfname = new JLabel("Father Name:");
        lblfname.setBounds(80, 170, 180, 30);
        lblfname.setFont(labelFont);
        add(lblfname);

        JLabel fnameLabel = new JLabel();
        fnameLabel.setBounds(260, 170, 220, 30);
        fnameLabel.setFont(valueFont);
        add(fnameLabel);

        JLabel lblemp = new JLabel("Employee ID:");
        lblemp.setBounds(80, 220, 180, 30);
        lblemp.setFont(labelFont);
        add(lblemp);

        JLabel empIdLabel = new JLabel();
        empIdLabel.setBounds(260, 220, 220, 30);
        empIdLabel.setFont(valueFont);
        add(empIdLabel);

        JLabel lbldept = new JLabel("Department:");
        lbldept.setBounds(80, 270, 180, 30);
        lbldept.setFont(labelFont);
        add(lbldept);

        JLabel departmentLabel = new JLabel();
        departmentLabel.setBounds(260, 270, 220, 30);
        departmentLabel.setFont(valueFont);
        add(departmentLabel);

        JLabel lbledu = new JLabel("Education:");
        lbledu.setBounds(80, 320, 180, 30);
        lbledu.setFont(labelFont);
        add(lbledu);

        JLabel educationLabel = new JLabel();
        educationLabel.setBounds(260, 320, 220, 30);
        educationLabel.setFont(valueFont);
        add(educationLabel);

        JLabel lblemail = new JLabel("Email:");
        lblemail.setBounds(80, 370, 180, 30);
        lblemail.setFont(labelFont);
        add(lblemail);

        JLabel emailLabel = new JLabel();
        emailLabel.setBounds(260, 370, 220, 30);
        emailLabel.setFont(valueFont);
        add(emailLabel);

        JLabel lblphone = new JLabel("Phone:");
        lblphone.setBounds(80, 420, 180, 30);
        lblphone.setFont(labelFont);
        add(lblphone);

        JLabel phoneLabel = new JLabel();
        phoneLabel.setBounds(260, 420, 220, 30);
        phoneLabel.setFont(valueFont);
        add(phoneLabel);

        // ===== Fetch Data From Database =====
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM teacher WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empIdValue);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                nameLabel.setText(rs.getString("name"));
                fnameLabel.setText(rs.getString("fname"));
                empIdLabel.setText(rs.getString("empId"));
                departmentLabel.setText(rs.getString("department"));
                educationLabel.setText(rs.getString("education"));
                emailLabel.setText(rs.getString("email"));
                phoneLabel.setText(rs.getString("phone"));

                String imgPath = rs.getString("profile_pic");

                if (imgPath != null && !imgPath.equals("")) {
                    ImageIcon icon = new ImageIcon(imgPath);
                    Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    profileImage.setIcon(new ImageIcon(img));
                }

            } else {
                JOptionPane.showMessageDialog(this, "Profile not found");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading profile");
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new TeacherProfile("T101");
    }
}