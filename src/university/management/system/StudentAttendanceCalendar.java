package university.management.system;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentAttendanceCalendar extends JFrame {

    private final String rollno;
    private final JCalendar calendar;
    private final JLabel lblStatus;
    private final Map<String, String> attendanceMap = new HashMap<>();

    public StudentAttendanceCalendar(String rollno) {
        this.rollno = rollno.trim();
        setTitle("My Attendance Calendar - " + this.rollno);
        setSize(850, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));

        // Heading
        JLabel heading = new JLabel("My Attendance Calendar", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        add(heading, BorderLayout.NORTH);

        calendar = new JCalendar();
        calendar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        calendar.setDecorationBordersVisible(true);
        calendar.getDayChooser().setDayBordersVisible(true);
        calendar.setWeekOfYearVisible(false);

        // Start on February 2026 to see your data
        Calendar start = Calendar.getInstance();
        start.set(2026, Calendar.FEBRUARY, 1);
        calendar.setCalendar(start);

        // Listeners
        calendar.getDayChooser().addPropertyChangeListener("day", evt -> updateSelectedDateStatus());
        calendar.getMonthChooser().addPropertyChangeListener("month", e -> colorKnownDates());
        calendar.getYearChooser().addPropertyChangeListener("year", e -> colorKnownDates());

        add(calendar, BorderLayout.CENTER);

        // Bottom panel with status + button
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        bottomPanel.setBackground(new Color(245, 245, 250));

        lblStatus = new JLabel("Select a date to see attendance status", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatus.setForeground(Color.DARK_GRAY);

        JButton btnPercentage = new JButton("View Percentage");
        btnPercentage.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnPercentage.setBackground(new Color(30, 60, 120));
        btnPercentage.setForeground(Color.WHITE);
        btnPercentage.setFocusPainted(false);
        btnPercentage.setPreferredSize(new Dimension(180, 40));

        // Open percentage window on click
        btnPercentage.addActionListener(e -> new AttendancePercentageView(this.rollno));

        bottomPanel.add(lblStatus, BorderLayout.CENTER);
        bottomPanel.add(btnPercentage, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        loadAttendanceIntoMemory();

        // Color after UI layout
        SwingUtilities.invokeLater(this::colorKnownDates);

        setVisible(true);
    }

    // ────────────────────────────────────────────────
    // Your existing loadAttendanceIntoMemory() method
    // ────────────────────────────────────────────────
    private void loadAttendanceIntoMemory() {
        try {
            Conn c = new Conn();
            String query = "SELECT date, status FROM attendance WHERE rollno = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                java.sql.Date sqlDate = rs.getDate("date");
                String status = rs.getString("status");
                if (sqlDate != null && status != null) {
                    String key = sdf.format(new java.util.Date(sqlDate.getTime()));
                    attendanceMap.put(key, status.trim());
                }
            }
            rs.close();
            ps.close();

            System.out.println("Loaded " + attendanceMap.size() + " records for rollno " + rollno);
            attendanceMap.forEach((k, v) -> System.out.println(" " + k + " → " + v));

        } catch (Exception ex) {
            ex.printStackTrace();
            lblStatus.setText("Error loading attendance");
            lblStatus.setForeground(Color.RED);
        }
    }

    // ────────────────────────────────────────────────
    // Your existing colorKnownDates() method (unchanged)
    // ────────────────────────────────────────────────
    private void colorKnownDates() {
        if (attendanceMap.isEmpty()) {
            System.out.println("No attendance to color");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar displayed = calendar.getCalendar();
        int dispYear = displayed.get(Calendar.YEAR);
        int dispMonth = displayed.get(Calendar.MONTH);

        Calendar first = (Calendar) displayed.clone();
        first.set(Calendar.DAY_OF_MONTH, 1);
        int firstDOW = first.get(Calendar.DAY_OF_WEEK);
        int offset = firstDOW - Calendar.SUNDAY;

        Component[] cells = calendar.getDayChooser().getDayPanel().getComponents();

        int headerCount = 7;
        int dayStart = headerCount;

        for (int i = dayStart; i < cells.length; i++) {
            Component c = cells[i];
            c.setBackground(new Color(248, 248, 255));
            c.setForeground(Color.GRAY.darker());
        }

        for (Map.Entry<String, String> entry : attendanceMap.entrySet()) {
            String key = entry.getKey();
            try {
                java.util.Date parsed = sdf.parse(key);
                Calendar dc = Calendar.getInstance();
                dc.setTime(parsed);

                if (dc.get(Calendar.YEAR) == dispYear && dc.get(Calendar.MONTH) == dispMonth) {
                    int dayNum = dc.get(Calendar.DAY_OF_MONTH);
                    int index = headerCount + offset + (dayNum - 1);

                    if (index >= dayStart && index < cells.length) {
                        Component comp = cells[index];
                        String norm = entry.getValue().trim().toLowerCase();
                        Color bg = new Color(248, 248, 255);
                        Color fg = Color.GRAY.darker();

                        if (norm.contains("present")) {
                            bg = new Color(40, 167, 69);
                            fg = Color.WHITE;
                        } else if (norm.contains("absent")) {
                            bg = new Color(220, 53, 69);
                            fg = Color.WHITE;
                        }

                        comp.setBackground(bg);
                        comp.setForeground(fg);

                        System.out.println("SUCCESS: Colored day " + dayNum + " at index " + index + " (" + key + ") → " + entry.getValue());
                    } else {
                        System.out.println("Index out of range: " + index + " for day " + dayNum + " (" + key + ")");
                    }
                }
            } catch (Exception e) {
                System.out.println("Date parse error: " + key);
            }
        }

        int daysInMonth = displayed.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int d = 1; d <= daysInMonth; d++) {
            Calendar sc = (Calendar) first.clone();
            sc.set(Calendar.DAY_OF_MONTH, d);
            if (sc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                String key = sdf.format(sc.getTime());
                if (!attendanceMap.containsKey(key)) {
                    int index = headerCount + offset + (d - 1);
                    if (index >= dayStart && index < cells.length) {
                        Component comp = cells[index];
                        comp.setBackground(new Color(220, 230, 255));
                        comp.setForeground(Color.BLACK);
                    }
                }
            }
        }

        calendar.getDayChooser().getDayPanel().revalidate();
        calendar.getDayChooser().getDayPanel().repaint();
    }

    private void updateSelectedDateStatus() {
        java.util.Date selected = calendar.getDate();
        if (selected == null) return;

        Calendar cal = Calendar.getInstance();
        cal.setTime(selected);
        int dow = cal.get(Calendar.DAY_OF_WEEK);

        SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy (EEEE)");
        String niceDate = fmt.format(selected);

        String key = new SimpleDateFormat("yyyy-MM-dd").format(selected);
        String status = attendanceMap.get(key);

        if (status != null) {
            String norm = status.trim().toLowerCase();
            if (norm.contains("present")) {
                lblStatus.setText("Date: " + niceDate + " → Present");
                lblStatus.setForeground(new Color(40, 167, 69));
            } else if (norm.contains("absent")) {
                lblStatus.setText("Date: " + niceDate + " → Absent");
                lblStatus.setForeground(new Color(220, 53, 69));
            } else {
                lblStatus.setText("Date: " + niceDate + " → " + status);
                lblStatus.setForeground(Color.GRAY);
            }
        } else if (dow == Calendar.SUNDAY) {
            lblStatus.setText("Date: " + niceDate + " → Sunday (Holiday)");
            lblStatus.setForeground(new Color(0, 80, 150));
        } else {
            lblStatus.setText("Date: " + niceDate + " → No attendance record");
            lblStatus.setForeground(Color.GRAY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentAttendanceCalendar("15333880"));
    }
}