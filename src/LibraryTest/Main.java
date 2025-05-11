package LibraryTest;

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

public class Main extends JFrame{
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    String filePath;
    JPanel mainPanel;
    JPanel p1, p2, p3;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    JPanel topPanel;
    String storedEmail;
    String userName;
    int userID;
    
    
    public Main(String email){
        this.storedEmail = email;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        
        topPanel = TopPanel();
        p1 = createP1();
        p2 = new JPanel();
        p3 = new JPanel();

        topPanel.setBounds(0, 0, screenSize.width, 200);
        p1.setBounds(0, 0, screenSize.width, screenSize.height);
        p2.setBounds(0, 0, screenSize.width, screenSize.height);
        p2.setBounds(0, 0, screenSize.width, screenSize.height);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(p1, BorderLayout.SOUTH);
        mainPanel.add(p2);
        mainPanel.add(p3);
        
        topPanel.setVisible(true);
        p1.setVisible(true);
        p2.setVisible(false);
        p3.setVisible(false);
        
        setVisible(true);
    }
    
    public void getNamebyEmail(String email) {
        userName = "";
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
    
    private JPanel TopPanel(){
        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(screenSize.width, 200));
        
        JLabel l1 = new JLabel("SRMLIB");
        l1.setBounds(90, 40, 200, 50);
        l1.setFont(new Font("Arial", Font.BOLD, 46));
        topPanel.add(l1);
        
        JLabel l2 = new JLabel("LIBRARY MANAGEMENT PORTAL");
        l2.setBounds(70, 90, 300, 20);
        l2.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(l2);
        
        JButton b1 = new JButton("Home");
        b1.setBounds(730, 60, 140,40);
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mainPanel.remove(p1);
                p1 = createP1();      
                p1.setBounds(0, 0, screenSize.width, screenSize.height);
                mainPanel.add(p1);

                p2.setVisible(false);
                p1.setVisible(true);

                mainPanel.revalidate();
                mainPanel.repaint(); 
            }
        });
        topPanel.add(b1);
        
        JButton b2 = new JButton("Search");
        b2.setBounds(870, 60, 140,40);
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mainPanel.remove(p2);
                p2 = showBookList();
                p2.setBounds(0, 0, screenSize.width, screenSize.height);
                mainPanel.add(p2);
                
                p1.setVisible(false);
                p2.setVisible(true);

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        topPanel.add(b2);
        
        JButton b3 = new JButton("My Borrowings");
        b3.setBounds(1010, 60, 140,40);
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            }
        });
        topPanel.add(b3);
        
        JButton b4 = new JButton("Sign Out");
        b4.setBounds(1150, 60, 140,40);
        b4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            }
        });
        topPanel.add(b4);
        
        JPanel line = new JPanel();
        line.setBackground(Color.BLACK);
        line.setPreferredSize(new Dimension(200, 2));
        line.setBounds(30, 140, 1300, 2);
        topPanel.add(line);
        
        return topPanel;
    }
    
    private JPanel createP1(){
        getNamebyEmail(storedEmail);
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(screenSize.width, 530));
        
        JPanel userPanel = new JPanel(null);
        userPanel.setPreferredSize(new Dimension(screenSize.width, 200));
        
        JLabel l3 = new JLabel("HI! " + userName);
        l3.setBounds(70, 20, 200, 40);
        l3.setFont(new Font("Arial", Font.BOLD, 30));
        userPanel.add(l3);
        
        JPanel line2 = new JPanel();
        line2.setBackground(Color.BLACK);
        line2.setPreferredSize(new Dimension(200, 2));
        line2.setBounds(30, 120, 1300, 2);
        userPanel.add(line2);
        
        JLabel l4 = new JLabel("New Released Books");
        l4.setBounds(90, 140, 200, 20);
        l4.setFont(new Font("Arial", Font.BOLD, 16));
        userPanel.add(l4);
        
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        p.add(userPanel, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        
        FeaturedBooks(gridPanel);
        
        return p;
    }
    
    private JPanel showBookList() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(screenSize.width, 600));

        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(screenSize.width, 100));

        JButton b1 = new JButton("Back");
        b1.setBounds(60, 0, 80, 30);
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
        searchField.setBounds(800, 10, 350, 40);
        searchField.setBorder(BorderFactory.createTitledBorder("Search"));
        topPanel.add(searchField);

        JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"All Genres", "Math", "Science", "Business", "History", "Psychology", "Novel", "Philosophy", "Encyclopedia", "Dictionary"});
        genreComboBox.setBounds(1160, 10, 130, 40);
        topPanel.add(genreComboBox);

        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        ActionListener updateDisplay = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBookDisplay(searchField.getText(), genreComboBox.getSelectedItem().toString(), gridPanel);
            }
        };

        searchField.addActionListener(updateDisplay);
        genreComboBox.addActionListener(updateDisplay);

        p.add(topPanel, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);

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
                bookPanel.setPreferredSize(new Dimension(170, 260));

                JLabel imageLabel = new JLabel();
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                imageLabel.setPreferredSize(new Dimension(170, 230));

                if (img != null) {
                    Image scaledImage = img.getScaledInstance(170, 230, Image.SCALE_SMOOTH);
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
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

                bookPanel.add(imageLabel);
                bookPanel.add(nameLabel);

                gridPanel.add(bookPanel);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int rows = (int) Math.ceil(bookCount / 6.0);
        int height = rows * 290;
        gridPanel.setPreferredSize(new Dimension(1000, height));

        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    private void FeaturedBooks(JPanel featured){
        String sql = "SELECT id, name, image, genre FROM books LIMIT 5";
        
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("id"); // get the id
                String bookName = rs.getString("name");
                InputStream is = rs.getBinaryStream("image");
                BufferedImage img = ImageIO.read(is);

                JPanel bookPanel = new JPanel();
                bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));
                bookPanel.setOpaque(false);
                bookPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                bookPanel.setPreferredSize(new Dimension(180, 270));

                JLabel imageLabel = new JLabel();
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                imageLabel.setPreferredSize(new Dimension(100, 240));

                if (img != null) {
                    Image scaledImage = img.getScaledInstance(150, 220, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setText("No Image");
                }

                // Mouse click to show details
                imageLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        JPanel detailPanel = createHomeBookDetailPanel(bookId);

                        // Remove the current panel and display the new detail panel
                        mainPanel.remove(p2);
                        p2 = detailPanel;
                        p2.setBounds(0, 0, screenSize.width, screenSize.height);
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
                nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

                bookPanel.add(imageLabel);
                bookPanel.add(nameLabel);

                featured.add(bookPanel);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private JPanel createHomeBookDetailPanel(int bookId) {
        JPanel p = new JPanel();
        p.setLayout(null);
        String bookName = "";
        String genre = "";

        // Labels for book details
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(400, 50, 160, 220);
        p.add(imageLabel);
        
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(400, 280, 400, 30);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(nameLabel);

        JLabel genreLabel = new JLabel("Genre: ");
        genreLabel.setBounds(400, 310, 400, 30);
        genreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(genreLabel);

        JLabel dateLabel = new JLabel("Date: ");
        dateLabel.setBounds(400, 340, 400, 30);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 15));
        p.add(dateLabel);
        
        JButton borrowButton = new JButton("Borrow");
        borrowButton.setBounds(400, 380, 100, 25);
        borrowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //bookBorrow(bookId);
            }});
        p.add(borrowButton);
        
        JLabel descriptHeaderLabel = new JLabel("Description");
        descriptHeaderLabel.setBounds(700, 60, 400, 30);
        descriptHeaderLabel.setFont(new Font("Arial", Font.BOLD, 20));
        p.add(descriptHeaderLabel);
        
        JLabel descriptLabel = new JLabel();
        descriptLabel.setBounds(700, 100, 280, 300);
        descriptLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descriptLabel.setFont(new Font("Arial", Font.PLAIN, 14));
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
                    Image scaledImage = img.getScaledInstance(160, 220, Image.SCALE_SMOOTH);
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
        backButton.setBounds(60, 0, 100, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(p1);    // Remove old p2 if exists
                p1 = createP1();      
                p1.setBounds(0, 0, screenSize.width, screenSize.height);
                mainPanel.add(p1);

                p.setVisible(false);
                p1.setVisible(true);

                mainPanel.revalidate();
                mainPanel.repaint();       // Repaint the main panel to reflect changes
            }
        });
        p.add(backButton);

        return p;
    }
    
    private JPanel createBookDetailPanel(int bookId) {
        JPanel p = new JPanel();
        p.setLayout(null);
        String bookName = "";
        String genre = "";

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(400, 50, 160, 220);
        p.add(imageLabel);
        
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(400, 280, 400, 30);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(nameLabel);

        JLabel genreLabel = new JLabel("Genre: ");
        genreLabel.setBounds(400, 310, 400, 30);
        genreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(genreLabel);

        JLabel dateLabel = new JLabel("Date: ");
        dateLabel.setBounds(400, 340, 400, 30);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 15));
        p.add(dateLabel);
        
        JButton borrowButton = new JButton("Borrow");
        borrowButton.setBounds(400, 380, 100, 25);
        borrowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //bookBorrow(bookId);
            }});
        p.add(borrowButton);
        
        JLabel descriptHeaderLabel = new JLabel("Description");
        descriptHeaderLabel.setBounds(700, 60, 400, 30);
        descriptHeaderLabel.setFont(new Font("Arial", Font.BOLD, 20));
        p.add(descriptHeaderLabel);
        
        JLabel descriptLabel = new JLabel();
        descriptLabel.setBounds(700, 100, 280, 300);
        descriptLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descriptLabel.setFont(new Font("Arial", Font.PLAIN, 14));
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
                    Image scaledImage = img.getScaledInstance(160, 220, Image.SCALE_SMOOTH);
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
                mainPanel.remove(p2);
                p2 = showBookList();
                p2.setBounds(0, 0, screenSize.width, screenSize.height);
                mainPanel.add(p2);

                p.setVisible(false);
                p2.setVisible(true);

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        p.add(backButton);

        return p;
    }
    
    public static void main(String[] args) {
    }
    
}
