//package university.management.system;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.*;
//import com.toedter.calendar.JDateChooser;
//import java.awt.event.*;
//
//public class AddTeacher extends JFrame implements ActionListener{
//    
//    JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfx, tfxii, tfaadhar;
//    JLabel labelempId;
//    JDateChooser dcdob;
//    JComboBox cbcourse, cbbranch;
//    JButton submit, cancel;
//    
//    Random ran = new Random();
//    long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);
//    
//    AddTeacher() {
//        
//        setSize(900, 700);
//        setLocation(350, 50);
//        
//        setLayout(null);
//        
//        JLabel heading = new JLabel("New Teacher Details");
//        heading.setBounds(310, 30, 500, 50);
//        heading.setFont(new Font("serif", Font.BOLD, 30));
//        add(heading);
//        
//        JLabel lblname = new JLabel("Name");
//        lblname.setBounds(50, 150, 100, 30);
//        lblname.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblname);
//        
//        tfname = new JTextField();
//        tfname.setBounds(200, 150, 150, 30);
//        add(tfname);
//        
//        JLabel lblfname = new JLabel("Father's Name");
//        lblfname.setBounds(400, 150, 200, 30);
//        lblfname.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblfname);
//        
//        tffname = new JTextField();
//        tffname.setBounds(600, 150, 150, 30);
//        add(tffname);
//        
//        JLabel lblempId = new JLabel("Employee Id");
//        lblempId.setBounds(50, 200, 200, 30);
//        lblempId.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblempId);
//        
//        labelempId = new JLabel("101"+first4);
//        labelempId.setBounds(200, 200, 200, 30);
//        labelempId.setFont(new Font("serif", Font.BOLD, 20));
//        add(labelempId);
//        
//        JLabel lbldob = new JLabel("Date of Birth");
//        lbldob.setBounds(400, 200, 200, 30);
//        lbldob.setFont(new Font("serif", Font.BOLD, 20));
//        add(lbldob);
//        
//        dcdob = new JDateChooser();
//        dcdob.setBounds(600, 200, 150, 30);
//        add(dcdob);
//        
//        JLabel lbladdress = new JLabel("Address");
//        lbladdress.setBounds(50, 250, 200, 30);
//        lbladdress.setFont(new Font("serif", Font.BOLD, 20));
//        add(lbladdress);
//        
//        tfaddress = new JTextField();
//        tfaddress.setBounds(200, 250, 150, 30);
//        add(tfaddress);
//        
//        JLabel lblphone = new JLabel("Phone");
//        lblphone.setBounds(400, 250, 200, 30);
//        lblphone.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblphone);
//        
//        tfphone = new JTextField();
//        tfphone.setBounds(600, 250, 150, 30);
//        add(tfphone);
//        
//        JLabel lblemail = new JLabel("Email Id");
//        lblemail.setBounds(50, 300, 200, 30);
//        lblemail.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblemail);
//        
//        tfemail = new JTextField();
//        tfemail.setBounds(200, 300, 150, 30);
//        add(tfemail);
//        
//        JLabel lblx = new JLabel("Class X (%)");
//        lblx.setBounds(400, 300, 200, 30);
//        lblx.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblx);
//        
//        tfx = new JTextField();
//        tfx.setBounds(600, 300, 150, 30);
//        add(tfx);
//        
//        JLabel lblxii = new JLabel("Class XII (%)");
//        lblxii.setBounds(50, 350, 200, 30);
//        lblxii.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblxii);
//        
//        tfxii = new JTextField();
//        tfxii.setBounds(200, 350, 150, 30);
//        add(tfxii);
//        
//        JLabel lblaadhar = new JLabel("Aadhar Number");
//        lblaadhar.setBounds(400, 350, 200, 30);
//        lblaadhar.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblaadhar);
//        
//        tfaadhar = new JTextField();
//        tfaadhar.setBounds(600, 350, 150, 30);
//        add(tfaadhar);
//        
//        JLabel lblcourse = new JLabel("Qualification");
//        lblcourse.setBounds(50, 400, 200, 30);
//        lblcourse.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblcourse);
//        
//        String course[] = {"B.Tech", "BBA", "BCA", "Bsc", "Msc", "MBA", "MCA", "MCom", "MA", "BA"};
//        cbcourse = new JComboBox(course);
//        cbcourse.setBounds(200, 400, 150, 30);
//        cbcourse.setBackground(Color.WHITE);
//        add(cbcourse);
//        
//        JLabel lblbranch = new JLabel("Department");
//        lblbranch.setBounds(400, 400, 200, 30);
//        lblbranch.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblbranch);
//        
//        String branch[] = {"Computer Science", "Electronics", "Mechanical", "Civil", "IT"};
//        cbbranch = new JComboBox(branch);
//        cbbranch.setBounds(600, 400, 150, 30);
//        cbbranch.setBackground(Color.WHITE);
//        add(cbbranch);
//        
//        submit = new JButton("Submit");
//        submit.setBounds(250, 550, 120, 30);
//        submit.setBackground(Color.BLACK);
//        submit.setForeground(Color.WHITE);
//        submit.addActionListener(this);
//        submit.setFont(new Font("Tahoma", Font.BOLD, 15));
//        add(submit);
//        
//        cancel = new JButton("Cancel");
//        cancel.setBounds(450, 550, 120, 30);
//        cancel.setBackground(Color.BLACK);
//        cancel.setForeground(Color.WHITE);
//        cancel.addActionListener(this);
//        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
//        add(cancel);
//        
//        setVisible(true);
//    }
//    
//    public void actionPerformed(ActionEvent ae) {
//        if (ae.getSource() == submit) {
//            String name = tfname.getText();
//            String fname = tffname.getText();
//            String rollno = labelempId.getText();
//            String dob = ((JTextField) dcdob.getDateEditor().getUiComponent()).getText();
//            String address = tfaddress.getText();
//            String phone = tfphone.getText();
//            String email = tfemail.getText();
//            String x = tfx.getText();
//            String xii = tfxii.getText();
//            String aadhar = tfaadhar.getText();
//            String course = (String) cbcourse.getSelectedItem();
//            String branch = (String) cbbranch.getSelectedItem();
//            
//            try {
//                String query = "insert into teacher values('"+name+"', '"+fname+"', '"+rollno+"', '"+dob+"', '"+address+"', '"+phone+"', '"+email+"', '"+x+"', '"+xii+"', '"+aadhar+"', '"+course+"', '"+branch+"')";
//
//                Conn con = new Conn();
//                con.s.executeUpdate(query);
//                
//                JOptionPane.showMessageDialog(null, "Teacher Details Inserted Successfully");
//                setVisible(false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            setVisible(false);
//        }
//    }
//    
//    public static void main(String[] args) {
//        new AddTeacher();
//    }
//}

