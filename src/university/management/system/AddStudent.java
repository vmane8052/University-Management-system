//package university.management.system;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.*;
//import com.toedter.calendar.JDateChooser;
//import java.awt.event.*;
//
//public class AddStudent extends JFrame implements ActionListener{
//    
//    JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfx, tfxii, tfaadhar;
//    JLabel labelrollno;
//    JDateChooser dcdob;
//    JComboBox cbcourse, cbbranch;
//    JButton submit, cancel;
//    
//    Random ran = new Random();
//    long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);
//    
//    AddStudent() {
//        
//        setSize(900, 700);
//        setLocation(350, 50);
//        
//        setLayout(null);
//        
//        JLabel heading = new JLabel("New Student Details");
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
//        JLabel lblrollno = new JLabel("Roll Number");
//        lblrollno.setBounds(50, 200, 200, 30);
//        lblrollno.setFont(new Font("serif", Font.BOLD, 20));
//        add(lblrollno);
//        
//        labelrollno = new JLabel("1533"+first4);
//        labelrollno.setBounds(200, 200, 200, 30);
//        labelrollno.setFont(new Font("serif", Font.BOLD, 20));
//        add(labelrollno);
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
//        JLabel lblcourse = new JLabel("Course");
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
//        JLabel lblbranch = new JLabel("Branch");
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
//            String rollno = labelrollno.getText();
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
//                String query = "insert into student values('"+name+"', '"+fname+"', '"+rollno+"', '"+dob+"', '"+address+"', '"+phone+"', '"+email+"', '"+x+"', '"+xii+"', '"+aadhar+"', '"+course+"', '"+branch+"')";
//
//                Conn con = new Conn();
//                con.s.executeUpdate(query);
//                
//                JOptionPane.showMessageDialog(null, "Student Details Inserted Successfully");
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
//        new AddStudent();
//    }
//}


package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;

public class AddStudent extends JFrame implements ActionListener {

    private JTextField tfname, tffname, tfaddress, tfphone, tfemail, tfx, tfxii, tfaadhar;
    private JPasswordField tfpassword;   // 🔥 Password Field
    private JLabel labelrollno;
    private JDateChooser dcdob;
    private JComboBox<String> cbcourse, cbbranch, cbClassName, cbSemester;
    private JButton submit, cancel;

    Random ran = new Random();
    long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);

    public AddStudent() {

        setTitle("Add Student");
        setSize(900, 750);
        setLocation(350, 30);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel heading = new JLabel("New Student Details");
        heading.setBounds(300, 20, 500, 40);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        addLabel("Name", 50, 100);
        tfname = addTextField(200, 100);

        addLabel("Father's Name", 400, 100);
        tffname = addTextField(600, 100);

        addLabel("Roll Number", 50, 150);
        labelrollno = new JLabel("1533" + first4);
        labelrollno.setBounds(200, 150, 200, 30);
        labelrollno.setFont(new Font("serif", Font.BOLD, 18));
        add(labelrollno);

        addLabel("Date of Birth", 400, 150);
        dcdob = new JDateChooser();
        dcdob.setBounds(600, 150, 150, 30);
        add(dcdob);

        addLabel("Address", 50, 200);
        tfaddress = addTextField(200, 200);

        addLabel("Phone", 400, 200);
        tfphone = addTextField(600, 200);

        addLabel("Email", 50, 250);
        tfemail = addTextField(200, 250);

        addLabel("Class X (%)", 400, 250);
        tfx = addTextField(600, 250);

        addLabel("Class XII (%)", 50, 300);
        tfxii = addTextField(200, 300);

        addLabel("Aadhar", 400, 300);
        tfaadhar = addTextField(600, 300);

        addLabel("Course", 50, 350);
        String course[] = {"B.Tech", "BBA", "BCA", "BSc", "MSc", "MBA", "MCA"};
        cbcourse = new JComboBox<>(course);
        cbcourse.setBounds(200, 350, 150, 30);
        add(cbcourse);

        addLabel("Branch", 400, 350);
        String branch[] = {"Computer Science", "Electronics", "Mechanical", "Civil", "IT"};
        cbbranch = new JComboBox<>(branch);
        cbbranch.setBounds(600, 350, 150, 30);
        add(cbbranch);

        addLabel("Class Name", 50, 400);
        cbClassName = new JComboBox<>();
        cbClassName.setBounds(200, 400, 150, 30);
        loadClassNames();
        add(cbClassName);

        addLabel("Semester", 400, 400);
        cbSemester = new JComboBox<>();
        cbSemester.setBounds(600, 400, 150, 30);
        loadSemesters();
        add(cbSemester);

        // 🔥 PASSWORD FIELD
        addLabel("Password", 50, 450);
        tfpassword = new JPasswordField();
        tfpassword.setBounds(200, 450, 150, 30);
        add(tfpassword);

        submit = new JButton("Submit");
        submit.setBounds(250, 550, 120, 40);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(450, 550, 120, 40);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("serif", Font.BOLD, 18));
        add(label);
    }

    private JTextField addTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 150, 30);
        add(field);
        return field;
    }

    private void loadClassNames() {
        try {
            Conn con = new Conn();
            ResultSet rs = con.s.executeQuery("SELECT coursename FROM class");
            while (rs.next()) {
                cbClassName.addItem(rs.getString("coursename"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSemesters() {
        try {
            Conn con = new Conn();
            ResultSet rs = con.s.executeQuery("SELECT sem_name FROM semester");
            while (rs.next()) {
                cbSemester.addItem(rs.getString("sem_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {

            try {

                java.sql.Date sqlDate = new java.sql.Date(dcdob.getDate().getTime());
                String password = new String(tfpassword.getPassword());

                Conn con = new Conn();

                String query = "INSERT INTO student (name, fname, rollno, dob, address, phone, email, class_x, class_xii, aadhar, course, branch, class_name, semester_name, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = con.c.prepareStatement(query);

                ps.setString(1, tfname.getText());
                ps.setString(2, tffname.getText());
                ps.setString(3, labelrollno.getText());
                ps.setDate(4, sqlDate);
                ps.setString(5, tfaddress.getText());
                ps.setString(6, tfphone.getText());
                ps.setString(7, tfemail.getText());
                ps.setString(8, tfx.getText());
                ps.setString(9, tfxii.getText());
                ps.setString(10, tfaadhar.getText());
                ps.setString(11, (String) cbcourse.getSelectedItem());
                ps.setString(12, (String) cbbranch.getSelectedItem());
                ps.setString(13, (String) cbClassName.getSelectedItem());
                ps.setString(14, (String) cbSemester.getSelectedItem());
                ps.setString(15, password);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student Added Successfully");
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
        new AddStudent();
    }
}