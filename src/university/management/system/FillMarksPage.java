package university.management.system;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FillMarksPage extends JFrame implements ActionListener {

    String rollno, teacherEmpId;
    JButton saveBtn, backBtn;
    JLabel lblStudentName, lblClass, lblSemester;
    JLabel lblOverallTotal, lblOverallMax, lblOverallPercent, lblOverallStatus;
    JPanel subjectsPanel;
    List<SubjectRow> subjectRows = new ArrayList<>();

    public FillMarksPage(String rollno, String teacherEmpId) {
        this.rollno = rollno;
        this.teacherEmpId = teacherEmpId;

        setTitle("Fill Marks - All Subjects");
        setSize(900, 700);
        setLocation(300, 0);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Header
        JLabel heading = new JLabel("Enter Marks for Student");
        heading.setBounds(220, 10, 500, 35);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(30, 60, 120));
        add(heading);

        // Student Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(40, 60, 820, 90);
        infoPanel.setBackground(new Color(230, 240, 255));
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 1));
        infoPanel.setLayout(null);
        add(infoPanel);

        lblStudentName = new JLabel("Loading...");
        lblStudentName.setBounds(20, 12, 780, 25);
        lblStudentName.setFont(new Font("Segoe UI", Font.BOLD, 17));
        infoPanel.add(lblStudentName);

        lblClass = new JLabel("Class: —");
        lblClass.setBounds(20, 42, 780, 22);
        lblClass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        infoPanel.add(lblClass);

        lblSemester = new JLabel("Semester: —");
        lblSemester.setBounds(20, 65, 780, 22);
        lblSemester.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        infoPanel.add(lblSemester);

        // Table Headers
        String[] headers = {"Subject", "Type", "Internal", "External", "Total", "Status"};
        int[] xPos = {30, 240, 340, 440, 560, 680};
        int headerY = 165;
        for (int i = 0; i < headers.length; i++) {
            JLabel lbl = new JLabel(headers[i]);
            lbl.setBounds(xPos[i], headerY, 120, 28);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            add(lbl);
        }

        // Scrollable subjects panel
        subjectsPanel = new JPanel();
        subjectsPanel.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(subjectsPanel);
        scrollPane.setBounds(30, 200, 840, 370);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        // Overall summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBounds(30, 580, 840, 80);
        summaryPanel.setBackground(new Color(240, 248, 255));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Overall Summary"));
        summaryPanel.setLayout(null);
        add(summaryPanel);

        lblOverallTotal = createSummaryLabel("Total Obtained: 0", 20, 20);
        summaryPanel.add(lblOverallTotal);

        lblOverallMax = createSummaryLabel("Total Maximum: 0", 20, 45);
        summaryPanel.add(lblOverallMax);

        lblOverallPercent = createSummaryLabel("Overall Percentage: 0.00%", 340, 20);
        summaryPanel.add(lblOverallPercent);

        lblOverallStatus = createSummaryLabel("Overall Status: —", 340, 45);
        summaryPanel.add(lblOverallStatus);

        // Buttons - Save right next to overall percentage
        saveBtn = new JButton("Save All Marks");
        saveBtn.setBounds(680, 595, 140, 40);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        saveBtn.setBackground(new Color(40, 167, 69));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(this);
        add(saveBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(680, 640, 140, 40);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        backBtn.setBackground(new Color(220, 53, 69));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> {
            new AddMarksPage(teacherEmpId);
            dispose();
        });
        add(backBtn);

        loadStudentAndSubjects();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JLabel createSummaryLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 400, 25);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return lbl;
    }

    private void loadStudentAndSubjects() {
        try {
            Conn c = new Conn();

            String stuQuery = "SELECT name, class_name, semester_name FROM student WHERE rollno = ?";
            PreparedStatement psStu = c.c.prepareStatement(stuQuery);
            psStu.setString(1, rollno);
            ResultSet rsStu = psStu.executeQuery();

            String name = "", className = "", sem = "";
            if (rsStu.next()) {
                name = rsStu.getString("name");
                className = rsStu.getString("class_name");
                sem = rsStu.getString("semester_name");
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            lblStudentName.setText("Student: " + name + " (" + rollno + ")");
            lblClass.setText("Class: " + (className.isEmpty() ? "—" : className));
            lblSemester.setText("Semester: " + (sem.isEmpty() ? "—" : sem));

            String subQuery =
                "SELECT subject_name, subject_type, internal_min, internal_max, external_min, external_max " +
                "FROM subject1 " +
                "WHERE REPLACE(REPLACE(REPLACE(UPPER(TRIM(course_id)), ' ', ''), '.', ''), '-', '') = " +
                " REPLACE(REPLACE(REPLACE(UPPER(TRIM(?)), ' ', ''), '.', ''), '-', '') " +
                "AND REPLACE(REPLACE(REPLACE(UPPER(TRIM(sem_id)), ' ', ''), '.', ''), '-', '') = " +
                " REPLACE(REPLACE(REPLACE(UPPER(TRIM(?)), ' ', ''), '.', ''), '-', '')";

            PreparedStatement psSub = c.c.prepareStatement(subQuery);
            psSub.setString(1, className);
            psSub.setString(2, sem);
            ResultSet rs = psSub.executeQuery();

            int y = 10;
            subjectRows.clear();

            if (!rs.isBeforeFirst()) {
                JLabel msg = new JLabel("No subjects found for this class/semester.");
                msg.setBounds(200, 100, 500, 30);
                subjectsPanel.add(msg);
                saveBtn.setEnabled(false);
                updateOverallSummary();
                return;
            }

            while (rs.next()) {
                String sub = rs.getString("subject_name");
                String type = rs.getString("subject_type");
                int imin = rs.getInt("internal_min");
                int imax = rs.getInt("internal_max");
                int emin = rs.getInt("external_min");
                int emax = rs.getInt("external_max");

                SubjectRow row = new SubjectRow(sub, type, imin, imax, emin, emax, y);
                subjectsPanel.add(row.panel);
                subjectRows.add(row);
                y += 48;
            }

            subjectsPanel.setPreferredSize(new Dimension(850, y + 20));
            updateOverallSummary();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOverallSummary() {
        int grandTotal = 0;
        int grandMax = 0;
        boolean allPass = true;

        for (SubjectRow row : subjectRows) {
            int i = 0, e = 0;
            try {
                i = row.tfInternal.getText().trim().isEmpty() ? 0 : Integer.parseInt(row.tfInternal.getText().trim());
                e = row.tfExternal.getText().trim().isEmpty() ? 0 : Integer.parseInt(row.tfExternal.getText().trim());
            } catch (Exception ignored) {}

            grandTotal += (i + e);
            grandMax += (row.internalMax + row.externalMax);

            boolean pass = (i >= row.internalMin && i <= row.internalMax) &&
                           (e >= row.externalMin && e <= row.externalMax);
            if (!pass) allPass = false;
        }

        lblOverallTotal.setText("Total Obtained: " + grandTotal);
        lblOverallMax.setText("Total Maximum: " + grandMax);

        double percent = grandMax > 0 ? (grandTotal * 100.0 / grandMax) : 0;
        lblOverallPercent.setText(String.format("Overall Percentage: %.2f%%", percent));

        lblOverallStatus.setText(allPass ? "Overall: Pass" : "Overall: Fail");
        lblOverallStatus.setForeground(allPass ? new Color(0, 128, 0) : Color.RED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            try {
                Conn c = new Conn();

                for (SubjectRow row : subjectRows) {
                    String sub = row.subjectName;
                    String intStr = row.tfInternal.getText().trim();
                    String extStr = row.tfExternal.getText().trim();

                    if (intStr.isEmpty() && extStr.isEmpty()) continue;

                    int internal = intStr.isEmpty() ? 0 : Integer.parseInt(intStr);
                    int external = extStr.isEmpty() ? 0 : Integer.parseInt(extStr);

                    if (internal > row.internalMax || external > row.externalMax) {
                        JOptionPane.showMessageDialog(this,
                            "Cannot enter marks higher than maximum for " + sub,
                            "Invalid Marks", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int total = internal + external;

                    String check = "SELECT * FROM marks WHERE rollno=? AND subject=? AND teacher_empId=?";
                    PreparedStatement psCheck = c.c.prepareStatement(check);
                    psCheck.setString(1, rollno);
                    psCheck.setString(2, sub);
                    psCheck.setString(3, teacherEmpId);
                    ResultSet rsCheck = psCheck.executeQuery();

                    String query;
                    PreparedStatement ps;

                    if (rsCheck.next()) {
                        // UPDATE
                        query = "UPDATE marks SET internal_marks=?, external_marks=?, total_marks=? " +
                                "WHERE rollno=? AND subject=? AND teacher_empId=?";
                        ps = c.c.prepareStatement(query);
                        ps.setInt(1, internal);
                        ps.setInt(2, external);
                        ps.setInt(3, total);
                        ps.setString(4, rollno);
                        ps.setString(5, sub);
                        ps.setString(6, teacherEmpId);
                    } else {
                        // INSERT - Correct parameter order
                        query = "INSERT INTO marks (rollno, subject, internal_marks, external_marks, total_marks, teacher_empId) " +
                                "VALUES (?, ?, ?, ?, ?, ?)";
                        ps = c.c.prepareStatement(query);
                        ps.setString(1, rollno);          // rollno
                        ps.setString(2, sub);             // subject
                        ps.setInt(3, internal);           // internal_marks
                        ps.setInt(4, external);           // external_marks
                        ps.setInt(5, total);              // total_marks
                        ps.setString(6, teacherEmpId);    // teacher_empId
                    }

                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "All marks saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new AddMarksPage(teacherEmpId);
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving marks: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SubjectRow {
        String subjectName;
        JTextField tfInternal, tfExternal;
        JLabel lblTotal, lblStatus, lblType;
        int internalMin, internalMax, externalMin, externalMax;
        JPanel panel;

        SubjectRow(String sub, String type, int iMin, int iMax, int eMin, int eMax, int y) {
            subjectName = sub;
            internalMin = iMin;
            internalMax = iMax;
            externalMin = eMin;
            externalMax = eMax;

            panel = new JPanel();
            panel.setBounds(0, y, 850, 42);
            panel.setLayout(null);
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            JLabel lblSub = new JLabel(sub);
            lblSub.setBounds(10, 8, 210, 28);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(lblSub);

            lblType = new JLabel(type != null ? type : "Theory");
            lblType.setBounds(230, 8, 90, 28);
            lblType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(lblType);

            tfInternal = createRestrictedTextField(iMax);
            tfInternal.setBounds(330, 8, 90, 28);
            panel.add(tfInternal);

            tfExternal = createRestrictedTextField(eMax);
            tfExternal.setBounds(430, 8, 90, 28);
            panel.add(tfExternal);

            lblTotal = new JLabel("0");
            lblTotal.setBounds(540, 8, 80, 28);
            lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTotal.setForeground(new Color(0, 80, 0));
            panel.add(lblTotal);

            lblStatus = new JLabel("—");
            lblStatus.setBounds(640, 8, 140, 28);
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(lblStatus);

            KeyListener calc = new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    updateRow();
                    updateOverallSummary();
                }
            };
            tfInternal.addKeyListener(calc);
            tfExternal.addKeyListener(calc);
        }

        private JTextField createRestrictedTextField(int maxValue) {
            JTextField field = new JTextField();
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            AbstractDocument doc = (AbstractDocument) field.getDocument();
            doc.setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) return;
                    String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                    if (isValidInput(newText)) super.insertString(fb, offset, string, attr);
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) return;
                    String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = current.substring(0, offset) + text + current.substring(offset + length);
                    if (isValidInput(newText)) super.replace(fb, offset, length, text, attrs);
                }

                private boolean isValidInput(String text) {
                    if (text.isEmpty()) return true;
                    try {
                        int val = Integer.parseInt(text);
                        return val >= 0 && val <= maxValue;
                    } catch (NumberFormatException e) {
                        return text.matches("\\d*");
                    }
                }
            });
            return field;
        }

        private void updateRow() {
            try {
                int i = tfInternal.getText().trim().isEmpty() ? 0 : Integer.parseInt(tfInternal.getText().trim());
                int e = tfExternal.getText().trim().isEmpty() ? 0 : Integer.parseInt(tfExternal.getText().trim());

                int total = i + e;
                lblTotal.setText(String.valueOf(total));

                boolean pass = (i >= internalMin && i <= internalMax) &&
                               (e >= externalMin && e <= externalMax);

                lblStatus.setText(pass ? "Pass" : "Fail");
                lblStatus.setForeground(pass ? new Color(0, 128, 0) : Color.RED);

            } catch (NumberFormatException ex) {
                lblTotal.setText("—");
                lblStatus.setText("Invalid");
                lblStatus.setForeground(Color.RED);
            }
        }
    }

    public static void main(String[] args) {
        new FillMarksPage("sample123", "T001");
    }
}