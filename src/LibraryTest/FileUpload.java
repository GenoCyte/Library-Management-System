package LibraryTest;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.awt.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class FileUpload extends JFrame{
    
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    String filePath;
    JPanel mainPanel;
    JPanel p1;
    
    public FileUpload(){
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel(null);
        setContentPane(mainPanel);
        
        p1 = createP1();
        
        p1.setBounds(0, 0, 500, 500);
        
        mainPanel.add(p1);
        
        p1.setVisible(true);
        
        setVisible(true);
    }
    
    private JPanel createP1(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 500, 500);
        
        JLabel l1 = new JLabel("BOOK REGISTER");
        l1.setBounds(180, 50, 140,30);
        panel.add(l1);
        
        JLabel l2 = new JLabel("Name:");
        l2.setBounds(120, 150, 90, 15);
        panel.add(l2);
        
        JTextField name = new JTextField();
        name.setBounds(200, 150, 150, 20);
        panel.add(name);
        
        JLabel l3 = new JLabel("Genre:");
        l3.setBounds(120, 200, 90, 15);
        panel.add(l3);
        
        JTextField genre = new JTextField();
        genre.setBounds(200, 200, 150, 20);
        panel.add(genre);
        
        JButton b1 = new JButton("Select Picture");
        b1.setBounds(180, 250, 140,30);
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);
                
                int res = fileChooser.showOpenDialog(null);
                
                if(res == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    filePath = file.getAbsolutePath();
                    System.out.println(filePath);
                }
                
            }
        });
        panel.add(b1);
        
        JButton b2 = new JButton("Submit");
        b2.setBounds(180, 300, 140,30);
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                bookRegister(name.getText(), genre.getText(), filePath);
            }
        });
        
        panel.add(b2);
        
        return panel;
    }
    
    public void bookRegister(String name, String genre, String image){
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            Statement state = (Statement) conn.createStatement();        

            String insert = "INSERT INTO books (name, genre, image) VALUES('"+name+"', '"+genre+"', '"+image+"');";
            state.executeUpdate(insert);
            JOptionPane.showMessageDialog(mainPanel, "Registration Successful");

        }catch(Exception e){
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new FileUpload();
    }
    
}
