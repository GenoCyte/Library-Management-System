package Library;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class LibraryTest {
    static String dbUrl = "jdbc:mysql://localhost:3306/Library";
    static String dbUser = "root";
    static String dbPass = "";
    
    public static void login(String getName, String getPass){
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);          
            Statement login = conn.createStatement();
            String sql = "SELECT * FROM user;";
            ResultSet rs = login.executeQuery(sql);

            while(rs.next()){
                if(rs.getString(1).equals(getName) && rs.getString(2).equals(getPass)){
                    System.out.println("Login Successful");
                }else{
                    System.out.println("Username and Password are incorrect");
                }
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
    }

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
                    login(username, pass);
                    break;
                case 2:
                    System.out.print("Username: ");
                    String username2 = sc.nextLine();
                    System.out.print("Password: ");
                    String pass2 = sc.nextLine();
                    signup(username2, pass2);
                    break;
                case 3:
                    System.out.println("Terminated");
                    return;
            }
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
}