package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;

public class AddTeacher extends JFrame implements ActionListener {

    private JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfx, tfxii, tfaadhar;
    private JLabel labelempId;
    private JDateChooser dcdob;
    private JComboBox<String> cbeducation, cbdepartment, cbClassTeacher, cbRole;
    private JPasswordField tfPassword;
    private JButton submit, cancel;

    Random ran = new Random();
    long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);

    public AddTeacher() {

        setTitle("Add Teacher");
        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel heading = new JLabel("New Teacher Details");
        heading.setBounds(310, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // Name
        JLabel lblname = new JLabel("Name");
        lblname.setBounds(50, 150, 200, 30);
        lblname.setFont(new Font("serif", Font.BOLD, 20));
        add(lblname);

        tfname = new JTextField();
        tfname.setBounds(200, 150, 150, 30);
        add(tfname);

        // Father's Name
        JLabel lblfname = new JLabel("Father's Name");
        lblfname.setBounds(400, 150, 200, 30);
        lblfname.setFont(new Font("serif", Font.BOLD, 20));
        add(lblfname);

        tffname = new JTextField();
        tffname.setBounds(600, 150, 150, 30);
        add(tffname);

        // Employee Id
        JLabel lblempId = new JLabel("Employee Id");
        lblempId.setBounds(50, 200, 200, 30);
        lblempId.setFont(new Font("serif", Font.BOLD, 20));
        add(lblempId);

        labelempId = new JLabel("101" + first4);
        labelempId.setBounds(200, 200, 200, 30);
        labelempId.setFont(new Font("serif", Font.BOLD, 20));
        add(labelempId);

        // DOB
        JLabel lbldob = new JLabel("Date of Birth");
        lbldob.setBounds(400, 200, 200, 30);
        lbldob.setFont(new Font("serif", Font.BOLD, 20));
        add(lbldob);

        dcdob = new JDateChooser();
        dcdob.setBounds(600, 200, 150, 30);
        add(dcdob);

        // Address
        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(50, 250, 200, 30);
        lbladdress.setFont(new Font("serif", Font.BOLD, 20));
        add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(200, 250, 150, 30);
        add(tfaddress);

        // Phone
        JLabel lblphone = new JLabel("Phone");
        lblphone.setBounds(400, 250, 200, 30);
        lblphone.setFont(new Font("serif", Font.BOLD, 20));
        add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(600, 250, 150, 30);
        add(tfphone);

        // Email
        JLabel lblemail = new JLabel("Email Id");
        lblemail.setBounds(50, 300, 200, 30);
        lblemail.setFont(new Font("serif", Font.BOLD, 20));
        add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(200, 300, 150, 30);
        add(tfemail);

        // Password
        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(400, 300, 200, 30);
        lblpassword.setFont(new Font("serif", Font.BOLD, 20));
        add(lblpassword);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(600, 300, 150, 30);
        add(tfPassword);

        // Class X
        JLabel lblx = new JLabel("Class X (%)");
        lblx.setBounds(50, 350, 200, 30);
        lblx.setFont(new Font("serif", Font.BOLD, 20));
        add(lblx);

        tfx = new JTextField();
        tfx.setBounds(200, 350, 150, 30);
        add(tfx);

        // Class XII
        JLabel lblxii = new JLabel("Class XII (%)");
        lblxii.setBounds(400, 350, 200, 30);
        lblxii.setFont(new Font("serif", Font.BOLD, 20));
        add(lblxii);

        tfxii = new JTextField();
        tfxii.setBounds(600, 350, 150, 30);
        add(tfxii);

        // Aadhar
        JLabel lblaadhar = new JLabel("Aadhar Number");
        lblaadhar.setBounds(50, 400, 200, 30);
        lblaadhar.setFont(new Font("serif", Font.BOLD, 20));
        add(lblaadhar);

        tfaadhar = new JTextField();
        tfaadhar.setBounds(200, 400, 150, 30);
        add(tfaadhar);

        // Qualification
        JLabel lblcourse = new JLabel("Qualification");
        lblcourse.setBounds(400, 400, 200, 30);
        lblcourse.setFont(new Font("serif", Font.BOLD, 20));
        add(lblcourse);

        String education[] = {"B.Tech", "BSc", "MSc", "MBA", "MCA", "PhD"};
        cbeducation = new JComboBox<>(education);
        cbeducation.setBounds(600, 400, 150, 30);
        add(cbeducation);

        // Department
        JLabel lbldepartment = new JLabel("Department");
        lbldepartment.setBounds(50, 450, 200, 30);
        lbldepartment.setFont(new Font("serif", Font.BOLD, 20));
        add(lbldepartment);

        String dept[] = {"Computer Science", "Electronics", "Mechanical", "Civil", "IT"};
        cbdepartment = new JComboBox<>(dept);
        cbdepartment.setBounds(200, 450, 150, 30);
        add(cbdepartment);

        // Class Teacher
        JLabel lblclass = new JLabel("Class Teacher");
        lblclass.setBounds(400, 450, 200, 30);
        lblclass.setFont(new Font("serif", Font.BOLD, 20));
        add(lblclass);

        String classes[] = {"B.Tech", "BBA", "BCA", "BSc", "MSc", "MBA", "MCA", "MCom", "MA", "BA"};
        cbClassTeacher = new JComboBox<>(classes);
        cbClassTeacher.setBounds(600, 450, 150, 30);
        add(cbClassTeacher);

        // Role
        JLabel lblrole = new JLabel("Role");
        lblrole.setBounds(50, 500, 200, 30);
        lblrole.setFont(new Font("serif", Font.BOLD, 20));
        add(lblrole);

        String roles[] = {"Professor", "Assistant Professor", "Principal", "Dean", "Lab Instructor"};
        cbRole = new JComboBox<>(roles);
        cbRole.setBounds(200, 500, 150, 30);
        add(cbRole);

        // Buttons
        submit = new JButton("Submit");
        submit.setBounds(250, 580, 120, 40);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(450, 580, 120, 40);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {

            try {

                java.sql.Date sqlDate = new java.sql.Date(dcdob.getDate().getTime());

                Conn con = new Conn();

                String query = "INSERT INTO teacher (name, fname, empId, dob, address, phone, email, password, class_x, class_xii, aadhar, education, department, class_teacher, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.c.prepareStatement(query);

                ps.setString(1, tfname.getText());
                ps.setString(2, tffname.getText());
                ps.setString(3, labelempId.getText());
                ps.setDate(4, sqlDate);
                ps.setString(5, tfaddress.getText());
                ps.setString(6, tfphone.getText());
                ps.setString(7, tfemail.getText());
                ps.setString(8, new String(tfPassword.getPassword()));
                ps.setString(9, tfx.getText());
                ps.setString(10, tfxii.getText());
                ps.setString(11, tfaadhar.getText());
                ps.setString(12, (String) cbeducation.getSelectedItem());
                ps.setString(13, (String) cbdepartment.getSelectedItem());
                ps.setString(14, (String) cbClassTeacher.getSelectedItem());
                ps.setString(15, (String) cbRole.getSelectedItem());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Teacher Added Successfully");

                dispose();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Inserting Data");
            }

        } else {
            dispose();
        }
    }

    public static void main(String[] args) {
        new AddTeacher();
    }
}