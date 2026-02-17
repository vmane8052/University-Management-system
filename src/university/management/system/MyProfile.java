package university.management.system;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyProfile extends JFrame {

    MyProfile(String email) {

        setTitle("My Profile");
        setSize(750, 600);
        setLocation(400, 120);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Student Profile");
        heading.setBounds(260, 20, 300, 35);
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

        JLabel name = new JLabel();
        name.setBounds(260, 120, 220, 30);
        name.setFont(valueFont);
        add(name);

        JLabel lblfname = new JLabel("Father Name:");
        lblfname.setBounds(80, 170, 180, 30);
        lblfname.setFont(labelFont);
        add(lblfname);

        JLabel fname = new JLabel();
        fname.setBounds(260, 170, 220, 30);
        fname.setFont(valueFont);
        add(fname);

        JLabel lblroll = new JLabel("Roll No:");
        lblroll.setBounds(80, 220, 180, 30);
        lblroll.setFont(labelFont);
        add(lblroll);

        JLabel roll = new JLabel();
        roll.setBounds(260, 220, 220, 30);
        roll.setFont(valueFont);
        add(roll);

        JLabel lblcourse = new JLabel("Course:");
        lblcourse.setBounds(80, 270, 180, 30);
        lblcourse.setFont(labelFont);
        add(lblcourse);

        JLabel course = new JLabel();
        course.setBounds(260, 270, 220, 30);
        course.setFont(valueFont);
        add(course);

        JLabel lblbranch = new JLabel("Branch:");
        lblbranch.setBounds(80, 320, 180, 30);
        lblbranch.setFont(labelFont);
        add(lblbranch);

        JLabel branch = new JLabel();
        branch.setBounds(260, 320, 220, 30);
        branch.setFont(valueFont);
        add(branch);

        JLabel lblemail = new JLabel("Email:");
        lblemail.setBounds(80, 370, 180, 30);
        lblemail.setFont(labelFont);
        add(lblemail);

        JLabel emaillbl = new JLabel();
        emaillbl.setBounds(260, 370, 220, 30);
        emaillbl.setFont(valueFont);
        add(emaillbl);

        JLabel lblphone = new JLabel("Phone:");
        lblphone.setBounds(80, 420, 180, 30);
        lblphone.setFont(labelFont);
        add(lblphone);

        JLabel phone = new JLabel();
        phone.setBounds(260, 420, 220, 30);
        phone.setFont(valueFont);
        add(phone);

        // ===== Fetch Data From Database =====
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM student WHERE email = '" + email + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {

                name.setText(rs.getString("name"));
                fname.setText(rs.getString("fname"));
                roll.setText(rs.getString("rollno"));
                course.setText(rs.getString("course"));
                branch.setText(rs.getString("branch"));
                emaillbl.setText(rs.getString("email"));
                phone.setText(rs.getString("phone"));

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
}