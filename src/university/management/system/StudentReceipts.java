package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StudentReceipts extends JFrame {

    private String rollno;
    private JTable table;
    private DefaultTableModel model;

    // Student details fetched once
    private String studentName = "N/A";
    private String className = "N/A";
    private String semesterName = "N/A";

    public StudentReceipts(String rollno) {
        this.rollno = rollno.trim();
        setTitle("My Payment Receipts - " + rollno);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fetch student details once
        fetchStudentDetails();

        // Heading
        JLabel heading = new JLabel("My Fee Payment Receipts", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Payment ID", "Amount Paid", "Payment Date", "Transaction ID", "Status", "View Receipt"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(30, 60, 120));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadPayments();

        // Handle click on "View Receipt" column
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row >= 0 && col == 5) {  // View Receipt column
                    String transId = (String) model.getValueAt(row, 3);
                    String amountStr = (String) model.getValueAt(row, 1);
                    double amount = Double.parseDouble(amountStr.replace("₹ ", "").replace(",", ""));

                    // Open receipt with real student details
                    new FeeReceipt(rollno, studentName, className, semesterName, amount, transId);
                }
            }
        });

        // Back button at bottom
        JButton btnBack = new JButton("Close");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBackground(new Color(220, 53, 69));
        btnBack.setForeground(Color.WHITE);
        btnBack.setPreferredSize(new Dimension(140, 45));
        btnBack.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void fetchStudentDetails() {
        try {
            Conn c = new Conn();
            String query = "SELECT name, class_name, semester_name FROM student WHERE rollno = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                studentName = rs.getString("name") != null ? rs.getString("name").trim() : "N/A";
                className = rs.getString("class_name") != null ? rs.getString("class_name").trim() : "N/A";
                semesterName = rs.getString("semester_name") != null ? rs.getString("semester_name").trim() : "N/A";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load student details.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadPayments() {
        model.setRowCount(0);
        try {
            Conn c = new Conn();
            String query = "SELECT payment_id, amount_paid, payment_date, transaction_id, status " +
                           "FROM fee_payment WHERE rollno = ? ORDER BY payment_date DESC";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String amountFormatted = String.format("₹ %,.2f", rs.getDouble("amount_paid"));
                String dateFormatted = rs.getTimestamp("payment_date").toString().substring(0, 19);
                String action = "View Receipt";

                model.addRow(new Object[]{
                    rs.getInt("payment_id"),
                    amountFormatted,
                    dateFormatted,
                    rs.getString("transaction_id"),
                    rs.getString("status"),
                    action
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "No fee payments found for Roll No: " + rollno,
                    "No Records", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading payment history:\n" + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new StudentReceipts("15333880"); // test with your roll number
    }
}