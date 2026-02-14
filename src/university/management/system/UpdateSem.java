package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateSem extends JFrame implements ActionListener {

    JTextField tfsemname;
    JLabel labelsemid;
    JButton submit, cancel;
    Choice csemid;

    UpdateSem() {

        setSize(600, 400);
        setLocation(400, 200);
        setLayout(null);

        JLabel heading = new JLabel("Update Semester Details");
        heading.setBounds(50, 20, 400, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 25));
        add(heading);

        JLabel lblselect = new JLabel("Select Semester ID");
        lblselect.setBounds(50, 100, 200, 30);
        add(lblselect);

        csemid = new Choice();
        csemid.setBounds(250, 100, 150, 30);
        add(csemid);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from semester");
            while (rs.next()) {
                csemid.add(rs.getString("semid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lblid = new JLabel("Semester ID");
        lblid.setBounds(50, 150, 200, 30);
        add(lblid);

        labelsemid = new JLabel();
        labelsemid.setBounds(250, 150, 200, 30);
        add(labelsemid);

        JLabel lblname = new JLabel("Semester Name");
        lblname.setBounds(50, 200, 200, 30);
        add(lblname);

        tfsemname = new JTextField();
        tfsemname.setBounds(250, 200, 150, 30);
        add(tfsemname);

        loadData();

        csemid.addItemListener(e -> loadData());

        submit = new JButton("Update");
        submit.setBounds(150, 280, 120, 30);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(300, 280, 120, 30);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    void loadData() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from semester where semid='" + csemid.getSelectedItem() + "'");
            while (rs.next()) {
                labelsemid.setText(rs.getString("semid"));
                tfsemname.setText(rs.getString("sem_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {

            String semid = labelsemid.getText();
            String semname = tfsemname.getText();

            try {
                Conn c = new Conn();
                c.s.executeUpdate("update semester set sem_name='" + semname + "' where semid='" + semid + "'");
                JOptionPane.showMessageDialog(null, "Semester Updated Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateSem();
    }
}