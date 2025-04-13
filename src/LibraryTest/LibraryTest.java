package LibraryTest;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LibraryTest extends JFrame{
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    JPanel mainPanel;
    JPanel p1, p2, p3;
    
    public LibraryTest(){
        setVisible(true);
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel(null);
        setContentPane(mainPanel);
        
        p1 = createP1();
        p2 = createP2();
        p3 = createP3();
        
        p1.setBounds(0, 0, 500, 500);
        p2.setBounds(0, 0, 500, 500);
        p3.setBounds(0, 0, 500, 500);
        
        mainPanel.add(p1);
        mainPanel.add(p2);
        mainPanel.add(p3);
        
        p1.setVisible(true);
        p2.setVisible(false);
        p3.setVisible(false);
    }
    
    private JPanel createP1(){
        JPanel p = new JPanel();
        p.setLayout(null);
        
        JLabel l1 = new JLabel("Email: ");
        l1.setBounds(120, 100, 90, 15);
        p.add(l1);
        
        JTextField name = new JTextField();
        name.setBounds(200, 100, 150, 20);
        p.add(name);
        
        JLabel l2 = new JLabel("Password: ");
        l2.setBounds(120, 150, 90, 15);
        p.add(l2);
        
        JTextField pass = new JTextField();
        pass.setBounds(200, 150, 150, 20);
        p.add(pass);
        
        JButton b = new JButton("Login");
        b.setBounds(210, 200, 80,30);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                login(name.getText(),pass.getText());
                name.setText("");
                pass.setText("");
            }
        });
        p.add(b);
        
        JButton b2 = new JButton("Sign Up");
        b2.setBounds(210, 250, 80,30);
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                p1.setVisible(false);
                p3.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        p.add(b2);
        
        JButton b3 = new JButton("Forgot password");
        b3.setBounds(180, 300, 140,30);
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                p1.setVisible(false);
                p2.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        p.add(b3);
        
        return p;
    }
    
    private JPanel createP2(){
        JPanel p = new JPanel();
        p.setLayout(null);
        
        JLabel l1 = new JLabel("Halimaw");
        l1.setBounds(210, 200, 90,15);
        p.add(l1);
        
        JButton b1 = new JButton("Back");
        b1.setBounds(30, 30, 80, 30);
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                p2.setVisible(false);
                p1.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        p.add(b1);
        
        return p;
    }
    
    private JPanel createP3(){
        JPanel p = new JPanel();
        p.setLayout(null);
        
        JButton b1 = new JButton("Back");
        b1.setBounds(20, 20, 80, 30);
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                p3.setVisible(false);
                p1.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        p.add(b1);
        
        JLabel l1 = new JLabel("SIGN UP");
        l1.setBounds(220, 50, 80, 30);
        p.add(l1);
        
        JLabel l2 = new JLabel("Name:");
        l2.setBounds(120, 150, 90, 15);
        p.add(l2);
        
        JTextField name = new JTextField();
        name.setBounds(200, 150, 150, 20);
        p.add(name);
        
        JLabel l3 = new JLabel("Email:");
        l3.setBounds(120, 200, 90, 15);
        p.add(l3);
        
        JTextField email = new JTextField();
        email.setBounds(200, 200, 150, 20);
        p.add(email);
        
        JLabel l4 = new JLabel("Password:");
        l4.setBounds(120, 250, 90, 15);
        p.add(l4);
        
        JTextField password = new JTextField();
        password.setBounds(200, 250, 150, 20);
        p.add(password);
        
        JButton b2 = new JButton("Sign Up");
        b2.setBounds(205, 300, 80, 30);
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                signup(name.getText(),email.getText(),password.getText());
                name.setText("");
                email.setText("");
                password.setText("");
            }
        });
        p.add(b2);
        
        return p;
    }
    
    public void login(String email, String pass){
        String query = "SELECT * FROM user WHERE email = ? AND pass = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(mainPanel, "Login Successful");
            }else{
                JOptionPane.showMessageDialog(mainPanel, "Username or password are incorrect");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void signup(String name, String email ,String pass){
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            Statement state = (Statement) conn.createStatement();        
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            int otp = Integer.parseInt(String.format("%06d",number));
            
            if(nameChecker(name)){
                JOptionPane.showMessageDialog(mainPanel, "Name is Already Used");
            }else{
                String insert = "INSERT INTO user (name, email ,pass, otp) VALUES('"+name+"', '"+email+"', '"+pass+"', "+otp+");";
                state.executeUpdate(insert);
                JOptionPane.showMessageDialog(mainPanel, "Registration Successful");
            }
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    public boolean nameChecker(String name){
        String query = "SELECT name FROM user WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Returns true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        //String body = "Ako Ay MAy Lobo";
        //SendEmail send = new SendEmail("mauricearlei723@gmail.com", "otp", body);
        new LibraryTest();
        /*Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("1. Login\n2. SignUp\n3. Exit");
            System.out.print("Choose Operation: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    System.out.print("Username: ");
                    String username = sc.nextLine();
                    System.out.print("Password: ");
                    String pass = sc.nextLine();
                    if(login(username, pass)){
                        System.out.println("Login Successful");
                    }else{
                        System.out.println("Username and Password are incorrect");
                    }
                    break;
                case 2:
                    System.out.print("Username: ");
                    String username2 = sc.nextLine();
                    System.out.print("Password: ");
                    String pass2 = sc.nextLine();
                    if(nameChecker(username2)){
                        System.out.println("Name is already used");
                        break;
                    }
                    signup(username2, pass2);
                    break;
                case 3:
                    System.out.println("Terminated");
                    return;
            }
        }*/
    }
}