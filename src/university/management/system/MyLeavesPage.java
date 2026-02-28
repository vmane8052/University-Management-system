package university.management.system;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class MyLeavesPage extends JFrame {

    private String rollno;
    private JTable table;
    private DefaultTableModel model;

    public MyLeavesPage(String rollno) {
        this.rollno = rollno;

        setTitle("My Applied Leaves - " + rollno);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));

        // Heading
        JLabel heading = new JLabel("My Applied Leaves", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Table
        String[] columns = {"Start Date", "End Date", "Reason", "Status", "Reject Reason", "Applied On", "Actions"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(30, 60, 120));
        table.getTableHeader().setForeground(Color.WHITE);

        // Custom renderer for Status column (color)
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());

        // Custom renderer & editor for Actions column (Edit & Cancel buttons)
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ActionEditor(new JCheckBox(), this)); // Pass outer instance

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        loadMyLeaves();

        setVisible(true);
    }

    private void loadMyLeaves() {
        try {
            Conn c = new Conn();
            String query = "SELECT id, start_date, end_date, reason, status, reject_reason, applied_at " +
                           "FROM leave_application WHERE rollno = ? " +
                           "ORDER BY applied_at DESC";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                Date start = rs.getDate("start_date");
                Date end = rs.getDate("end_date");
                String reason = rs.getString("reason");
                String status = rs.getString("status");
                String rejectReason = rs.getString("reject_reason") != null ? rs.getString("reject_reason") : "-";
                Timestamp applied = rs.getTimestamp("applied_at");

                model.addRow(new Object[]{start, end, reason, status, rejectReason, applied, "Actions"});
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "You have not applied for any leaves yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading leaves:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom Renderer for Status column (color)
    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) value;

            if ("Pending".equals(status)) {
                c.setForeground(new Color(255, 165, 0)); // Orange
            } else if ("Approved".equals(status)) {
                c.setForeground(new Color(40, 167, 69)); // Green
            } else if ("Rejected".equals(status)) {
                c.setForeground(new Color(220, 53, 69)); // Red
            } else {
                c.setForeground(Color.GRAY);
            }

            return c;
        }
    }

    // Custom Renderer for Actions column
    class ActionRenderer extends JButton implements TableCellRenderer {
        public ActionRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            String status = (String) table.getValueAt(row, 3);
            setText("Actions");
            if ("Pending".equals(status)) {
                setBackground(new Color(0, 123, 255));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.LIGHT_GRAY);
                setForeground(Color.DARK_GRAY);
                setEnabled(false);
            }
            return this;
        }
    }

    // Custom Editor for Actions column
    class ActionEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;
        private final MyLeavesPage outer;

        public ActionEditor(JCheckBox checkBox, MyLeavesPage outer) {
            super(checkBox);
            this.outer = outer;
            button = new JButton("Actions");
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            String status = (String) table.getValueAt(row, 3);
            if ("Pending".equals(status)) {
                button.setText("View / Edit / Cancel");
                button.setBackground(new Color(0, 123, 255));
                button.setForeground(Color.WHITE);
            } else {
                button.setText("No Action");
                button.setBackground(Color.LIGHT_GRAY);
                button.setForeground(Color.DARK_GRAY);
            }
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = outer.table.getSelectedRow();
                if (row != -1) {
                    String status = (String) outer.model.getValueAt(row, 3);
                    if ("Pending".equals(status)) {
                        outer.showActionsDialog(row);
                    }
                }
            }
            isPushed = false;
            return "Actions";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    // Dialog with View, Edit, Cancel options
    private void showActionsDialog(int row) {
        JDialog dialog = new JDialog(this, "Manage Leave Application", true);
        dialog.setSize(650, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        Date start = (Date) model.getValueAt(row, 0);
        Date end = (Date) model.getValueAt(row, 1);
        String reason = (String) model.getValueAt(row, 2);

        JLabel title = new JLabel("Leave Application Details");
        title.setBounds(180, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        dialog.add(title);

        int y = 80;
        addLabel(dialog, "Start Date:", start.toString(), y); y += 40;
        addLabel(dialog, "End Date:", end.toString(), y); y += 40;
        JLabel lblReason = new JLabel("Reason:");
        lblReason.setBounds(50, y, 100, 25);
        lblReason.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dialog.add(lblReason);

        JTextArea txtReason = new JTextArea(reason);
        txtReason.setBounds(50, y + 30, 550, 120);
        txtReason.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtReason.setEditable(false);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        txtReason.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        dialog.add(txtReason);

        // Edit Button
        JButton editBtn = new JButton("Edit Leave");
        editBtn.setBounds(150, 430, 150, 45);
        editBtn.setBackground(new Color(255, 193, 7));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(ev -> {
            editLeaveApplication(row, dialog);
        });
        dialog.add(editBtn);

        // Cancel Button
        JButton cancelBtn = new JButton("Cancel Leave");
        cancelBtn.setBounds(350, 430, 150, 45);
        cancelBtn.setBackground(new Color(220, 53, 69));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(ev -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to cancel this leave application?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cancelLeave(row);
                dialog.dispose();
            }
        });
        dialog.add(cancelBtn);

        dialog.setVisible(true);
    }

    private void addLabel(JDialog dialog, String labelText, String value, int y) {
        JLabel lbl = new JLabel(labelText + " " + value);
        lbl.setBounds(50, y, 500, 25);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dialog.add(lbl);
    }

    // Edit Leave Application
    private void editLeaveApplication(int row, JDialog parentDialog) {
        JDialog editDialog = new JDialog(parentDialog, "Edit Leave Application", true);
        editDialog.setSize(600, 500);
        editDialog.setLocationRelativeTo(parentDialog);
        editDialog.setLayout(null);

        Date oldStart = (Date) model.getValueAt(row, 0);
        Date oldEnd = (Date) model.getValueAt(row, 1);
        String oldReason = (String) model.getValueAt(row, 2);

        JLabel title = new JLabel("Edit Leave Application");
        title.setBounds(180, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        editDialog.add(title);

        JLabel lblStart = new JLabel("Start Date:");
        lblStart.setBounds(50, 80, 150, 25);
        editDialog.add(lblStart);

        JDateChooser startChooser = new JDateChooser();
        startChooser.setBounds(200, 80, 300, 30);
        startChooser.setDate(oldStart);
        editDialog.add(startChooser);

        JLabel lblEnd = new JLabel("End Date:");
        lblEnd.setBounds(50, 130, 150, 25);
        editDialog.add(lblEnd);

        JDateChooser endChooser = new JDateChooser();
        endChooser.setBounds(200, 130, 300, 30);
        endChooser.setDate(oldEnd);
        editDialog.add(endChooser);

        JLabel lblReason = new JLabel("Reason:");
        lblReason.setBounds(50, 180, 150, 25);
        editDialog.add(lblReason);

        JTextArea txtReason = new JTextArea(oldReason);
        txtReason.setBounds(50, 210, 500, 150);
        txtReason.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        txtReason.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        editDialog.add(txtReason);

        JButton updateBtn = new JButton("Update Leave");
        updateBtn.setBounds(200, 400, 200, 45);
        updateBtn.setBackground(new Color(40, 167, 69));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(ev -> {
            Date newStart = startChooser.getDate();
            Date newEnd = endChooser.getDate();
            String newReason = txtReason.getText().trim();

            // Add validations (similar to apply)
            if (newStart == null || newEnd == null || newReason.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate startLocal = newStart.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocal = newEnd.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();

            if (startLocal.isAfter(endLocal)) {
                JOptionPane.showMessageDialog(editDialog, "Start date must be before end date!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (startLocal.isBefore(today)) {
                JOptionPane.showMessageDialog(editDialog, "Cannot edit to past dates!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // More validations... (add all from ApplyLeavePage if needed)

            // Save updates
            updateLeave(row, newStart, newEnd, newReason);
            editDialog.dispose();
        });
        editDialog.add(updateBtn);

        editDialog.setVisible(true);
    }

    private void updateLeave(int row, Date newStart, Date newEnd, String newReason) {
        try {
            Conn c = new Conn();
            String query = "UPDATE leave_application SET start_date = ?, end_date = ?, reason = ? " +
                           "WHERE rollno = ? AND start_date = ? AND end_date = ? AND status = 'Pending'";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(newStart.getTime()));
            ps.setDate(2, new java.sql.Date(newEnd.getTime()));
            ps.setString(3, newReason);
            ps.setString(4, rollno);
            ps.setDate(5, (java.sql.Date) model.getValueAt(row, 0)); // old start
            ps.setDate(6, (java.sql.Date) model.getValueAt(row, 1)); // old end

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Leave updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMyLeaves(); // Refresh
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating leave", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelLeave(int row) {
        try {
            Conn c = new Conn();
            String query = "DELETE FROM leave_application WHERE rollno = ? AND start_date = ? AND end_date = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ps.setDate(2, (java.sql.Date) model.getValueAt(row, 0));
            ps.setDate(3, (java.sql.Date) model.getValueAt(row, 1));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Leave cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMyLeaves(); // Refresh
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cancelling leave", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new MyLeavesPage("15333936");
    }
}