package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class TeacherDetails extends JFrame implements ActionListener {

    Choice cEmpId;
    JTable table;
    JButton search, print, update, add, cancel;

    TeacherDetails() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Search by Employee Id");
        heading.setBounds(20, 20, 150, 20);
        add(heading);

        cEmpId = new Choice();
        cEmpId.setBounds(180, 20, 150, 20);
        add(cEmpId);

        // Populate Emp IDs
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT empId FROM teacher ORDER BY empId");
            while (rs.next()) {
                cEmpId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table = new JTable();

        // Load only necessary columns
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery(
                "SELECT name, empId, fname, department, education, email, phone, aadhar " +
                "FROM teacher ORDER BY empId"
            );
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 900, 600);
        add(jsp);

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
            String selectedEmpId = cEmpId.getSelectedItem();
            if (selectedEmpId == null || selectedEmpId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an Employee ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String query = "SELECT name, empId, fname, department, education, email, phone, aadhar " +
                           "FROM teacher WHERE empId = ?";

            try {
                Conn c = new Conn();
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, selectedEmpId);
                ResultSet rs = ps.executeQuery();
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error searching teacher", "Error", JOptionPane.ERROR_MESSAGE);
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
            new AddTeacher();
        } 
        else if (ae.getSource() == update) {
            setVisible(false);
            new UpdateTeacher();
        } 
        else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new TeacherDetails();
    }
}