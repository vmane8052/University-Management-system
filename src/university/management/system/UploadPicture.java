package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.*;

public class UploadPicture extends JFrame implements ActionListener {

    private String rollno;
    private JLabel imageLabel;
    private JButton chooseBtn, saveBtn;
    private String imagePath = "";

    public UploadPicture(String rollno) {
        this.rollno = rollno.trim();

        setTitle("Upload Profile Picture - " + rollno);
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 250));

        // Heading
        JLabel heading = new JLabel("Upload Your Profile Picture");
        heading.setBounds(100, 20, 350, 35);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(30, 60, 120));
        add(heading);

        // Image Preview
        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imageLabel.setBounds(150, 80, 250, 250);
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 3));
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setOpaque(true);
        add(imageLabel);

        // Choose Button
        chooseBtn = new JButton("Choose Image");
        chooseBtn.setBounds(150, 350, 250, 40);
        chooseBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chooseBtn.setBackground(new Color(40, 167, 69));
        chooseBtn.setForeground(Color.WHITE);
        chooseBtn.setFocusPainted(false);
        chooseBtn.addActionListener(this);
        add(chooseBtn);

        // Save Button
        saveBtn = new JButton("Save Picture");
        saveBtn.setBounds(150, 410, 250, 40);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveBtn.setBackground(new Color(0, 123, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseBtn) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePath = selectedFile.getAbsolutePath().replace("\\", "/"); // Use forward slashes

                try {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                    imageLabel.setText(""); // Clear "No Image" text
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid image file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (e.getSource() == saveBtn) {
            if (imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please choose an image first!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Conn conn = new Conn();
                String query = "UPDATE student SET profile_pic = ? WHERE rollno = ?";
                PreparedStatement ps = conn.c.prepareStatement(query);
                ps.setString(1, imagePath);
                ps.setString(2, rollno);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Profile picture uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close window after success
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with Roll No: " + rollno, "Not Found", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving picture:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new UploadPicture("15333936"); // Test with your student rollno
    }
}