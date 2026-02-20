package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class UploadTeacherPicture extends JFrame implements ActionListener {

    private String teacherEmail;
    private JLabel imageLabel;
    private JButton chooseBtn, saveBtn;
    private String imagePath = "";

    UploadTeacherPicture(String email) {

        this.teacherEmail = email;

        setTitle("Upload Teacher Profile Picture");
        setSize(500, 500);
        setLocation(500, 200);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Upload Teacher Profile Picture");
        heading.setBounds(90, 20, 350, 30);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        add(heading);

        imageLabel = new JLabel();
        imageLabel.setBounds(150, 80, 200, 200);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(imageLabel);

        chooseBtn = new JButton("Choose Image");
        chooseBtn.setBounds(150, 310, 200, 30);
        chooseBtn.addActionListener(this);
        add(chooseBtn);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(150, 360, 200, 30);
        saveBtn.setBackground(Color.BLACK);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Choose Image
        if (e.getSource() == chooseBtn) {

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {

                File selectedFile = fileChooser.getSelectedFile();
                imagePath = selectedFile.getAbsolutePath().replace("\\", "\\\\");

                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            }
        }

        // Save Image Path
        if (e.getSource() == saveBtn) {

            if (imagePath.equals("")) {
                JOptionPane.showMessageDialog(null, "Please select an image first");
                return;
            }

            try {
                Conn c = new Conn();

                String query = "UPDATE teacher SET profile_pic=? WHERE empId=?";
                PreparedStatement ps = c.c.prepareStatement(query);

                ps.setString(1, imagePath);
                ps.setString(2, teacherEmail);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Profile picture uploaded successfully");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new UploadTeacherPicture("teacher@example.com");
    }
}