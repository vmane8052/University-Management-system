package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Project1 extends JFrame {

    private JLabel sliderLabel;
    private JPanel loginSection;
    private int currentImage = -1;

    private final String[] images = {
            "icons/shivaji-university-kolhapur.jpg",
            "icons/unishivaji_cover.jpg",
            "icons/slider3.jpg",
            "icons/slider4.jpg"
    };

    public Project1() {

        setTitle("University Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setContentPane(scrollPane);

        // ================= NAVBAR =================
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(10, 35, 66));
        navbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        navbar.setPreferredSize(new Dimension(0, 70));
        navbar.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JLabel logo = new JLabel("UNIVERSITY MANAGEMENT SYSTEM");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);

        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        navButtons.setOpaque(false);

        navButtons.add(createNavButton("Home"));
        navButtons.add(createNavButton("About"));
        navButtons.add(createNavButton("Courses"));
        navButtons.add(createNavButton("Contact"));

        navbar.add(logo, BorderLayout.WEST);
        navbar.add(navButtons, BorderLayout.EAST);

        mainPanel.add(navbar);

        // ================= SLIDER =================
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setPreferredSize(new Dimension(0, 350));
        sliderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        sliderPanel.setBackground(Color.BLACK);

        sliderLabel = new JLabel("", SwingConstants.CENTER);
        sliderLabel.setOpaque(true);
        sliderLabel.setBackground(Color.BLACK);

        sliderPanel.add(sliderLabel, BorderLayout.CENTER);
        mainPanel.add(sliderPanel);

        Timer sliderTimer = new Timer(4000, e -> nextSlide());
        sliderTimer.start();
        nextSlide();

        // ================= PROFESSIONAL MARQUEE =================

JPanel marqueePanel = new JPanel(null);
marqueePanel.setBackground(new Color(200, 0, 0));
marqueePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
marqueePanel.setPreferredSize(new Dimension(0, 50));

JLabel marquee = new JLabel(
        "Admission Open 2026  |  Exam Form Last Date: 15 March  |  NIRF Ranking #18  |  Contact: +91-40-2313-0000"
);
marquee.setFont(new Font("Segoe UI", Font.BOLD, 18));
marquee.setForeground(Color.WHITE);

marqueePanel.add(marquee);
mainPanel.add(marqueePanel);

// Smooth Pixel Animation
Timer marqueeTimer = new Timer(10, null);

marqueeTimer.addActionListener(e -> {

    int x = marquee.getX();
    x -= 1; // speed

    if (x + marquee.getWidth() < 0) {
        x = marqueePanel.getWidth();
    }

    marquee.setLocation(x, 15);
});

marqueePanel.addComponentListener(new ComponentAdapter() {
    public void componentResized(ComponentEvent e) {
        marquee.setSize(marquee.getPreferredSize());
        marquee.setLocation(marqueePanel.getWidth(), 15);
    }
});

marqueeTimer.start();

        // ================= LOGIN SECTION =================
        loginSection = new JPanel();
        loginSection.setOpaque(false);
        loginSection.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));
        mainPanel.add(loginSection);

        loginSection.add(createLoginCard("STUDENT LOGIN", new Color(30, 144, 255)));
        loginSection.add(createLoginCard("TEACHER LOGIN", new Color(46, 204, 113)));
        loginSection.add(createLoginCard("ADMIN LOGIN", new Color(220, 53, 69)));

        mainPanel.add(Box.createVerticalStrut(40));

        // ================= FOOTER =================
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(10, 35, 66));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        footer.setPreferredSize(new Dimension(0, 80));

        JLabel footerText = new JLabel(
                "© 2026 University Management System | Developed by MCA Department",
                SwingConstants.CENTER
        );
        footerText.setForeground(Color.WHITE);
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        footer.add(footerText, BorderLayout.CENTER);
        mainPanel.add(footer);

        // ================= RESPONSIVE =================
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {

                int width = getWidth();

                if (width < 1000) {
                    loginSection.setLayout(new GridLayout(3, 1, 30, 30));
                } else {
                    loginSection.setLayout(new GridLayout(1, 3, 30, 30));
                }

                loginSection.revalidate();
                nextSlide();
            }
        });

        setVisible(true);
    }

    // ================= NAV BUTTON =================
    private JButton createNavButton(String text) {

        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(10, 35, 66));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBorder(null);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(new Color(255, 215, 0));
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    // ================= LOGIN CARD =================
    private JPanel createLoginCard(String text, Color color) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        card.add(label, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                card.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(color);
            }

            public void mouseClicked(MouseEvent e) {

                if (text.contains("STUDENT")) {
                    new StudentLogin();
                } else if (text.contains("TEACHER")) {
                    new TeacherLogin();
                } else {
                    new Login();
                }
            }
        });

        return card;
    }

    // ================= SLIDER =================
    private void nextSlide() {

        currentImage = (currentImage + 1) % images.length;

        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(images[currentImage]));

            Image img = icon.getImage().getScaledInstance(
                    sliderLabel.getWidth(),
                    sliderLabel.getHeight(),
                    Image.SCALE_SMOOTH
            );

            sliderLabel.setIcon(new ImageIcon(img));

        } catch (Exception e) {
            sliderLabel.setText("Image Missing");
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(Project1::new);
    }
}