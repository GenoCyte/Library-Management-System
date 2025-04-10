package Library;

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
    static String dbUrl = "jdbc:mysql://localhost:3306/library";
    static String dbUser = "root";
    static String dbPass = "";
    static JFrame f = new JFrame();
    
    public LibraryTest(){
        JPanel p = new JPanel();
        f.add(p);
        JPanel p2 = new JPanel();
        f.add(p2);
        p2.setVisible(false);
        
        
        JButton b = new JButton("Login");
        JButton b2 = new JButton("Forgot password");
        JLabel l1 = new JLabel("Email: ");
        JLabel l2 = new JLabel("Password: ");
        JTextField name = new JTextField();
        JTextField pass = new JTextField();
        
        JLabel l3 = new JLabel("Halimaw");
        JButton b4 = new JButton("Back");
        
        p.setLayout(null);
        
        l1.setBounds(120, 100, 90, 15);
        p.add(l1);
        l2.setBounds(120, 200, 90, 15);
        p.add(l2);
        name.setBounds(200, 100, 150, 20);
        p.add(name);
        pass.setBounds(200, 200, 150, 20);
        p.add(pass);
        
        b.setBounds(210, 250, 80,30);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                login(name.getText(),pass.getText());
            }
        });
        p.add(b);
        
        b2.setBounds(180, 300, 140,30);
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                p.setVisible(false);
                p2.setVisible(true);
                f.revalidate();
                f.repaint();
            }
        });
        p.add(b2);
        
        p2.setLayout(null);
        
        l3.setBounds(210, 200, 90,15);
        p2.add(l3);
        b4.setBounds(30, 30, 80, 30);
        b4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                f.remove(p2);
                f.add(p);
                f.revalidate();
            }
        });
        p2.add(b4);
        
        f.setVisible(true);
        f.setSize(500,500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void login(String name, String pass){
        String query = "SELECT * FROM user WHERE name = ? AND pass = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(f, "Login Successful");
            }else{
                JOptionPane.showMessageDialog(f, "Username or password are incorrect");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void signup(String name, String pass){
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            Statement state = (Statement) conn.createStatement();        
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            int otp = Integer.parseInt(String.format("%06d",number));
            
            
            String insert = "INSERT INTO user (name, pass, otp) VALUES('"+name+"', '"+pass+"', "+otp+");";
            state.executeUpdate(insert);
            
            System.out.println("Registered Successfully");
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    public static boolean nameChecker(String name){
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