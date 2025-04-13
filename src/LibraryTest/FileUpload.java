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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FileUpload extends JFrame{
    
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    String filePath;
    JPanel mainPanel;
    JPanel p1, p2;
    
    public FileUpload(){
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel(null);
        setContentPane(mainPanel);
        
        p1 = createP1();
        p2 = createP2();
        
        p1.setBounds(0, 0, 500, 500);
        p2.setBounds(0, 0, 500, 500);
        
        mainPanel.add(p1);
        mainPanel.add(p2);
        
        p1.setVisible(true);
        p2.setVisible(false);
        
        setVisible(true);
    }
    
    private JPanel createP1(){
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(0, 0, 500, 500);
        
        JLabel l1 = new JLabel("BOOK REGISTER");
        l1.setBounds(180, 50, 140,30);
        p.add(l1);
        
        JLabel l2 = new JLabel("Name:");
        l2.setBounds(120, 150, 90, 15);
        p.add(l2);
        
        JTextField name = new JTextField();
        name.setBounds(200, 150, 150, 20);
        p.add(name);
        
        JLabel l3 = new JLabel("Genre:");
        l3.setBounds(120, 200, 90, 15);
        p.add(l3);
        
        JTextField genre = new JTextField();
        genre.setBounds(200, 200, 150, 20);
        p.add(genre);
        
        JLabel previewLabel = new JLabel(); // Add this above the buttons
        previewLabel.setBounds(360, 150, 100, 150); // Position it nicely
        p.add(previewLabel);
        
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
                    System.out.println("Selected image: " + filePath);

                    // Preview image
                    ImageIcon icon = new ImageIcon(filePath);
                    Image scaledImage = icon.getImage().getScaledInstance(previewLabel.getWidth(), previewLabel.getHeight(), Image.SCALE_SMOOTH);
                    previewLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
        });
        p.add(b1);
        
        JButton b2 = new JButton("Submit");
        b2.setBounds(180, 300, 140,30);
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                bookRegister(name.getText(), genre.getText(), filePath);
            }
        });
        
        p.add(b2);
        
        return p;
    }
    
    private JPanel createP2(){
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(0, 0, 500, 500);
        
        JLabel l1 = new JLabel("BOOK SHOW");
        l1.setBounds(180, 50, 140,30);
        p.add(l1);
        
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(150,150,100,150);
        p.add(imageLabel);
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            Statement state = (Statement) conn.createStatement();        

            String sql = "SELECT image FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 3);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                InputStream is = rs.getBinaryStream("image");
                BufferedImage img = ImageIO.read(is);
                if (img != null) {
                    Image scaledImage = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setText("Failed to load image.");
                }
            }

        }catch(Exception e){
            System.err.println(e);
        }
        
        return p;
    }
    
    public void bookRegister(String name, String genre, String image){
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            String insert = "INSERT INTO books (name, genre, image) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setString(1, name);
            stmt.setString(2, genre);

            File imageFile = new File(image);
            FileInputStream fis = new FileInputStream(imageFile);
            stmt.setBinaryStream(3, fis, (int) imageFile.length());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(mainPanel, "Registration Successful");

            fis.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.err.println("Error inserting image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileUpload();
    }
    
}
