package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeeReceipt extends JFrame implements Printable {

    public FeeReceipt(String rollno, String studentName, String className, String semesterName,
                      double amountPaid, String transactionId) {
        setTitle("Fee Payment Receipt");
        setSize(650, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel receiptPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 230, 230));
                g2.drawRoundRect(15, 15, getWidth() - 30, getHeight() - 30, 20, 20);
                g2.dispose();
            }
        };
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setLayout(new BorderLayout(20, 20));
        receiptPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JLabel uniName = new JLabel("UNIVERSITY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        uniName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        uniName.setForeground(new Color(30, 60, 120));

        JLabel receiptTitle = new JLabel("OFFICIAL FEE RECEIPT", SwingConstants.CENTER);
        receiptTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        receiptTitle.setForeground(new Color(40, 167, 69));

        JLabel receiptNo = new JLabel("Receipt No: " + (transactionId != null ? transactionId : "DEMO-" + System.currentTimeMillis()),
                SwingConstants.CENTER);
        receiptNo.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(uniName);
        headerPanel.add(receiptTitle);
        headerPanel.add(receiptNo);

        receiptPanel.add(headerPanel, BorderLayout.NORTH);

        // Details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 15, 18));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        Font boldFont = new Font("Segoe UI", Font.BOLD, 16);
        Font plainFont = new Font("Segoe UI", Font.PLAIN, 16);

        addDetail(detailsPanel, "Student Name:", studentName, boldFont, plainFont);
        addDetail(detailsPanel, "Roll Number:", rollno, boldFont, plainFont);
        addDetail(detailsPanel, "Course / Class:", className, boldFont, plainFont);
        addDetail(detailsPanel, "Semester:", semesterName, boldFont, plainFont);
        addDetail(detailsPanel, "Amount Paid:", String.format("₹ %,.2f", amountPaid), boldFont, plainFont);
        addDetail(detailsPanel, "Payment Date:", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()), boldFont, plainFont);
        addDetail(detailsPanel, "Transaction ID:", transactionId, boldFont, plainFont);
        addDetail(detailsPanel, "Payment Mode:", "Online (Demo)", boldFont, plainFont);
        addDetail(detailsPanel, "Status:", "SUCCESS", boldFont, plainFont);

        receiptPanel.add(detailsPanel, BorderLayout.CENTER);

        // Footer message
        JLabel thankYou = new JLabel("Thank you for your payment. This is a computer-generated receipt.", SwingConstants.CENTER);
        thankYou.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        thankYou.setForeground(new Color(100, 100, 100));
        receiptPanel.add(thankYou, BorderLayout.SOUTH);

        add(receiptPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        btnPanel.setBackground(new Color(245, 245, 245));

        JButton btnPrint = new JButton("Print Receipt");
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPrint.setBackground(new Color(40, 167, 69));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setPreferredSize(new Dimension(180, 50));
        btnPrint.addActionListener(e -> printThisReceipt());
        btnPanel.add(btnPrint);

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.WHITE);
        btnClose.setPreferredSize(new Dimension(140, 50));
        btnClose.addActionListener(e -> dispose());
        btnPanel.add(btnClose);

        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addDetail(JPanel panel, String label, String value, Font lblFont, Font valFont) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(lblFont);
        lbl.setForeground(new Color(60, 60, 80));

        JLabel val = new JLabel(value);
        val.setFont(valFont);
        val.setForeground(new Color(30, 30, 50));

        panel.add(lbl);
        panel.add(val);
    }

    private void printThisReceipt() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Receipt sent to printer.", "Print Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Printing failed:\n" + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
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

        // Optional: scale down slightly if needed
        double scale = 0.92;
        g2d.scale(scale, scale);

        getContentPane().printAll(g2d);

        return Printable.PAGE_EXISTS;
    }

    // Test main
    public static void main(String[] args) {
        new FeeReceipt("15333936", "Vikas Kumar", "B.Tech Computer Science", "Semester 5", 85000.00, "DEMO-XYZ789123");
    }
}