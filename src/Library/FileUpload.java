package Library;

import LibraryTest.*;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.awt.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileUpload extends JFrame{
    
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    String filePath;
    JPanel mainPanel;
    JPanel p1, p2, p3;
    String storedEmail;
    String userName;
    int userID;
    
    public FileUpload(){
       // this.storedEmail = email; // Now it's properly passed
        setSize(515,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(null);
        setContentPane(mainPanel);

        p1 = createP1();
        p2 = new JPanel();
        p3 = new JPanel();

        p1.setBounds(0, 0, 500, 500);
        p2.setBounds(0, 0, 500, 500);
        p2.setBounds(0, 0, 500, 500);
        
        mainPanel.add(p1);
        mainPanel.add(p2);
        mainPanel.add(p3);

        p1.setVisible(true);
        p2.setVisible(false);
        p3.setVisible(false);

        setVisible(true);
    }
    
    public void getNamebyEmail(String email) {
        userName = ""; // must be declared outside try block
        String query = "SELECT id, name FROM user WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userID = rs.getInt("id");
                    userName = rs.getString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private JPanel createP1(){
        getNamebyEmail(storedEmail);
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(0, 0, 500, 500);
        
        JLabel userLabel = new JLabel(userName);
        userLabel.setBounds(400, 20, 140,30);
        p.add(userLabel);
        
        JLabel l1 = new JLabel("BOOK REGISTER");
        l1.setBounds(180, 50, 140,30);
        p.add(l1);
        
        JLabel l2 = new JLabel("Name:");
        l2.setBounds(120, 100, 90, 15);
        p.add(l2);
        
        JTextField name = new JTextField();
        name.setBounds(200, 100, 150, 20);
        p.add(name);
        
        JLabel l3 = new JLabel("Genre:");
        l3.setBounds(120, 150, 90, 15);
        p.add(l3);
        
        JTextField genre = new JTextField();
        genre.setBounds(200, 150, 150, 20);
        p.add(genre);
        
        JLabel l4 = new JLabel("Description:");
        l4.setBounds(120, 200, 90, 15);
        p.add(l4);
        
        JTextField description = new JTextField();
        description.setBounds(200, 200, 150, 20);
        p.add(description);
        
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
                bookRegister(name.getText(), genre.getText(), description.getText(), filePath);
                name.setText("");
                genre.setText("");
                description.setText("");
                previewLabel.setIcon(null);
            }
        });
        p.add(b2);
        
        JButton b3 = new JButton("Show Books");
        b3.setBounds(180, 350, 140,30);
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mainPanel.remove(p2);    // Remove old p2 if exists
                p2 = showBookList();         // Recreate p2 fresh
                p2.setBounds(0, 0, 500, 500);
                mainPanel.add(p2);        // Add the new p2
                
                p1.setVisible(false);
                p2.setVisible(true);

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        p.add(b3);
        
        JButton b4 = new JButton("Show Borrowed");
        b4.setBounds(180, 400, 140,30);
        b4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){              
                mainPanel.remove(p3);    // Remove old p2 if exists
                p3 = createP3();         // Recreate p2 fresh
                p3.setBounds(0, 0, 500, 500);
                mainPanel.add(p3);        // Add the new p2
                
                p1.setVisible(false);
                p3.setVisible(true);

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        p.add(b4);
        
        return p;
    }
    
    private JPanel createP3(){
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(500, 500));
        
        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(500, 100));
        
        JButton b1 = new JButton("Back");
        b1.setBounds(10, 10, 80, 30);
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){              
                p.setVisible(false);
                p1.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        topPanel.add(b1);
        
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setMaximumSize(new Dimension(480, 150));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        p.add(scrollPane, BorderLayout.CENTER);
        
        JLabel l1 = new JLabel("Pogi");
        l1.setBounds(100,100,100,20);
        p.add(l1);
        
        p.add(topPanel, BorderLayout.NORTH);
        
        return p;
    }
    
    public JPanel showBookList() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(500, 500));

        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(500, 100));

        JButton b1 = new JButton("Back");
        b1.setBounds(10, 10, 80, 30);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setVisible(false);
                p1.setVisible(true);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        topPanel.add(b1);

        JTextField searchField = new JTextField();
        searchField.setBounds(100, 10, 250, 40);
        searchField.setBorder(BorderFactory.createTitledBorder("Search"));
        topPanel.add(searchField);

        JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"All Genres", "Math", "Science", "Business", "History", "Psychology", "Novel", "Philosophy", "Encyclopedia", "Dictionary"});
        genreComboBox.setBounds(360, 10, 130, 40);
        topPanel.add(genreComboBox);

        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        p.add(scrollPane, BorderLayout.CENTER);

        ActionListener updateDisplay = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBookDisplay(searchField.getText(), genreComboBox.getSelectedItem().toString(), gridPanel);
            }
        };

        searchField.addActionListener(updateDisplay);
        genreComboBox.addActionListener(updateDisplay);

        p.add(topPanel, BorderLayout.NORTH);

        // Initially display all books
        updateBookDisplay("", "All Genres", gridPanel);

        return p;
    }

    private void updateBookDisplay(String searchQuery, String genreFilter, JPanel gridPanel) {
        gridPanel.removeAll();

        int bookCount = 0;
        String sql = "SELECT id, name, image, genre FROM books WHERE name LIKE ? AND (? = 'All Genres' OR genre = ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, searchQuery + "%");
            stmt.setString(2, genreFilter);
            stmt.setString(3, genreFilter);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookCount++;

                int bookId = rs.getInt("id"); // get the id
                String bookName = rs.getString("name");
                InputStream is = rs.getBinaryStream("image");
                BufferedImage img = ImageIO.read(is);

                JPanel bookPanel = new JPanel();
                bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));
                bookPanel.setOpaque(false);
                bookPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                bookPanel.setPreferredSize(new Dimension(140, 200));

                JLabel imageLabel = new JLabel();
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                imageLabel.setPreferredSize(new Dimension(100, 150));

                if (img != null) {
                    Image scaledImage = img.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setText("No Image");
                }

                // Mouse click to show details
                imageLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        // Create a new panel to display book details
                        JPanel detailPanel = createBookDetailPanel(bookId);

                        // Remove the current panel and display the new detail panel
                        mainPanel.remove(p2);
                        p2 = detailPanel;
                        p2.setBounds(0, 0, 500, 500);
                        mainPanel.add(p2);

                        p1.setVisible(false);
                        p2.setVisible(true);

                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                });

                JLabel nameLabel = new JLabel(bookName);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

                bookPanel.add(imageLabel);
                bookPanel.add(nameLabel);

                gridPanel.add(bookPanel);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int rows = (int) Math.ceil(bookCount / 3.0);
        int height = rows * 220;
        gridPanel.setPreferredSize(new Dimension(500, height));

        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    private JPanel createBookDetailPanel(int bookId) {
        JPanel p = new JPanel();
        p.setLayout(null);
        String bookName = "";
        String genre = "";

        // Labels for book details
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(30, 70, 120, 170);
        p.add(imageLabel);
        
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(30, 260, 400, 30);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(nameLabel);

        JLabel genreLabel = new JLabel("Genre: ");
        genreLabel.setBounds(30, 280, 400, 30);
        genreLabel.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(genreLabel);

        JLabel dateLabel = new JLabel("Date: ");
        dateLabel.setBounds(30, 300, 400, 30);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(dateLabel);
        
        JButton borrowButton = new JButton("Borrow");
        borrowButton.setBounds(30, 330, 100, 25);
        borrowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookBorrow(bookId);
            }});
        p.add(borrowButton);
        
        JLabel descriptHeaderLabel = new JLabel("Description");
        descriptHeaderLabel.setBounds(200, 70, 400, 30);
        descriptHeaderLabel.setFont(new Font("Arial", Font.BOLD, 15));
        p.add(descriptHeaderLabel);
        
        JLabel descriptLabel = new JLabel();
        descriptLabel.setBounds(200, 100, 280, 300);
        descriptLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptLabel.setHorizontalAlignment(SwingConstants.LEFT);
        p.add(descriptLabel);

        // Fetch book details from database
        String sql = "SELECT name, genre, date, description, image FROM books WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bookName = rs.getString("name");
                genre = rs.getString("genre");
                Date date = rs.getDate("date");
                String descript = rs.getString("description");
                InputStream is = rs.getBinaryStream("image");
                BufferedImage img = ImageIO.read(is);

                // Update labels
                nameLabel.setText("Name: " + bookName);
                genreLabel.setText("Genre: " + genre);
                dateLabel.setText("Date: " + date.toString());
                descriptLabel.setText("<html>" + descript + "</html>");

                // Set image
                if (img != null) {
                    Image scaledImage = img.getScaledInstance(120, 170, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setText("No Image Available");
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Back button to return to book list (p2 panel)
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 100, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(p2);    // Remove old p2 if exists
                p2 = showBookList();         // Recreate p2 fresh
                p2.setBounds(0, 0, 500, 500);
                mainPanel.add(p2);

                p.setVisible(false);
                p2.setVisible(true);

                mainPanel.revalidate();
                mainPanel.repaint();    // Repaint the main panel to reflect changes
            }
        });
        p.add(backButton);

        return p;
    }
    
    public void bookBorrow(int bookId){
        String bookName = "";
        String genre = "";
        String sql = "SELECT name, genre FROM books WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, bookId);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                bookName = rs.getString("name");
                genre = rs.getString("genre");
            }
            
            stmt.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            String insert = "INSERT INTO borrowed_books "
                    + "(user_id, user_name, book_id, book_name, genre, date_borrowed, deadline) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";       
            PreparedStatement stmt = conn.prepareStatement(insert);

            LocalDate currentDate = LocalDate.now();
            LocalDate deadline = currentDate.plusWeeks(1);
            Date sqlDate = Date.valueOf(currentDate);
            Date sqlDeadline = Date.valueOf(deadline);
            
            stmt.setInt(1, userID);
            stmt.setString(2, userName);
            stmt.setInt(3, bookId);
            stmt.setString(4, bookName);
            stmt.setString(5, genre);
            stmt.setDate(6, sqlDate);
            stmt.setDate(7, sqlDeadline);
            
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(mainPanel, "Borrowed Successful");

            stmt.close();
            conn.close();
       }catch(Exception e){
           e.printStackTrace();
       }
    }
    
    public void bookRegister(String name, String genre, String description, String image) {
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            String insert = "INSERT INTO books (name, genre, date, description, image) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);

            // Convert image file to binary stream
            File imageFile = new File(image);
            FileInputStream fis = new FileInputStream(imageFile);

            // Get current date
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);
            Date sqlDate = Date.valueOf(currentDate);  // Convert LocalDate to SQL Date

            // Set parameters in the prepared statement
            stmt.setString(1, name); // name
            stmt.setString(2, genre); // genre
            stmt.setDate(3, sqlDate); // date
            stmt.setString(4, description);
            stmt.setBinaryStream(5, fis, (int) imageFile.length()); // image (binary stream)

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
    }
    
}
