package university.management.system;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

public class LeaveApprovalPage extends JFrame {

    private String empId;
    private JTable leaveTable;
    private DefaultTableModel model;

    public LeaveApprovalPage(String empId) {
        this.empId = empId;

        setTitle("Leave Approvals - " + empId);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("Pending Leave Applications", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Table
        String[] columns = {"Student Name", "Roll No", "Start Date", "End Date", "Reason", "Status", "View"};
        model = new DefaultTableModel(columns, 0);
        leaveTable = new JTable(model);
        leaveTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leaveTable.setRowHeight(30);
        leaveTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        leaveTable.getTableHeader().setBackground(new Color(30, 60, 120));
        leaveTable.getTableHeader().setForeground(Color.WHITE);

        // View button renderer & editor
        leaveTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        leaveTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(leaveTable);
        add(scroll, BorderLayout.CENTER);

        loadPendingLeaves();

        setVisible(true);
    }

    private void loadPendingLeaves() {
        try {
            Conn c = new Conn();

            // Get teacher's class_teacher
            String teacherQuery = "SELECT class_teacher FROM teacher WHERE empId = ?";
            PreparedStatement psTeacher = c.c.prepareStatement(teacherQuery);
            psTeacher.setString(1, empId);
            ResultSet rsTeacher = psTeacher.executeQuery();
            String classTeacher = "";
            if (rsTeacher.next()) {
                classTeacher = rsTeacher.getString("class_teacher");
            }

            if (classTeacher.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You are not assigned any class.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Get pending leaves from students of this class
            String query = "SELECT la.id, s.name, la.rollno, la.start_date, la.end_date, la.reason, la.status " +
                           "FROM leave_application la " +
                           "JOIN student s ON la.rollno = s.rollno " +
                           "WHERE s.class_name = ? AND la.status = 'Pending' " +
                           "ORDER BY la.applied_at DESC";

            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, classTeacher);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String roll = rs.getString("rollno");
                Date start = rs.getDate("start_date");
                Date end = rs.getDate("end_date");
                String reason = rs.getString("reason");
                String status = rs.getString("status");

                model.addRow(new Object[]{name, roll, start, end, reason, status, "View"});
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No pending leave applications.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading leaves", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom Button Renderer for "View"
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setBackground(new Color(0, 123, 255));
            setForeground(Color.WHITE);
            return this;
        }
    }

    // Custom Button Editor for "View"
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = leaveTable.getSelectedRow();
                if (row != -1) {
                    String name = (String) model.getValueAt(row, 0);
                    String roll = (String) model.getValueAt(row, 1);
                    Date start = (Date) model.getValueAt(row, 2);
                    Date end = (Date) model.getValueAt(row, 3);
                    String reason = (String) model.getValueAt(row, 4);

                    // Open view form with Approve/Reject
                    showLeaveViewForm(roll, name, start, end, reason, row);
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    // View Form with Approve/Reject buttons
    private void showLeaveViewForm(String roll, String name, Date start, Date end, String reason, int tableRow) {
        JDialog dialog = new JDialog(this, "View Leave - " + roll, true);
        dialog.setSize(650, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel title = new JLabel("Leave Application Details");
        title.setBounds(180, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        dialog.add(title);

        int y = 80;
        addLabel(dialog, "Student Name:", name, y); y += 40;
        addLabel(dialog, "Roll No:", roll, y); y += 40;
        addLabel(dialog, "Start Date:", start.toString(), y); y += 40;
        addLabel(dialog, "End Date:", end.toString(), y); y += 40;

        JLabel lblReason = new JLabel("Reason:");
        lblReason.setBounds(50, y, 100, 25);
        lblReason.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dialog.add(lblReason);

        JTextArea txtReason = new JTextArea(reason);
        txtReason.setBounds(50, y + 30, 550, 150);
        txtReason.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtReason.setEditable(false);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        txtReason.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        dialog.add(txtReason);

        // Approve Button
        JButton approveBtn = new JButton("Approve");
        approveBtn.setBounds(150, 430, 150, 45);
        approveBtn.setBackground(new Color(40, 167, 69));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        approveBtn.addActionListener(ev -> {
            updateLeaveStatus("Approved", "", tableRow);
            dialog.dispose();
        });
        dialog.add(approveBtn);

        // Reject Button
        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBounds(350, 430, 150, 45);
        rejectBtn.setBackground(new Color(220, 53, 69));
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rejectBtn.addActionListener(ev -> {
            String rejectReason = JOptionPane.showInputDialog(dialog,
                    "Enter Reject Reason (mandatory):",
                    "Reject Leave",
                    JOptionPane.QUESTION_MESSAGE);

            if (rejectReason != null && !rejectReason.trim().isEmpty()) {
                updateLeaveStatus("Rejected", rejectReason.trim(), tableRow);
                dialog.dispose();
            } else if (rejectReason != null) {
                JOptionPane.showMessageDialog(dialog, "Reject reason is required!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(rejectBtn);

        dialog.setVisible(true);
    }

    private void addLabel(JDialog dialog, String labelText, String value, int y) {
        JLabel lbl = new JLabel(labelText + " " + value);
        lbl.setBounds(50, y, 500, 25);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dialog.add(lbl);
    }

    private void updateLeaveStatus(String newStatus, String rejectReason, int tableRow) {
        try {
            Conn c = new Conn();
            String query = "UPDATE leave_application SET status = ?, reject_reason = ? " +
                           "WHERE rollno = ? AND status = 'Pending'";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, newStatus);
            ps.setString(2, rejectReason.isEmpty() ? null : rejectReason);
            ps.setString(3, (String) model.getValueAt(tableRow, 1)); // rollno

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Leave " + newStatus.toLowerCase() + " successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                loadPendingLeaves(); // Refresh table
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LeaveApprovalPage("101455");
    }
}