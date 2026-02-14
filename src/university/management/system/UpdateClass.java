package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateClass extends JFrame implements ActionListener {

    JTextField tfcoursename, tfstudentlimit;
    JLabel labelcourseid;
    JButton submit, cancel;
    Choice ccourseid;

    UpdateClass() {

        setSize(700, 450);
        setLocation(350, 150);
        setLayout(null);

        JLabel heading = new JLabel("Update Class Details");
        heading.setBounds(50, 20, 400, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 25));
        add(heading);

        JLabel lblselect = new JLabel("Select Course ID");
        lblselect.setBounds(50, 100, 200, 30);
        add(lblselect);

        ccourseid = new Choice();
        ccourseid.setBounds(250, 100, 150, 30);
        add(ccourseid);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from class");
            while (rs.next()) {
                ccourseid.add(rs.getString("courseid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lblid = new JLabel("Course ID");
        lblid.setBounds(50, 150, 200, 30);
        add(lblid);

        labelcourseid = new JLabel();
        labelcourseid.setBounds(250, 150, 200, 30);
        add(labelcourseid);

        JLabel lblname = new JLabel("Course Name");
        lblname.setBounds(50, 200, 200, 30);
        add(lblname);

        tfcoursename = new JTextField();
        tfcoursename.setBounds(250, 200, 200, 30);
        add(tfcoursename);

        JLabel lbllimit = new JLabel("Student Limit");
        lbllimit.setBounds(50, 250, 200, 30);
        add(lbllimit);

        tfstudentlimit = new JTextField();
        tfstudentlimit.setBounds(250, 250, 200, 30);
        add(tfstudentlimit);

        loadData();

        ccourseid.addItemListener(e -> loadData());

        submit = new JButton("Update");
        submit.setBounds(150, 320, 120, 30);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(300, 320, 120, 30);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    void loadData() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from class where courseid='" + ccourseid.getSelectedItem() + "'");
            while (rs.next()) {
                labelcourseid.setText(rs.getString("courseid"));
                tfcoursename.setText(rs.getString("coursename"));
                tfstudentlimit.setText(rs.getString("studentlimit"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {

            String courseid = labelcourseid.getText();
            String name = tfcoursename.getText();
            String limit = tfstudentlimit.getText();

            try {
                Conn c = new Conn();
                c.s.executeUpdate("update class set coursename='" + name + "', studentlimit='" + limit + "' where courseid='" + courseid + "'");
                JOptionPane.showMessageDialog(null, "Class Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateClass();
    }
}