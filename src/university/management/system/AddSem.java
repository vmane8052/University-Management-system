package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddSem extends JFrame implements ActionListener {

    JTextField tfSemName;
    JComboBox<String> cbSemId;
    JButton submit, cancel;

    AddSem() {

        setSize(900, 600);
        setLocation(350, 100);
        setLayout(null);

        JLabel heading = new JLabel("New Semester Details");
        heading.setBounds(250, 30, 400, 40);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // Sem Id
        JLabel lblSemId = new JLabel("Semester Id");
        lblSemId.setBounds(100, 150, 150, 30);
        lblSemId.setFont(new Font("serif", Font.BOLD, 20));
        add(lblSemId);

        String sem[] = {"Sem 1", "Sem 2", "Sem 3", "Sem 4",
                        "Sem 5", "Sem 6", "Sem 7", "Sem 8"};

        cbSemId = new JComboBox<>(sem);
        cbSemId.setBounds(300, 150, 200, 30);
        add(cbSemId);

        // Sem Name
        JLabel lblSemName = new JLabel("Semester Name");
        lblSemName.setBounds(100, 220, 200, 30);
        lblSemName.setFont(new Font("serif", Font.BOLD, 20));
        add(lblSemName);

        tfSemName = new JTextField();
        tfSemName.setBounds(300, 220, 200, 30);
        add(tfSemName);

        // Buttons
        submit = new JButton("Submit");
        submit.setBounds(250, 350, 120, 40);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(400, 350, 120, 40);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {

            String semid = (String) cbSemId.getSelectedItem();
            String semname = tfSemName.getText();

            if (semname.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All Fields Are Required");
                return;
            }

            try {

                Conn con = new Conn();

                PreparedStatement ps = con.c.prepareStatement(
                        "INSERT INTO semester(semid, sem_name) VALUES (?, ?)");

                ps.setString(1, semid);
                ps.setString(2, semname);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Semester Added Successfully");

                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddSem();
    }
}