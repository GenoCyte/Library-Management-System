/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Andre
 */
public class User extends javax.swing.JFrame {
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    JPanel showBookPanel;
    JPanel showBorrowedPanel;
    String storedEmail;
    String userName;
    int userID;
    private javax.swing.JLabel refresh;
    
    public User(String email) {
        this.storedEmail = email;
        initComponents();
        getNamebyEmail(storedEmail);
        
        detailsPanel.setVisible(false);
        
        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        featuredBooks.add(scrollPane, BorderLayout.CENTER);
        
        FeaturedBooks(gridPanel);
        
        searchPanel.setLayout(new BorderLayout());
        showBookPanel = showBookList();
        searchPanel.add(showBookPanel, BorderLayout.CENTER);
        
        borrowingsPanel.setLayout(new BorderLayout());
        showBorrowedPanel = showBorrowedBooksPanel();
        borrowingsPanel.add(showBorrowedPanel, BorderLayout.CENTER);
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
        userNameLabel.setText("Hi! " + userName);
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
                        detailsPanel = createHomeBookDetailPanel(bookId);

                        // Replace the panel in the selected tab
                        int selectedIndex = tabbedPane.getSelectedIndex();
                        tabbedPane.setComponentAt(selectedIndex, detailsPanel);

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
                String bookName = rs.getString("name");
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
                
                JButton borrowButton = new JButton("Borrow");
                borrowButton.setBounds(400, 380, 100, 25);
                borrowButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        insertPendingBorrow(userID, userName, bookId, bookName);
                    }});
                p.add(borrowButton);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Back button to return to book list (p2 panel)
        JButton backButton = new JButton("Back");
        backButton.setBounds(60, 10, 120, 40);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                tabbedPane.setComponentAt(selectedIndex, homePanel);
                tabbedPane.revalidate();
                tabbedPane.repaint();
            }
        });
        p.add(backButton);

        return p;
    }
    
    
    private JPanel showBookList() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(1190, 575));

        JLabel l1 = new JLabel("Search Book");
        l1.setBounds(50, 15, 150, 30);
        l1.setFont(new Font("Arial", Font.PLAIN, 26));
        p.add(l1);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setPreferredSize(new Dimension(1190, 80));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(350, 40));
        topPanel.add(searchField);

        JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"All Genres", "Math", "Science", "Business", "History", "Psychology", "Novel", "Philosophy", "Encyclopedia", "Dictionary"});
        genreComboBox.setPreferredSize(new Dimension(130, 40));
        topPanel.add(genreComboBox);

        JPanel gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 20));
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
                bookPanel.setPreferredSize(new Dimension(170, 280));

                JLabel imageLabel = new JLabel();
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                imageLabel.setPreferredSize(new Dimension(170, 250));

                if (img != null) {
                    Image scaledImage = img.getScaledInstance(170, 250, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setText("No Image");
                }

                // Mouse click to show details
                imageLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        detailsPanel = createBookDetailPanel(bookId);

                        int selectedIndex = tabbedPane.getSelectedIndex();
                        tabbedPane.setComponentAt(selectedIndex, detailsPanel);
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

        int rows = (int) Math.ceil(bookCount / 5.0);
        int height = rows * 320;
        gridPanel.setPreferredSize(new Dimension(1100, height));

        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    private JPanel createBookDetailPanel(int bookId) {
        JPanel p = new JPanel();
        p.setLayout(null);
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
        String sql = "SELECT name, genre, date, description, image, status FROM books WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String bookName = rs.getString("name");
                genre = rs.getString("genre");
                Date date = rs.getDate("date");
                String descript = rs.getString("description");
                InputStream is = rs.getBinaryStream("image");
                BufferedImage img = ImageIO.read(is);
                int status = rs.getInt("status");

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
                
                JButton borrowButton = new JButton("Borrow");
                borrowButton.setBounds(400, 380, 100, 25);
                borrowButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        insertPendingBorrow(userID, userName, bookId, bookName);
                    }});
                p.add(borrowButton);
                
                if(status == 1){
                    borrowButton.setEnabled(false);
                    borrowButton.setText("Unavailable");
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Back button to return to book list (p2 panel)
        JButton backButton = new JButton("Back");
        backButton.setBounds(60, 10, 120, 40);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                tabbedPane.setComponentAt(selectedIndex, searchPanel);
                
                tabbedPane.revalidate();
                tabbedPane.repaint();
            }
        });
        p.add(backButton);

        return p;
    }
    
    private JPanel showBorrowedBooksPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(1190, 575));

         JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1190, 80));
        
        JLabel l1 = new JLabel("Borrowed Books");
        l1.setBounds(50, 15, 200, 30);
        l1.setFont(new Font("Arial", Font.PLAIN, 26));
        p.add(l1);
        
        refresh = new javax.swing.JLabel();

        refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GuiTest/Pics/refresh-arrow.png")));
        refresh.setBounds(1080, 15, 30, 30);
        p.add(refresh);
        
        refresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                borrowingsPanel.revalidate();
                borrowingsPanel.repaint();
            }
        });


        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 150, 10, 150)); // spacing on left & right

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPanel.removeAll(); // clear old entries
                loadBorrowedBooks(contentPanel); // reload
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });

        // Load initial data
        loadBorrowedBooks(contentPanel);

        // Final panel assembly
        p.add(topPanel, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);

        return p;
    }
    
    private void loadBorrowedBooks(JPanel contentPanel) {
        String sql = "SELECT bb.book_id, b.name, b.genre, b.image, bb.date_borrowed, bb.deadline " +
                     "FROM borrowed_books bb " +
                     "JOIN books b ON bb.book_id = b.id " +
                     "WHERE bb.user_id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String genre = rs.getString("genre");
                String dateBorrowed = rs.getString("date_borrowed");
                String deadline = rs.getString("deadline");
                int bookId = rs.getInt("book_id");

                BufferedImage img = null;
                InputStream is = rs.getBinaryStream("image");
                if (is != null) img = ImageIO.read(is);

                ImageIcon icon = null;
                if (img != null) {
                    Image scaledImage = img.getScaledInstance(120, 160, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                }

                JPanel rowPanel = new JPanel(new BorderLayout(15, 0));
                rowPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                rowPanel.setBackground(Color.WHITE);
                rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

                JLabel imageLabel = new JLabel();
                imageLabel.setPreferredSize(new Dimension(120, 160));
                if (icon != null) {
                    imageLabel.setIcon(icon);
                } else {
                    imageLabel.setText("No Image");
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);
                }
                rowPanel.add(imageLabel, BorderLayout.WEST);

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setOpaque(false);
                Font font = new Font("Arial", Font.PLAIN, 14);

                JLabel nameLabel = new JLabel(name);
                nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                JLabel genreLabel = new JLabel("Genre: " + genre);
                genreLabel.setFont(font);
                JLabel borrowedLabel = new JLabel("Borrowed: " + dateBorrowed);
                borrowedLabel.setFont(font);
                JLabel deadlineLabel = new JLabel("Deadline: " + deadline);
                deadlineLabel.setFont(font);
                JLabel penaltyLabel = new JLabel("Penalty: " + 0);
                penaltyLabel.setFont(font);

                JButton returnButton = new JButton("Return Book");
                returnButton.addActionListener(e -> {
                    System.out.println("Returning book ID: " + bookId);
                    // Add return logic
                });

                infoPanel.add(nameLabel);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(genreLabel);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(borrowedLabel);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(deadlineLabel);
                infoPanel.add(Box.createVerticalStrut(5));
                infoPanel.add(penaltyLabel);
                infoPanel.add(Box.createVerticalStrut(10));
                infoPanel.add(returnButton);

                rowPanel.add(infoPanel, BorderLayout.CENTER);

                contentPanel.add(rowPanel);
                contentPanel.add(Box.createVerticalStrut(15));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(contentPanel, "Failed to load borrowed books: " + ex.getMessage());
        }
    }
    
    public void insertPendingBorrow(int userId, String userName, int bookId, String bookName) {
        String sql = "INSERT INTO pending_borrows (user_id, user_name, book_id, book_name, time, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalTime currentTime = LocalTime.now();
            LocalDate currentDate = LocalDate.now();

            stmt.setInt(1, userId);
            stmt.setString(2, userName);
            stmt.setInt(3, bookId);
            stmt.setString(4, bookName);
            stmt.setTime(5, Time.valueOf(currentTime));
            stmt.setDate(6, Date.valueOf(currentDate));

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(mainPanel, "Successfully reserve the book. Please Proceed to the Library Counter");
            } else {
                System.out.println("Failed to insert record.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        jFrame1 = new javax.swing.JFrame();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        canvas1 = new java.awt.Canvas();
        jButton3 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        mainPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        exitButton = new javax.swing.JLabel();
        notifButton = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        detailsPanel = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        homePanel = new javax.swing.JPanel();
        userNameLabel = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        featuredBooks = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        borrowingsPanel = new javax.swing.JPanel();

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel3.setText("SRMLIB");

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel4.setText("LIBRARY MANAGEMENT PORTAL");

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("X");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Home");

        jButton8.setBackground(new java.awt.Color(0, 0, 0));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Book Search");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 0, 0));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Borrow");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(0, 0, 0));
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("My Borrowings");

        jButton11.setBackground(new java.awt.Color(0, 0, 0));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Sign out");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        jLabel5.setText("Hi,");

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        jLabel6.setText("Renz!");

        jLabel7.setText("jLabel5");

        jLabel14.setText("jLabel5");

        jLabel15.setText("jLabel5");

        jLabel16.setText("jLabel5");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(79, 79, 79)
                                .addComponent(jLabel3))
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(jLabel4)))
                        .addGap(26, 26, 26)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jFrame1Layout.createSequentialGroup()
                                .addGap(109, 109, 109)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jFrame1Layout.createSequentialGroup()
                                .addGap(700, 700, 700)
                                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(61, 61, 61)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createSequentialGroup()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(45, 45, 45)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(91, 91, 91)
                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topPanel.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel1.setText("SRMLIB");
        topPanel.add(jLabel1);
        jLabel1.setBounds(80, 10, 146, 50);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel2.setText("LIBRARY MANAGEMENT PORTAL");
        topPanel.add(jLabel2);
        jLabel2.setBounds(30, 60, 246, 19);

        jSeparator8.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        topPanel.add(jSeparator8);
        jSeparator8.setBounds(0, 100, 1190, 10);

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Book Search");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        topPanel.add(jButton4);
        jButton4.setBounds(740, 30, 130, 50);

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Home");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        topPanel.add(jButton2);
        jButton2.setBounds(610, 30, 130, 50);

        jButton6.setBackground(new java.awt.Color(0, 0, 0));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("My Borrowings");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        topPanel.add(jButton6);
        jButton6.setBounds(870, 30, 130, 50);

        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GuiTest/Pics/logout.png"))); // NOI18N
        topPanel.add(exitButton);
        exitButton.setBounds(1110, 40, 30, 30);

        notifButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GuiTest/Pics/notification.png"))); // NOI18N
        topPanel.add(notifButton);
        notifButton.setBounds(1040, 40, 30, 30);

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel8.setText("SRMLIB");
        topPanel.add(jLabel8);
        jLabel8.setBounds(80, 10, 146, 50);

        mainPanel.add(topPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 130));

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1190, Short.MAX_VALUE)
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );

        mainPanel.add(detailsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 1190, 570));

        homePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userNameLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        userNameLabel.setText("HI! Renz!");
        homePanel.add(userNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, -1, -1));

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        homePanel.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-170, 100, 1360, 10));
        homePanel.add(featuredBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 1190, 390));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setText("Featured Books");
        homePanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        tabbedPane.addTab("", homePanel);

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1190, Short.MAX_VALUE)
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );

        tabbedPane.addTab("tab2", searchPanel);

        javax.swing.GroupLayout borrowingsPanelLayout = new javax.swing.GroupLayout(borrowingsPanel);
        borrowingsPanel.setLayout(borrowingsPanelLayout);
        borrowingsPanelLayout.setHorizontalGroup(
            borrowingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1190, Short.MAX_VALUE)
        );
        borrowingsPanelLayout.setVerticalGroup(
            borrowingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );

        tabbedPane.addTab("tab4", borrowingsPanel);

        mainPanel.add(tabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1190, 610));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        tabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       tabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked

    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        tabbedPane.setSelectedIndex(2);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,
            "Are you sure you want to exit?", "Library Management System",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
            ObjButtons,ObjButtons[1]);
        if(PromptResult==0)
        {
            System.exit(0);             }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
     
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,
            "Are you sure you want to Logout?", "Library Management System",
            JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null,
            ObjButtons,ObjButtons[1]);
        if(PromptResult==0)
        {
            Login log = new Login();
            log.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel borrowingsPanel;
    private java.awt.Canvas canvas1;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JLabel exitButton;
    private javax.swing.JPanel featuredBooks;
    private javax.swing.JPanel homePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel notifButton;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel userNameLabel;
    // End of variables declaration//GEN-END:variables
}
