package university.management.system;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StudentAttendanceCalendar extends JFrame {

    private String rollno;
    private JCalendar calendar;
    private JLabel lblStatus;

    public StudentAttendanceCalendar(String rollno) {
        this.rollno = rollno.trim();

        setTitle("My Attendance Calendar - " + rollno);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));

        // Heading
        JLabel heading = new JLabel("My Attendance Calendar", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(30, 60, 120));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Calendar Component
        calendar = new JCalendar();
        calendar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        calendar.setDecorationBordersVisible(true);
        calendar.getDayChooser().setDayBordersVisible(true);
        calendar.setWeekOfYearVisible(false);

        // Custom renderer for red/green marks
        calendar.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateDayColors();
            }
        });

        add(calendar, BorderLayout.CENTER);

        // Status label (bottom)
        lblStatus = new JLabel("Select a date to see attendance status", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(lblStatus, BorderLayout.SOUTH);

        // Load attendance and color days
        loadAndColorAttendance();

        setVisible(true);
    }

    private void loadAndColorAttendance() {
        try {
            Conn c = new Conn();

            // Get all attendance for this student
            String query = "SELECT date, status FROM attendance WHERE rollno = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, rollno);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Date date = rs.getDate("date");
                String status = rs.getString("status");

                // Mark day in calendar
                calendar.getDayChooser().setDay(date.getDate());
                // We use custom decoration later
            }

            updateDayColors();

        } catch (Exception ex) {
            ex.printStackTrace();
            lblStatus.setText("Error loading attendance");
            lblStatus.setForeground(Color.RED);
        }
    }

    private void updateDayColors() {
        Calendar cal = calendar.getCalendar();

        // Simple way: loop through current month days
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            Date date = cal.getTime();

            // Check attendance for this date
            try {
                Conn c = new Conn();
                String query = "SELECT status FROM attendance WHERE rollno = ? AND date = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, rollno);
                ps.setDate(2, new java.sql.Date(date.getTime()));
                ResultSet rs = ps.executeQuery();

                Color color = Color.LIGHT_GRAY; // default (not marked)
                if (rs.next()) {
                    String status = rs.getString("status");
                    if ("Present".equalsIgnoreCase(status)) {
                        color = Color.GREEN;
                    } else if ("Absent".equalsIgnoreCase(status)) {
                        color = Color.RED;
                    }
                }

                // Apply color to day button (custom decoration)
                calendar.getDayChooser().getDayPanel().getComponent(day - 1).setBackground(color);

            } catch (Exception ex) {
                // silent fail for individual days
            }
        }

        lblStatus.setText("Select any date to see details");
        lblStatus.setForeground(Color.GRAY);
    }

    public static void main(String[] args) {
        new StudentAttendanceCalendar("15333936"); // test rollno
    }
}