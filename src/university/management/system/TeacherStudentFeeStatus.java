package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TeacherStudentFeeStatus extends JFrame implements Printable {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private String teacherEmpId;
    private String teacherClass;

    public TeacherStudentFeeStatus(String empId) {
        this.teacherEmpId = empId;
        setTitle("My Class Students Fee Status");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        fetchTeacherClass();

        // Heading
        JLabel heading = new JLabel("Fee Status - Class: " + (teacherClass != null ? teacherClass : "Not Assigned"), SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
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
        String[] columns = {"Roll No", "Student Name", "Class", "Semester", "Total Fee", "Paid Amount", "Pending", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(30);
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

        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JButton btnPrintPage = new JButton("Print Full List");
        btnPrintPage.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPrintPage.setBackground(new Color(0, 123, 255));
        btnPrintPage.setForeground(Color.WHITE);
        btnPrintPage.addActionListener(e -> printFullPage());
        bottomPanel.add(btnPrintPage);

        JButton btnPrintReceipt = new JButton("Print Receipt of Selected Student");
        btnPrintReceipt.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPrintReceipt.setBackground(new Color(40, 167, 69));
        btnPrintReceipt.setForeground(Color.WHITE);
        btnPrintReceipt.addActionListener(e -> openAndPrintReceipt());
        bottomPanel.add(btnPrintReceipt);

        add(bottomPanel, BorderLayout.SOUTH);

        loadFeeStatus("");
        setVisible(true);
    }

    private void fetchTeacherClass() {
        Conn c = null;
        try {
            c = new Conn();
            String query = "SELECT class_teacher FROM teacher WHERE empId = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, teacherEmpId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                teacherClass = rs.getString("class_teacher");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not fetch teacher class.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (c != null) {
                try {
                    if (c.c != null) c.c.close();
                    // if you have Statement s in Conn class, close it too
                    // if (c.s != null) c.s.close();
                } catch (Exception ignored) {}
            }
        }
    }

    private void loadFeeStatus(String searchTerm) {
        model.setRowCount(0);
        if (teacherClass == null || teacherClass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No class assigned to you.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Conn c = null;
        try {
            c = new Conn();

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
                "   AND fp.semid = (SELECT semid FROM semester WHERE sem_name = s.semester_name LIMIT 1) " +
                "WHERE s.class_name = ? "
            );

            if (!searchTerm.isEmpty()) {
                sb.append("AND (s.rollno LIKE ? OR s.name LIKE ?) ");
            }

            sb.append("GROUP BY s.rollno, s.name, s.class_name, s.semester_name, fa.total_fee ");
            sb.append("ORDER BY s.rollno");

            PreparedStatement ps = c.c.prepareStatement(sb.toString());
            ps.setString(1, teacherClass);

            if (!searchTerm.isEmpty()) {
                String like = "%" + searchTerm + "%";
                ps.setString(2, like);
                ps.setString(3, like);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double total = rs.getDouble("total_fee");
                double paid = rs.getDouble("paid_amount");
                double pending = total - paid;

                String status = (total == 0) ? "No Fee Assigned" :
                                (pending <= 0) ? "Paid" :
                                (paid > 0) ? "Partially Paid" : "Unpaid";

                model.addRow(new Object[]{
                    rs.getString("rollno"),
                    rs.getString("name"),
                    rs.getString("class_name"),
                    rs.getString("semester_name"),
                    total > 0 ? String.format("₹ %,.2f", total) : "Not Assigned",
                    paid > 0 ? String.format("₹ %,.2f", paid) : "₹ 0.00",
                    pending > 0 ? String.format("₹ %,.2f", pending) : "₹ 0.00",
                    status
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No students found in class: " + teacherClass, "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading fee status:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (c != null) {
                try {
                    if (c.c != null) c.c.close();
                } catch (Exception ignored) {}
            }
        }
    }

    private void printFullPage() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Full list sent to printer.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Print failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        double scale = 0.85;
        g2d.scale(scale, scale);

        getContentPane().printAll(g2d);
        return Printable.PAGE_EXISTS;
    }

    private void openAndPrintReceipt() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) model.getValueAt(row, 7);
        if (!status.equals("Paid") && !status.equals("Partially Paid")) {
            JOptionPane.showMessageDialog(this, "No payment record for this student.", "No Receipt", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rollno = (String) model.getValueAt(row, 0);
        String studentName = (String) model.getValueAt(row, 1);
        String className = (String) model.getValueAt(row, 2);
        String semesterName = (String) model.getValueAt(row, 3);
        String paidStr = (String) model.getValueAt(row, 5);
        double amountPaid = Double.parseDouble(paidStr.replace("₹ ", "").replace(",", ""));

        String transactionId = "DEMO-" + System.currentTimeMillis();
        Conn c = null;
        try {
            c = new Conn();
            String q = "SELECT transaction_id FROM fee_payment WHERE rollno = ? ORDER BY payment_date DESC LIMIT 1";
            PreparedStatement ps = c.c.prepareStatement(q);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                transactionId = rs.getString("transaction_id");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    if (c.c != null) c.c.close();
                } catch (Exception ignored) {}
            }
        }

        new FeeReceipt(rollno, studentName, className, semesterName, amountPaid, transactionId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherStudentFeeStatus("1011321")); // replace with real empId
    }
}