package Library;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Ampong
 */
public class Main extends javax.swing.JFrame {
    
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    FileUpload fu = new FileUpload();
    
    public Main() {
        initComponents();
        loadBookImage();
        
        featuredBooks = fu.showBookList();
        
        homeBtn.setBackground(Color.gray);
        homeBtn.setForeground(Color.white);
        
        homeBtn.setFocusPainted(false);
        searchBtn.setFocusPainted(false);
        borrowingsBtn.setFocusPainted(false);
    }
    
    private void loadBookImage() {
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            String query = "SELECT image FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, 4);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob("image");
                InputStream inputStream = blob.getBinaryStream();
                byte[] imageBytes = inputStream.readAllBytes();

                ImageIcon icon = new ImageIcon(imageBytes);
                Image img = icon.getImage().getScaledInstance(190, 280, Image.SCALE_SMOOTH);
                book1.setIcon(new ImageIcon(img));
                book1.setText(""); // remove placeholder text
            } else {
                book1.setText("No book found.");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            book1.setText("Error loading image.");
        }
    }
    
    private void FeaturedBooks(){
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
                        
                    }
                });

                JLabel nameLabel = new JLabel(bookName);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

                bookPanel.add(imageLabel);
                bookPanel.add(nameLabel);

                featuredBooks.add(bookPanel);
            }
            featuredBooks.revalidate();
            featuredBooks.repaint();

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void scaleLabelIcon() {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("GuiTest/Pics/106073l.jpg"));
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(book1.getWidth(), book1.getHeight(), Image.SCALE_SMOOTH);

        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        book1.setIcon(scaledIcon);
    }
    
    private void resetButtonColors() {
        Color defaultBg = Color.black;
        Color defaultFg = Color.white;

        homeBtn.setBackground(defaultBg);
        homeBtn.setForeground(defaultFg);

        searchBtn.setBackground(defaultBg);
        searchBtn.setForeground(defaultFg);

        borrowingsBtn.setBackground(defaultBg);
        borrowingsBtn.setForeground(defaultFg);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        homeBtn = new javax.swing.JButton();
        searchBtn = new javax.swing.JButton();
        borrowingsBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        homePanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        book5 = new javax.swing.JLabel();
        book1 = new javax.swing.JLabel();
        book2 = new javax.swing.JLabel();
        book3 = new javax.swing.JLabel();
        book4 = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        featuredBooks = new javax.swing.JPanel();
        borrowingsPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 1000, 5));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("SRMLIB");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(60, 20, 180, 50);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("LIBRARY MANAGEMENT PORTAL");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(56, 70, 184, 16);

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Sign Out");
        jButton1.setBorder(null);
        jButton1.setMargin(new java.awt.Insets(2, 0, 3, 0));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(1120, 40, 160, 40);

        homeBtn.setBackground(new java.awt.Color(0, 0, 0));
        homeBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        homeBtn.setForeground(new java.awt.Color(255, 255, 255));
        homeBtn.setText("Home");
        homeBtn.setBorder(null);
        homeBtn.setMargin(new java.awt.Insets(2, 0, 3, 0));
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });
        jPanel1.add(homeBtn);
        homeBtn.setBounds(640, 40, 160, 40);

        searchBtn.setBackground(new java.awt.Color(0, 0, 0));
        searchBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchBtn.setForeground(new java.awt.Color(255, 255, 255));
        searchBtn.setText("Book Search");
        searchBtn.setBorder(null);
        searchBtn.setMargin(new java.awt.Insets(2, 0, 3, 0));
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });
        jPanel1.add(searchBtn);
        searchBtn.setBounds(800, 40, 160, 40);

        borrowingsBtn.setBackground(new java.awt.Color(0, 0, 0));
        borrowingsBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        borrowingsBtn.setForeground(new java.awt.Color(255, 255, 255));
        borrowingsBtn.setText("My Borrowings");
        borrowingsBtn.setBorder(null);
        borrowingsBtn.setMargin(new java.awt.Insets(2, 0, 3, 0));
        borrowingsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowingsBtnActionPerformed(evt);
            }
        });
        jPanel1.add(borrowingsBtn);
        borrowingsBtn.setBounds(960, 40, 160, 40);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setMinimumSize(new java.awt.Dimension(1300, 5));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1300, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(30, 110, 1300, 5);

        jPanel7.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1420, 130));

        homePanel.setBackground(new java.awt.Color(255, 255, 255));
        homePanel.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Hi! User");
        homePanel.add(jLabel6);
        jLabel6.setBounds(80, 60, 140, 30);

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.setMinimumSize(new java.awt.Dimension(1300, 5));
        jPanel4.setPreferredSize(new java.awt.Dimension(1300, 5));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1300, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        homePanel.add(jPanel4);
        jPanel4.setBounds(30, 150, 1300, 5);

        book5.setForeground(new java.awt.Color(0, 0, 0));
        book5.setText("Book1");
        homePanel.add(book5);
        book5.setBounds(1040, 240, 190, 280);

        book1.setBackground(new java.awt.Color(102, 102, 102));
        book1.setForeground(new java.awt.Color(0, 0, 0));
        book1.setText("Book1");
        homePanel.add(book1);
        book1.setBounds(120, 240, 190, 280);

        book2.setForeground(new java.awt.Color(0, 0, 0));
        book2.setText("Book1");
        homePanel.add(book2);
        book2.setBounds(350, 240, 190, 280);

        book3.setForeground(new java.awt.Color(0, 0, 0));
        book3.setText("Book1");
        homePanel.add(book3);
        book3.setBounds(580, 240, 190, 280);

        book4.setForeground(new java.awt.Color(0, 0, 0));
        book4.setText("Book1");
        homePanel.add(book4);
        book4.setBounds(810, 240, 190, 280);

        tabbedPanel.addTab("tab1", homePanel);

        searchPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchPanel.setLayout(null);

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Book Search");
        searchPanel.add(jLabel5);
        jLabel5.setBounds(424, 72, 65, 16);

        javax.swing.GroupLayout featuredBooksLayout = new javax.swing.GroupLayout(featuredBooks);
        featuredBooks.setLayout(featuredBooksLayout);
        featuredBooksLayout.setHorizontalGroup(
            featuredBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1420, Short.MAX_VALUE)
        );
        featuredBooksLayout.setVerticalGroup(
            featuredBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 460, Short.MAX_VALUE)
        );

        searchPanel.add(featuredBooks);
        featuredBooks.setBounds(0, 150, 1420, 460);

        tabbedPanel.addTab("tab2", searchPanel);

        borrowingsPanel.setBackground(new java.awt.Color(255, 255, 255));
        borrowingsPanel.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("My Borrowings");

        javax.swing.GroupLayout borrowingsPanelLayout = new javax.swing.GroupLayout(borrowingsPanel);
        borrowingsPanel.setLayout(borrowingsPanelLayout);
        borrowingsPanelLayout.setHorizontalGroup(
            borrowingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowingsPanelLayout.createSequentialGroup()
                .addGap(379, 379, 379)
                .addComponent(jLabel4)
                .addContainerGap(961, Short.MAX_VALUE))
        );
        borrowingsPanelLayout.setVerticalGroup(
            borrowingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowingsPanelLayout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jLabel4)
                .addContainerGap(448, Short.MAX_VALUE))
        );

        tabbedPanel.addTab("tab3", borrowingsPanel);

        jPanel7.add(tabbedPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1420, 650));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        tabbedPanel.setSelectedIndex(0);
        resetButtonColors();
        homeBtn.setBackground(Color.gray);
        homeBtn.setForeground(Color.white);
    }//GEN-LAST:event_homeBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        tabbedPanel.setSelectedIndex(1);
        resetButtonColors();
        searchBtn.setBackground(Color.gray);
        searchBtn.setForeground(Color.white);
    }//GEN-LAST:event_searchBtnActionPerformed

    private void borrowingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowingsBtnActionPerformed
        tabbedPanel.setSelectedIndex(2);
        resetButtonColors();
        borrowingsBtn.setBackground(Color.gray);
        borrowingsBtn.setForeground(Color.white);
    }//GEN-LAST:event_borrowingsBtnActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel book1;
    private javax.swing.JLabel book2;
    private javax.swing.JLabel book3;
    private javax.swing.JLabel book4;
    private javax.swing.JLabel book5;
    private javax.swing.JButton borrowingsBtn;
    private javax.swing.JPanel borrowingsPanel;
    private javax.swing.JPanel featuredBooks;
    private javax.swing.JButton homeBtn;
    private javax.swing.JPanel homePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton searchBtn;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTabbedPane tabbedPanel;
    // End of variables declaration//GEN-END:variables
}
