package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FillMarksPage extends JFrame implements ActionListener {

    JTextField subjectField, marksField;
    JButton saveBtn;
    String rollno, teacherEmpId;

    FillMarksPage(String rollno, String teacherEmpId) {

        this.rollno = rollno;
        this.teacherEmpId = teacherEmpId;

        setTitle("Fill Marks");
        setSize(400, 300);
        setLocation(500, 200);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel subject = new JLabel("Subject:");
        subject.setBounds(50, 50, 100, 30);
        add(subject);

        subjectField = new JTextField();
        subjectField.setBounds(150, 50, 150, 30);
        add(subjectField);

        JLabel marks = new JLabel("Marks:");
        marks.setBounds(50, 100, 100, 30);
        add(marks);

        marksField = new JTextField();
        marksField.setBounds(150, 100, 150, 30);
        add(marksField);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(130, 170, 120, 30);
        saveBtn.setBackground(Color.BLACK);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        try {
            Conn c = new Conn();

            String query = "INSERT INTO marks (rollno, subject, marks, teacher_empId) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = c.c.prepareStatement(query);

            ps.setString(1, rollno);
            ps.setString(2, subjectField.getText());
            ps.setInt(3, Integer.parseInt(marksField.getText()));
            ps.setString(4, teacherEmpId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Marks Saved Successfully");

            new AddMarksPage(teacherEmpId);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}