package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentMarksView extends JFrame {

    private String rollno;
    private JTable marksTable;
    private JLabel lblBottomResult;
    private String studentName = "N/A"; // fetched from DB

    public StudentMarksView(String rollno) {
        this.rollno = rollno.trim();

        setTitle("My Marks - " + rollno);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));

        // Heading
        JLabel heading = new JLabel("My Marks", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        // Marks Table
        String[] columns = {"Subject", "Internal", "External", "Total", "Marked By", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        marksTable = new JTable(tableModel);
        marksTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        marksTable.setRowHeight(28);
        marksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        marksTable.getTableHeader().setBackground(new Color(30, 60, 120));
        marksTable.getTableHeader().setForeground(Color.WHITE);
        marksTable.setGridColor(new Color(200, 200, 200));
        marksTable.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(marksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Result Panel (green text only)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        lblBottomResult = new JLabel("Calculating Result...", SwingConstants.CENTER);
        lblBottomResult.setFont(new Font("Segoe UI", Font.BOLD, 16)); // smaller size
        lblBottomResult.setForeground(new Color(0, 128, 0)); // Green
        bottomPanel.add(lblBottomResult, BorderLayout.CENTER);

        // Print Button
        JButton printBtn = new JButton("Print / Save as PDF");
        printBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        printBtn.setBackground(new Color(40, 167, 69));
        printBtn.setForeground(Color.WHITE);
        printBtn.setPreferredSize(new Dimension(200, 40));
        printBtn.addActionListener(e -> printMarks());
        bottomPanel.add(printBtn, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load data
        loadMarksAndResult();

        setVisible(true);
    }

    private void loadMarksAndResult() {
        try {
            Conn c = new Conn();

            // Get student name for print
            String nameQuery = "SELECT name FROM student WHERE rollno = ?";
            PreparedStatement psName = c.c.prepareStatement(nameQuery);
            psName.setString(1, rollno);
            ResultSet rsName = psName.executeQuery();
            if (rsName.next()) {
                studentName = rsName.getString("name");
            }

            // Get marks
            String marksQuery = 
                "SELECT m.subject, m.internal_marks, m.external_marks, m.total_marks, " +
                "t.name AS teacher_name, " +
                "CASE WHEN m.internal_marks >= s.internal_min AND m.external_marks >= s.external_min " +
                "THEN 'Pass' ELSE 'Fail' END AS status " +
                "FROM marks m " +
                "LEFT JOIN teacher t ON m.teacher_empId = t.empId " +
                "LEFT JOIN subject1 s ON m.subject = s.subject_name " +
                "WHERE m.rollno = ? ORDER BY m.subject";

            PreparedStatement ps = c.c.prepareStatement(marksQuery);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) marksTable.getModel();
            model.setRowCount(0);

            int totalObtained = 0;
            int totalMax = 0;
            boolean allPass = true;
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                String subject = rs.getString("subject");
                int internal = rs.getInt("internal_marks");
                int external = rs.getInt("external_marks");
                int total = rs.getInt("total_marks");
                String teacher = rs.getString("teacher_name") != null ? rs.getString("teacher_name") : "Unknown";
                String status = rs.getString("status");

                model.addRow(new Object[]{subject, internal, external, total, teacher, status});

                totalObtained += total;

                String maxQ = "SELECT internal_max, external_max FROM subject1 WHERE subject_name = ?";
                PreparedStatement psMax = c.c.prepareStatement(maxQ);
                psMax.setString(1, subject);
                ResultSet rsMax = psMax.executeQuery();
                if (rsMax.next()) {
                    totalMax += rsMax.getInt("internal_max") + rsMax.getInt("external_max");
                }

                if ("Fail".equals(status)) {
                    allPass = false;
                }
            }

            if (hasData) {
                double percentage = totalMax > 0 ? (totalObtained * 100.0 / totalMax) : 0;

                String resultText = String.format(
                    "<html><font color='green'><b>Total Marks: %d / %d</b><br>" +
                    "<b>Percentage: %.2f%%</b><br>" +
                    "<b>Overall Result: %s</b></font></html>",
                    totalObtained, totalMax, percentage,
                    allPass ? "PASS" : "FAIL"
                );

                lblBottomResult.setText(resultText);
            } else {
                lblBottomResult.setText("No marks have been filled yet.");
                lblBottomResult.setForeground(Color.GRAY);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            lblBottomResult.setText("Error loading marks");
            lblBottomResult.setForeground(Color.RED);
        }
    }

    private void printMarks() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                // Header - Student Name, Roll No, Date
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                g2d.drawString("Student Name: " + studentName, 100, 80);
                g2d.drawString("Roll No: " + rollno, 100, 110);
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                g2d.drawString("Date: " + currentDate, 100, 140);

                // Title
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString("Marks & Result", 100, 180);

                // Print table (no catch needed here)
                marksTable.print(g2d);

                // Bottom result
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String bottomText = lblBottomResult.getText().replaceAll("<[^>]*>", "");
                g2d.drawString(bottomText, 100, 450);

                return Printable.PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, 
                    "Print / Save as PDF successful!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Failed to print or save PDF.\n\n" +
                    "Error: " + ex.getMessage() + "\n\n" +
                    "Possible reasons:\n" +
                    "• No printer connected\n" +
                    "• No default printer set\n" +
                    "• PDF driver/printer not installed\n" +
                    "• Permission issue\n\n" +
                    "Try again or contact support.",
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Print cancelled.", 
                "Cancelled", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new StudentMarksView("15333936"); // test with your rollno
    }
}