package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Project extends JFrame implements ActionListener {

    Project() {
        setSize(1540, 850);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1500, 750, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image);
        
        JMenuBar mb = new JMenuBar();
        
        // New Information
        JMenu newInformation = new JMenu("New Information");
        newInformation.setForeground(Color.BLUE);
        mb.add(newInformation);
        
        JMenuItem facultyInfo = new JMenuItem("Add Faculty Information");
        facultyInfo.setBackground(Color.WHITE);
        facultyInfo.addActionListener(this);
        newInformation.add(facultyInfo);
        
        JMenuItem studentInfo = new JMenuItem("Add Student Information");
        studentInfo.setBackground(Color.WHITE);
        studentInfo.addActionListener(this);
        newInformation.add(studentInfo);
        
        JMenuItem semtInfo = new JMenuItem("Add sem Information");
        semtInfo.setBackground(Color.WHITE);
        semtInfo.addActionListener(this);
        newInformation.add(semtInfo);
        
        JMenuItem classInfo = new JMenuItem("Add Class Information");
        classInfo.setBackground(Color.WHITE);
        classInfo.addActionListener(this);
        newInformation.add(classInfo);
        
        JMenuItem subjectInfo = new JMenuItem("Add subject Information");
        subjectInfo.setBackground(Color.WHITE);
        subjectInfo.addActionListener(this);
        newInformation.add(subjectInfo);
        
        // Details
        JMenu details = new JMenu("View Details");
        details.setForeground(Color.RED);
        mb.add(details);
        
        JMenuItem facultydetails = new JMenuItem("View Faculty Details");
        facultydetails.setBackground(Color.WHITE);
        facultydetails.addActionListener(this);
        details.add(facultydetails);
        
        JMenuItem studentdetails = new JMenuItem("View Student Details");
        studentdetails.setBackground(Color.WHITE);
        studentdetails.addActionListener(this);
        details.add(studentdetails);
        
        JMenuItem semdetails = new JMenuItem("View Sem Details");
        semdetails.setBackground(Color.WHITE);
        semdetails.addActionListener(this);
        details.add(semdetails);
        
        
        JMenuItem coursedetails = new JMenuItem("View Course Details");
        coursedetails.setBackground(Color.WHITE);
        coursedetails.addActionListener(this);
        details.add(coursedetails);
        
         JMenuItem subjectdetails = new JMenuItem("View Subject Details");
        subjectdetails.setBackground(Color.WHITE);
        subjectdetails.addActionListener(this);
        details.add(subjectdetails);
        
        // Leave
        JMenu leave = new JMenu("Apply Leave");
        leave.setForeground(Color.BLUE);
        mb.add(leave);
        
        JMenuItem facultyleave = new JMenuItem("Faculty Leave");
        facultyleave.setBackground(Color.WHITE);
        facultyleave.addActionListener(this);
        leave.add(facultyleave);
        
        JMenuItem studentleave = new JMenuItem("Student Leave");
        studentleave.setBackground(Color.WHITE);
        studentleave.addActionListener(this);
        leave.add(studentleave);
        
        // Leave Details
        JMenu leaveDetails = new JMenu("Leave Details");
        leaveDetails.setForeground(Color.RED);
        mb.add(leaveDetails);
        
        JMenuItem facultyleavedetails = new JMenuItem("Faculty Leave Details");
        facultyleavedetails.setBackground(Color.WHITE);
        facultyleavedetails.addActionListener(this);
        leaveDetails.add(facultyleavedetails);
        
        JMenuItem studentleavedetails = new JMenuItem("Student Leave Details");
        studentleavedetails.setBackground(Color.WHITE);
        studentleavedetails.addActionListener(this);
        leaveDetails.add(studentleavedetails);
        
        // Exams
        JMenu exam = new JMenu("Examination");
        exam.setForeground(Color.BLUE);
        mb.add(exam);
        
        JMenuItem examinationdetails = new JMenuItem("Examination Results");
        examinationdetails.setBackground(Color.WHITE);
        examinationdetails.addActionListener(this);
        exam.add(examinationdetails);
        
        JMenuItem entermarks = new JMenuItem("Enter Marks");
        entermarks.setBackground(Color.WHITE);
        entermarks.addActionListener(this);
        exam.add(entermarks);
        
        // UpdateInfo
        JMenu updateInfo = new JMenu("Update Details");
        updateInfo.setForeground(Color.RED);
        mb.add(updateInfo);
        
        JMenuItem updatefacultyinfo = new JMenuItem("Update Faculty Details");
        updatefacultyinfo.setBackground(Color.WHITE);
        updatefacultyinfo.addActionListener(this);
        updateInfo.add(updatefacultyinfo);
        
        JMenuItem updatestudentinfo = new JMenuItem("Update Student Details");
        updatestudentinfo.setBackground(Color.WHITE);
        updatestudentinfo.addActionListener(this);
        updateInfo.add(updatestudentinfo);
        
        
        JMenuItem updateseminfo = new JMenuItem("Update Sem Details");
        updateseminfo.setBackground(Color.WHITE);
        updateseminfo.addActionListener(this);
        updateInfo.add(updateseminfo);
        
        JMenuItem updatecourseinfo = new JMenuItem("Update Course Details");
        updatecourseinfo.setBackground(Color.WHITE);
        updatecourseinfo.addActionListener(this);
        updateInfo.add(updatecourseinfo);
        
        
         JMenuItem updatesubjectinfo = new JMenuItem("Update Subject Details");
        updatesubjectinfo.setBackground(Color.WHITE);
        updatesubjectinfo.addActionListener(this);
        updateInfo.add(updatesubjectinfo);
        
        // fee
        JMenu fee = new JMenu("Fee Details");
        fee.setForeground(Color.BLUE);
        mb.add(fee);
        
        JMenuItem feestructure = new JMenuItem("Fee Structure");
        feestructure.setBackground(Color.WHITE);
        feestructure.addActionListener(this);
        fee.add(feestructure);
        
        JMenuItem feeform = new JMenuItem("Student Fee Form");
        feeform.setBackground(Color.WHITE);
        feeform.addActionListener(this);
        fee.add(feeform);
        
        // Utility
        JMenu utility = new JMenu("Utility");
        utility.setForeground(Color.RED);
        mb.add(utility);
        
        JMenuItem notepad = new JMenuItem("Notepad");
        notepad.setBackground(Color.WHITE);
        notepad.addActionListener(this);
        utility.add(notepad);
        
        JMenuItem calc = new JMenuItem("Calculator");
        calc.setBackground(Color.WHITE);
        calc.addActionListener(this);
        utility.add(calc);
        
        // about
        JMenu about = new JMenu("About");
        about.setForeground(Color.BLUE);
        mb.add(about);
        
        JMenuItem ab = new JMenuItem("About");
        ab.setBackground(Color.WHITE);
        ab.addActionListener(this);
        about.add(ab);
        
        // exit
        JMenu exit = new JMenu("Exit");
        exit.setForeground(Color.RED);
        mb.add(exit);
        
        JMenuItem ex = new JMenuItem("Exit");
        ex.setBackground(Color.WHITE);
        ex.addActionListener(this);
        exit.add(ex);
        
        setJMenuBar(mb);
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();
        
        if (msg.equals("Exit")) {
            setVisible(false);
        } else if (msg.equals("Calculator")) {
            try {
                Runtime.getRuntime().exec("calc.exe");
            } catch (Exception e) {
                
            }
        } else if (msg.equals("Notepad")) {
            try {
                Runtime.getRuntime().exec("notepad.exe");
            } catch (Exception e) {
                
            }
        } else if (msg.equals("Add Faculty Information")) {
            new AddTeacher();
        } else if (msg.equals("Add Student Information")) {
            new AddStudent();
        } else if (msg.equals("Add sem Information")) {
            new AddSem();
        } else if (msg.equals("Add Class Information")) {
            new AddClass();
        } else if (msg.equals("Add subject Information")) {
            new AddSubject();
        } else if (msg.equals("View Faculty Details")) {
            new TeacherDetails();
        } else if (msg.equals("View Student Details")) {
            new StudentDetails();
        } else if (msg.equals("View Sem Details")) {
            new SemesterDetails();
        } else if (msg.equals("View Course Details")) {
            new ClassDetails();
        } else if (msg.equals("View Subject Details")) {
            new SubjectDetails();
        } else if (msg.equals("Faculty Leave")) {
            new TeacherLeave();
        } else if (msg.equals("Student Leave")) {
            new StudentLeave();
        } else if (msg.equals("Faculty Leave Details")) {
            new TeacherLeaveDetails();
        } else if (msg.equals("Student Leave Details")) {
            new StudentLeaveDetails();
        } else if (msg.equals("Update Faculty Details")) {
            new UpdateTeacher();
        } else if (msg.equals("Update Student Details")) {
            new UpdateStudent();
        } else if (msg.equals("Update Sem Details")) {
            new UpdateSem();
        } else if (msg.equals("Update Course Details")) {
            new UpdateClass();
        } else if (msg.equals("Update Subject Details")) {
            new UpdateSubject();
        } else if (msg.equals("Enter Marks")) {
            new EnterMarks();
        } else if (msg.equals("Examination Results")) {
            new ExaminationDetails();
        } else if (msg.equals("Fee Structure")) {
            new FeeStructure();
        } else if (msg.equals("About")) {
            new About();
        } else if (msg.equals("Student Fee Form")) {
            new StudentFeeForm();
        }
    }

    public static void main(String[] args) {
        new Project();
    }
}
//package university.management.system;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class Project extends JFrame implements ActionListener {
//
//    Project() {
//        setSize(1540, 850);
//        setLocation(0, 0);
//
//        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
//        Image i2 = i1.getImage().getScaledInstance(1500, 750, Image.SCALE_SMOOTH);
//        ImageIcon i3 = new ImageIcon(i2);
//        JLabel image = new JLabel(i3);
//        add(image);
//
//        JMenuBar mb = new JMenuBar();
//        mb.setBackground(new Color(20, 33, 61));
//        mb.setBorder(BorderFactory.createEmptyBorder());
//
//        // ===== New Information =====
//        JMenu newInformation = createMenu("New Information", "icons/add.png");
//        mb.add(newInformation);
//
//        JMenuItem facultyInfo = createItem("New Faculty Information");
//        JMenuItem studentInfo = createItem("New Student Information");
//        newInformation.add(facultyInfo);
//        newInformation.add(studentInfo);
//
//        // ===== View Details =====
//        JMenu details = createMenu("View Details", "icons/view.png");
//        mb.add(details);
//
//        JMenuItem facultydetails = createItem("View Faculty Details");
//        JMenuItem studentdetails = createItem("View Student Details");
//        details.add(facultydetails);
//        details.add(studentdetails);
//
//        // ===== Leave =====
//        JMenu leave = createMenu("Apply Leave", "icons/leave.png");
//        mb.add(leave);
//
//        JMenuItem facultyleave = createItem("Faculty Leave");
//        JMenuItem studentleave = createItem("Student Leave");
//        leave.add(facultyleave);
//        leave.add(studentleave);
//
//        // ===== Leave Details =====
//        JMenu leaveDetails = createMenu("Leave Details", "icons/leave.png");
//        mb.add(leaveDetails);
//
//        JMenuItem facultyleavedetails = createItem("Faculty Leave Details");
//        JMenuItem studentleavedetails = createItem("Student Leave Details");
//        leaveDetails.add(facultyleavedetails);
//        leaveDetails.add(studentleavedetails);
//
//        // ===== Exam =====
//        JMenu exam = createMenu("Examination", "icons/exam.png");
//        mb.add(exam);
//
//        JMenuItem examinationdetails = createItem("Examination Results");
//        JMenuItem entermarks = createItem("Enter Marks");
//        exam.add(examinationdetails);
//        exam.add(entermarks);
//
//        // ===== Update =====
//        JMenu updateInfo = createMenu("Update Details", "icons/update.png");
//        mb.add(updateInfo);
//
//        JMenuItem updatefacultyinfo = createItem("Update Faculty Details");
//        JMenuItem updatestudentinfo = createItem("Update Student Details");
//        updateInfo.add(updatefacultyinfo);
//        updateInfo.add(updatestudentinfo);
//
//        // ===== Fee =====
//        JMenu fee = createMenu("Fee Details", "icons/fee.png");
//        mb.add(fee);
//
//        JMenuItem feestructure = createItem("Fee Structure");
//        JMenuItem feeform = createItem("Student Fee Form");
//        fee.add(feestructure);
//        fee.add(feeform);
//
//        // ===== Utility =====
//        JMenu utility = createMenu("Utility", "icons/util.png");
//        mb.add(utility);
//
//        JMenuItem notepad = createItem("Notepad");
//        JMenuItem calc = createItem("Calculator");
//        utility.add(notepad);
//        utility.add(calc);
//
//        // ===== About =====
//        JMenu about = createMenu("About", "icons/about.png");
//        mb.add(about);
//
//        JMenuItem ab = createItem("About");
//        about.add(ab);
//
//        // ===== Exit =====
//        JMenu exit = createMenu("Exit", "icons/exit.png");
//        mb.add(exit);
//
//        JMenuItem ex = createItem("Exit");
//        exit.add(ex);
//
//        setJMenuBar(mb);
//        setVisible(true);
//    }
//
//    private JMenu createMenu(String title, String iconPath) {
//
//    JMenu menu = new JMenu(title);
//    menu.setForeground(Color.blue);
//    menu.setFont(new Font("Tahoma", Font.BOLD, 15));
//
//    java.net.URL imgURL = ClassLoader.getSystemResource(iconPath);
//
//    if (imgURL != null) {
//        menu.setIcon(new ImageIcon(imgURL));
//    } else {
//        System.out.println("Icon not found: " + iconPath);
//    }
//
//    menu.addMouseListener(new MouseAdapter() {
//        public void mouseEntered(MouseEvent e) {
//            menu.setForeground(Color.pink);
//        }
//        public void mouseExited(MouseEvent e) {
//            menu.setForeground(Color.blue);
//        }
//    });
//
//    return menu;
//}
//
//    // ===== Create Styled Menu Item =====
//    private JMenuItem createItem(String title) {
//        JMenuItem item = new JMenuItem(title);
//        item.setBackground(Color.WHITE);
//        item.setFont(new Font("Tahoma", Font.PLAIN, 14));
//        item.addActionListener(this);
//
//        item.addMouseListener(new MouseAdapter() {
//            public void mouseEntered(MouseEvent e) {
//                item.setBackground(new Color(200, 230, 255));
//            }
//
//            public void mouseExited(MouseEvent e) {
//                item.setBackground(Color.WHITE);
//            }
//        });
//
//        return item;
//    }
//
//    // ===== Actions =====
//    public void actionPerformed(ActionEvent ae) {
//
//        String msg = ae.getActionCommand();
//
//        if (msg.equals("Exit")) {
//            System.exit(0);
//
//        } else if (msg.equals("Calculator")) {
//            try { Runtime.getRuntime().exec("calc.exe"); } catch (Exception e) {}
//
//        } else if (msg.equals("Notepad")) {
//            try { Runtime.getRuntime().exec("notepad.exe"); } catch (Exception e) {}
//
//        } else if (msg.equals("New Faculty Information")) {
//            new AddTeacher();
//        } else if (msg.equals("New Student Information")) {
//            new AddStudent();
//        } else if (msg.equals("View Faculty Details")) {
//            new TeacherDetails();
//        } else if (msg.equals("View Student Details")) {
//            new StudentDetails();
//        } else if (msg.equals("Faculty Leave")) {
//            new TeacherLeave();
//        } else if (msg.equals("Student Leave")) {
//            new StudentLeave();
//        } else if (msg.equals("Faculty Leave Details")) {
//            new TeacherLeaveDetails();
//        } else if (msg.equals("Student Leave Details")) {
//            new StudentLeaveDetails();
//        } else if (msg.equals("Update Faculty Details")) {
//            new UpdateTeacher();
//        } else if (msg.equals("Update Student Details")) {
//            new UpdateStudent();
//        } else if (msg.equals("Enter Marks")) {
//            new EnterMarks();
//        } else if (msg.equals("Examination Results")) {
//            new ExaminationDetails();
//        } else if (msg.equals("Fee Structure")) {
//            new FeeStructure();
//        } else if (msg.equals("About")) {
//            new About();
//        } else if (msg.equals("Student Fee Form")) {
//            new StudentFeeForm();
//        }
//    }
//
//    public static void main(String[] args) {
//
//        try {
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (Exception e) {}
//
//        new Project();
//    }
//}