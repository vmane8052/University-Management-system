package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class UploadPicture extends JFrame implements ActionListener {

    private String studentEmail;
    private JLabel imageLabel;
    private JButton chooseBtn, saveBtn;
    private String imagePath = "";

    UploadPicture(String email) {

        this.studentEmail = email;

        setTitle("Upload Profile Picture");
        setSize(500, 500);
        setLocation(500, 200);
        setLayout(null);

        JLabel heading = new JLabel("Upload Profile Picture");
        heading.setBounds(130, 20, 300, 30);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        add(heading);

        imageLabel = new JLabel();
        imageLabel.setBounds(150, 80, 200, 200);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(imageLabel);

        chooseBtn = new JButton("Choose Image");
        chooseBtn.setBounds(150, 310, 200, 30);
        chooseBtn.addActionListener(this);
        add(chooseBtn);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(150, 360, 200, 30);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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

        if (e.getSource() == saveBtn) {

            if (imagePath.equals("")) {
                JOptionPane.showMessageDialog(null, "Please select an image first");
                return;
            }

            try {
                Conn c = new Conn();
                String query = "update student set profile_pic='" + imagePath + "' where email='" + studentEmail + "'";
                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Profile picture uploaded successfully");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new UploadPicture("test@example.com");
    }
}