package Library;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class LibraryTest {
    static String dbUrl = "jdbc:mysql://localhost:3306/library";
    static String dbUser = "root";
    static String dbPass = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
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
        }
    }
    
    public static boolean login(String name, String pass){
        String query = "SELECT * FROM user WHERE name = ? AND pass = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Returns true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
}