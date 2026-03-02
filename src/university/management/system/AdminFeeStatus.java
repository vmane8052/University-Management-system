package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class AdminFeeStatus extends JFrame implements Printable {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public AdminFeeStatus() {
        setTitle("Admin - All Students Fee Status");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Heading
        JLabel heading = new JLabel("Students Fee Status Overview", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        add(heading, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        searchPanel.setBackground(new Color(245, 245, 245));

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSearch.setHorizontalAlignment(SwingConstants.RIGHT);

        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchField.setToolTipText("Search by Roll No or Name");

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBackground(new Color(0, 123, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> loadFeeStatus(searchField.getText().trim()));

        JButton btnRefresh = new JButton("Refresh All");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(new Color(40, 167, 69));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> {
            searchField.setText("");
            loadFeeStatus("");
        });

        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);

        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Roll No", "Student Name", "Class/Course", "Semester", "Total Fee", "Paid Amount", "Pending Amount", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(30, 60, 120));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Status column color
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean focus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, isSel, focus, r, c);
                String status = val.toString();
                if (status.equals("Paid")) {
                    comp.setForeground(new Color(40, 167, 69));
                } else if (status.equals("Partially Paid")) {
                    comp.setForeground(new Color(255, 193, 7));
                } else if (status.equals("Unpaid")) {
                    comp.setForeground(new Color(220, 53, 69));
                } else {
                    comp.setForeground(Color.GRAY);
                }
                return comp;
            }
        });

        // Bottom buttons panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JButton btnPrintTable = new JButton("Print This Page");
        btnPrintTable.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPrintTable.setBackground(new Color(0, 123, 255));
        btnPrintTable.setForeground(Color.WHITE);
        btnPrintTable.addActionListener(e -> printTable());
        bottomPanel.add(btnPrintTable);

        JButton btnPrintReceipt = new JButton("Print Selected Student's Receipt");
        btnPrintReceipt.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPrintReceipt.setBackground(new Color(40, 167, 69));
        btnPrintReceipt.setForeground(Color.WHITE);
        btnPrintReceipt.addActionListener(e -> openReceiptForSelected());
        bottomPanel.add(btnPrintReceipt);

        add(bottomPanel, BorderLayout.SOUTH);

        loadFeeStatus("");
        setVisible(true);
    }

    private void loadFeeStatus(String searchTerm) {
        model.setRowCount(0);
        try {
            Conn c = new Conn();

            StringBuilder sb = new StringBuilder(
                "SELECT s.rollno, s.name, s.class_name, s.semester_name, " +
                "COALESCE(fa.total_fee, 0) AS total_fee, " +
                "COALESCE(SUM(fp.amount_paid), 0) AS paid_amount " +
                "FROM student s " +
                "LEFT JOIN class cl ON s.class_name = cl.coursename " +
                "LEFT JOIN fee_assignment fa ON cl.courseid = fa.courseid " +
                "   AND (SELECT semid FROM semester WHERE sem_name = s.semester_name LIMIT 1) = fa.semid " +
                "LEFT JOIN fee_payment fp ON s.rollno = fp.rollno " +
                "   AND fp.courseid = cl.courseid " +
                "   AND fp.semid = (SELECT semid FROM semester WHERE sem_name = s.semester_name LIMIT 1) "
            );

            if (!searchTerm.isEmpty()) {
                sb.append("WHERE s.rollno LIKE ? OR s.name LIKE ? ");
            }

            sb.append("GROUP BY s.rollno, s.name, s.class_name, s.semester_name, fa.total_fee ");
            sb.append("ORDER BY s.rollno");

            PreparedStatement ps = c.c.prepareStatement(sb.toString());

            if (!searchTerm.isEmpty()) {
                String like = "%" + searchTerm + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String roll = rs.getString("rollno");
                String name = rs.getString("name") != null ? rs.getString("name") : "N/A";
                String cls  = rs.getString("class_name") != null ? rs.getString("class_name") : "N/A";
                String sem  = rs.getString("semester_name") != null ? rs.getString("semester_name") : "N/A";

                double total   = rs.getDouble("total_fee");
                double paid    = rs.getDouble("paid_amount");
                double pending = total - paid;

                String totalStr   = total > 0 ? String.format("₹ %,.2f", total) : "Not Assigned";
                String paidStr    = paid > 0  ? String.format("₹ %,.2f", paid)   : "₹ 0.00";
                String pendingStr = pending > 0 ? String.format("₹ %,.2f", pending) : "₹ 0.00";

                String status = (total == 0) ? "No Fee Assigned" :
                                (pending <= 0) ? "Paid" :
                                (paid > 0) ? "Partially Paid" : "Unpaid";

                model.addRow(new Object[]{roll, name, cls, sem, totalStr, paidStr, pendingStr, status});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading fee status:\n" + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printTable() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this,
                    "Table sent to printer successfully.",
                    "Print Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this,
                    "Printing failed:\n" + ex.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Scale to fit better on paper (optional adjustment)
        double scale = 0.85;
        g2d.scale(scale, scale);

        // Print the table (or entire content)
        getContentPane().printAll(g2d);

        return Printable.PAGE_EXISTS;
    }

    private void openReceiptForSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student row first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) model.getValueAt(row, 7);
        if (!status.equals("Paid") && !status.equals("Partially Paid")) {
            JOptionPane.showMessageDialog(this,
                "No payment record available for this student.",
                "No Receipt", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rollno = (String) model.getValueAt(row, 0);
        String name   = (String) model.getValueAt(row, 1);
        String cls    = (String) model.getValueAt(row, 2);
        String sem    = (String) model.getValueAt(row, 3);

        try {
            Conn c = new Conn();
            String q = "SELECT transaction_id, amount_paid FROM fee_payment " +
                       "WHERE rollno = ? ORDER BY payment_date DESC LIMIT 1";
            PreparedStatement ps = c.c.prepareStatement(q);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String transId = rs.getString("transaction_id");
                double amount = rs.getDouble("amount_paid");
                new FeeReceipt(rollno, name, cls, sem, amount, transId);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No payment record found for this student.",
                    "No Data", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error fetching receipt:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminFeeStatus());
    }
}