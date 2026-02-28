package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class StudentDetails extends JFrame implements ActionListener {

    Choice crollno;
    JTable table;
    JButton search, print, update, add, cancel;

    StudentDetails() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Heading
        JLabel heading = new JLabel("Search by Roll Number");
        heading.setBounds(20, 20, 150, 20);
        add(heading);

        // Roll No dropdown
        crollno = new Choice();
        crollno.setBounds(180, 20, 150, 20);
        add(crollno);

        // Populate roll numbers
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT rollno FROM student ORDER BY rollno");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Table (only necessary columns)
        table = new JTable();

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                "SELECT name, rollno, fname, class_name, branch, phone, email, aadhar " +
                "FROM student ORDER BY rollno"
            );
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 900, 600);
        add(jsp);

        // Buttons (same positions)
        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        add = new JButton("Add");
        add.setBounds(220, 70, 80, 20);
        add.addActionListener(this);
        add(add);

        update = new JButton("Update");
        update.setBounds(320, 70, 80, 20);
        update.addActionListener(this);
        add(update);

        cancel = new JButton("Cancel");
        cancel.setBounds(420, 70, 80, 20);
        cancel.addActionListener(this);
        add(cancel);

        setSize(900, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            String selectedRoll = crollno.getSelectedItem();
            if (selectedRoll == null || selectedRoll.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a roll number", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String query = "SELECT name, rollno, fname, class_name, branch, phone, email, aadhar " +
                           "FROM student WHERE rollno = ?";

            try {
                Conn c = new Conn();
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, selectedRoll);
                ResultSet rs = ps.executeQuery();
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error searching student", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Print failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (ae.getSource() == add) {
            setVisible(false);
            new AddStudent();
        } 
        else if (ae.getSource() == update) {
            setVisible(false);
            new UpdateStudent();
        } 
        else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new StudentDetails();
    }
}