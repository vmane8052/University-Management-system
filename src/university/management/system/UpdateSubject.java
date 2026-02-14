package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateSubject extends JFrame implements ActionListener {

    JTextField tfsubjectname, tfinmin, tfinmax, tfexmin, tfexmax;
    JLabel labelsubjectid;
    Choice csubjectid, ctype;
    JButton submit, cancel;

    UpdateSubject() {

        setSize(800, 550);
        setLocation(300, 100);
        setLayout(null);

        JLabel heading = new JLabel("Update Subject Details");
        heading.setBounds(50, 20, 400, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 25));
        add(heading);

        JLabel lblselect = new JLabel("Select Subject ID");
        lblselect.setBounds(50, 100, 200, 30);
        add(lblselect);

        csubjectid = new Choice();
        csubjectid.setBounds(250, 100, 150, 30);
        add(csubjectid);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from subject1");
            while (rs.next()) {
                csubjectid.add(rs.getString("subject_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lblid = new JLabel("Subject ID");
        lblid.setBounds(50, 150, 200, 30);
        add(lblid);

        labelsubjectid = new JLabel();
        labelsubjectid.setBounds(250, 150, 200, 30);
        add(labelsubjectid);

        JLabel lblname = new JLabel("Subject Name");
        lblname.setBounds(50, 200, 200, 30);
        add(lblname);

        tfsubjectname = new JTextField();
        tfsubjectname.setBounds(250, 200, 200, 30);
        add(tfsubjectname);

        JLabel lbltype = new JLabel("Subject Type");
        lbltype.setBounds(50, 250, 200, 30);
        add(lbltype);

        ctype = new Choice();
        ctype.add("Theory");
        ctype.add("Practical");
        ctype.setBounds(250, 250, 150, 30);
        add(ctype);

        JLabel lblinmin = new JLabel("Internal Min");
        lblinmin.setBounds(50, 300, 200, 30);
        add(lblinmin);

        tfinmin = new JTextField();
        tfinmin.setBounds(250, 300, 100, 30);
        add(tfinmin);

        JLabel lblinmax = new JLabel("Internal Max");
        lblinmax.setBounds(400, 300, 200, 30);
        add(lblinmax);

        tfinmax = new JTextField();
        tfinmax.setBounds(600, 300, 100, 30);
        add(tfinmax);

        JLabel lblexmin = new JLabel("External Min");
        lblexmin.setBounds(50, 350, 200, 30);
        add(lblexmin);

        tfexmin = new JTextField();
        tfexmin.setBounds(250, 350, 100, 30);
        add(tfexmin);

        JLabel lblexmax = new JLabel("External Max");
        lblexmax.setBounds(400, 350, 200, 30);
        add(lblexmax);

        tfexmax = new JTextField();
        tfexmax.setBounds(600, 350, 100, 30);
        add(tfexmax);

        loadData();

        csubjectid.addItemListener(e -> loadData());

        submit = new JButton("Update");
        submit.setBounds(250, 420, 120, 30);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(450, 420, 120, 30);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    void loadData() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from subject1 where subject_id='" + csubjectid.getSelectedItem() + "'");
            while (rs.next()) {
                labelsubjectid.setText(rs.getString("subject_id"));
                tfsubjectname.setText(rs.getString("subject_name"));
                tfinmin.setText(rs.getString("internal_min"));
                tfinmax.setText(rs.getString("internal_max"));
                tfexmin.setText(rs.getString("external_min"));
                tfexmax.setText(rs.getString("external_max"));
                ctype.select(rs.getString("subject_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {

            String id = labelsubjectid.getText();
            String name = tfsubjectname.getText();
            String type = ctype.getSelectedItem();
            String inmin = tfinmin.getText();
            String inmax = tfinmax.getText();
            String exmin = tfexmin.getText();
            String exmax = tfexmax.getText();

            try {
                Conn c = new Conn();
                c.s.executeUpdate("update subject1 set subject_name='" + name + "', subject_type='" + type + "', internal_min='" + inmin + "', internal_max='" + inmax + "', external_min='" + exmin + "', external_max='" + exmax + "' where subject_id='" + id + "'");
                JOptionPane.showMessageDialog(null, "Subject Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateSubject();
    }
}