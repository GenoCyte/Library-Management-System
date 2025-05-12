package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.DefaultTableModel;

public class AdminUI extends javax.swing.JFrame {
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    JPanel showPendingPanel;
    JPanel showBorrowedPanel;

    public AdminUI() {
        initComponents();
        
        pendingPanel.setLayout(new BorderLayout());
        showPendingPanel = showPendingBorrows();
        pendingPanel.add(showPendingPanel, BorderLayout.CENTER);
        
        borrowedPanel.setLayout(new BorderLayout());
        showBorrowedPanel = showBorrowedBooks();
        borrowedPanel.add(showBorrowedPanel, BorderLayout.CENTER);
    }
    
    private JPanel showPendingBorrows() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(1190, 575));

        // Title panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setPreferredSize(new Dimension(1190, 80));
        JLabel l1 = new JLabel("Pending Books");
        l1.setFont(new Font("Arial", Font.PLAIN, 26));
        topPanel.add(l1);

        // Table with model
        DefaultTableModel model = new DefaultTableModel(new Object[]{"User ID", "User Name", "Book ID", "Book Name", "Time", "Date"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Confirm button
        JButton confirmButton = new JButton("Confirm Selected Borrow");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 18));
        confirmButton.setPreferredSize(new Dimension(250, 50));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(confirmButton);

        confirmButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(p, "Please select a row.");
                return;
            }

            int userId = Integer.parseInt(model.getValueAt(row, 0).toString());
            String userName = model.getValueAt(row, 1).toString();
            int bookId = Integer.parseInt(model.getValueAt(row, 2).toString());
            String bookName = model.getValueAt(row, 3).toString();

            LocalDate currentDate = LocalDate.now();
            LocalDate deadline = currentDate.plusWeeks(1);

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
                // Get genre from books
                String genre = "";
                try (PreparedStatement stmtGenre = conn.prepareStatement("SELECT genre FROM books WHERE id = ?")) {
                    stmtGenre.setInt(1, bookId);
                    ResultSet rs = stmtGenre.executeQuery();
                    if (rs.next()) {
                        genre = rs.getString("genre");
                    }
                    rs.close();
                }

                // Insert into borrowed_books
                try (PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO borrowed_books (user_id, user_name, book_id, book_name, genre, date_borrowed, deadline, penalty) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, userName);
                    insertStmt.setInt(3, bookId);
                    insertStmt.setString(4, bookName);
                    insertStmt.setString(5, genre);
                    insertStmt.setDate(6, java.sql.Date.valueOf(currentDate));
                    insertStmt.setDate(7, java.sql.Date.valueOf(deadline));
                    insertStmt.setInt(8, 0); // default penalty to 0

                    insertStmt.executeUpdate();
                }
                
                try (PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE books SET status = 1 WHERE id = ?")) {

                    updateStmt.setInt(1, bookId);

                    updateStmt.executeUpdate();
                }

                // Delete from pending_borrows
                try (PreparedStatement deleteStmt = conn.prepareStatement(
                        "DELETE FROM pending_borrows WHERE user_id = ? AND book_id = ? LIMIT 1")) {
                    deleteStmt.setInt(1, userId);
                    deleteStmt.setInt(2, bookId);
                    deleteStmt.executeUpdate();
                }

                model.removeRow(row);
                JOptionPane.showMessageDialog(p, "Confirmed borrow for " + userName + " - " + bookName);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(p, "Error updating database: " + ex.getMessage());
            }
        });

        // Load data into the table
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement("SELECT user_id, user_name, book_id, book_name, time, date FROM pending_borrows");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getInt("book_id"),
                    rs.getString("book_name"),
                    rs.getTime("time").toString(),
                    rs.getDate("date").toString()
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(p, "Database error: " + ex.getMessage());
        }

        // Final panel assembly
        p.add(topPanel, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);

        return p;
    }
    
    private JPanel showBorrowedBooks() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(1190, 575));

        // Title panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setPreferredSize(new Dimension(1190, 80));
        JLabel l1 = new JLabel("Borrowed Books");
        l1.setFont(new Font("Arial", Font.PLAIN, 26));
        topPanel.add(l1);

        // Table with model
        DefaultTableModel model = new DefaultTableModel(new Object[]{"User ID", "User Name", "Book ID", "Book Name", "Date", "Deadline", "Penalty"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Confirm button
        JButton confirmButton = new JButton("Confirm Returned Books");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 18));
        confirmButton.setPreferredSize(new Dimension(250, 50));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(confirmButton);

        confirmButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(p, "Please select a row.");
                return;
            }

            int userId = Integer.parseInt(model.getValueAt(row, 0).toString());
            String userName = model.getValueAt(row, 1).toString();
            int bookId = Integer.parseInt(model.getValueAt(row, 2).toString());
            String bookName = model.getValueAt(row, 3).toString();

            LocalDate currentDate = LocalDate.now();
            LocalDate deadline = currentDate.plusWeeks(1);

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
                // Get genre from books
                String genre = "";
                try (PreparedStatement stmtGenre = conn.prepareStatement("SELECT genre FROM books WHERE id = ?")) {
                    stmtGenre.setInt(1, bookId);
                    ResultSet rs = stmtGenre.executeQuery();
                    if (rs.next()) {
                        genre = rs.getString("genre");
                    }
                    rs.close();
                }
                
                try (PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE books SET status = 0 WHERE id = ?")) {

                    updateStmt.setInt(1, bookId);

                    updateStmt.executeUpdate();
                }

                // Delete from pending_borrows
                try (PreparedStatement deleteStmt = conn.prepareStatement(
                        "DELETE FROM borrowed_books WHERE user_id = ? AND book_id = ? LIMIT 1")) {
                    deleteStmt.setInt(1, userId);
                    deleteStmt.setInt(2, bookId);
                    deleteStmt.executeUpdate();
                }

                model.removeRow(row);
                JOptionPane.showMessageDialog(p, "Successfull Return of Book for " + userName + " - " + bookName);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(p, "Error updating database: " + ex.getMessage());
            }
        });

        // Load data into the table
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement("SELECT user_id, user_name, book_id, book_name, date_borrowed, deadline, penalty FROM borrowed_books");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getInt("book_id"),
                    rs.getString("book_name"),
                    rs.getDate("date_borrowed").toString(),
                    rs.getDate("deadline").toString(),
                    rs.getInt("penalty")
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(p, "Database error: " + ex.getMessage());
        }

        // Final panel assembly
        p.add(topPanel, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);

        return p;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        navBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        pastdueBtn = new javax.swing.JButton();
        addUserBtn = new javax.swing.JButton();
        pendingBtn = new javax.swing.JButton();
        borrowedBtn = new javax.swing.JButton();
        dashboardBtn = new javax.swing.JButton();
        addBooksBtn = new javax.swing.JButton();
        exitButton = new javax.swing.JLabel();
        tabbedPane = new javax.swing.JTabbedPane();
        dashbboardPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pendingPanel = new javax.swing.JPanel();
        borrowedPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pastduePanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        addBooksPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        addUserPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        navBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel1.setText("SRMLIB");
        navBar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel2.setText("LIBRARY MANAGEMENT PORTAL");
        navBar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        jSeparator8.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        navBar.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1190, 10));

        pastdueBtn.setBackground(new java.awt.Color(0, 0, 0));
        pastdueBtn.setForeground(new java.awt.Color(255, 255, 255));
        pastdueBtn.setText("Past Due");
        pastdueBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pastdueBtnActionPerformed(evt);
            }
        });
        navBar.add(pastdueBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 30, 130, 50));

        addUserBtn.setBackground(new java.awt.Color(0, 0, 0));
        addUserBtn.setForeground(new java.awt.Color(255, 255, 255));
        addUserBtn.setText("Add User");
        addUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserBtnActionPerformed(evt);
            }
        });
        navBar.add(addUserBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 30, 130, 50));

        pendingBtn.setBackground(new java.awt.Color(0, 0, 0));
        pendingBtn.setForeground(new java.awt.Color(255, 255, 255));
        pendingBtn.setText("Pending Borrows");
        pendingBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendingBtnActionPerformed(evt);
            }
        });
        navBar.add(pendingBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 130, 50));

        borrowedBtn.setBackground(new java.awt.Color(0, 0, 0));
        borrowedBtn.setForeground(new java.awt.Color(255, 255, 255));
        borrowedBtn.setText("Borrowed Books");
        borrowedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowedBtnActionPerformed(evt);
            }
        });
        navBar.add(borrowedBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, 130, 50));

        dashboardBtn.setBackground(new java.awt.Color(0, 0, 0));
        dashboardBtn.setForeground(new java.awt.Color(255, 255, 255));
        dashboardBtn.setText("Dashboard");
        dashboardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardBtnActionPerformed(evt);
            }
        });
        navBar.add(dashboardBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 130, 50));

        addBooksBtn.setBackground(new java.awt.Color(0, 0, 0));
        addBooksBtn.setForeground(new java.awt.Color(255, 255, 255));
        addBooksBtn.setText("Add Books");
        addBooksBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBooksBtnActionPerformed(evt);
            }
        });
        navBar.add(addBooksBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 30, 130, 50));

        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GuiTest/Pics/logout.png"))); // NOI18N
        navBar.add(exitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 40, -1, -1));

        mainPanel.add(navBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 140));

        jLabel8.setText("dashboard'");

        javax.swing.GroupLayout dashbboardPanelLayout = new javax.swing.GroupLayout(dashbboardPanel);
        dashbboardPanel.setLayout(dashbboardPanelLayout);
        dashbboardPanelLayout.setHorizontalGroup(
            dashbboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashbboardPanelLayout.createSequentialGroup()
                .addGap(429, 429, 429)
                .addComponent(jLabel8)
                .addContainerGap(702, Short.MAX_VALUE))
        );
        dashbboardPanelLayout.setVerticalGroup(
            dashbboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashbboardPanelLayout.createSequentialGroup()
                .addGap(224, 224, 224)
                .addComponent(jLabel8)
                .addContainerGap(325, Short.MAX_VALUE))
        );

        tabbedPane.addTab("tab1", dashbboardPanel);

        javax.swing.GroupLayout pendingPanelLayout = new javax.swing.GroupLayout(pendingPanel);
        pendingPanel.setLayout(pendingPanelLayout);
        pendingPanelLayout.setHorizontalGroup(
            pendingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1190, Short.MAX_VALUE)
        );
        pendingPanelLayout.setVerticalGroup(
            pendingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 565, Short.MAX_VALUE)
        );

        tabbedPane.addTab("tab2", pendingPanel);

        jLabel6.setText("borrowed books");

        javax.swing.GroupLayout borrowedPanelLayout = new javax.swing.GroupLayout(borrowedPanel);
        borrowedPanel.setLayout(borrowedPanelLayout);
        borrowedPanelLayout.setHorizontalGroup(
            borrowedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowedPanelLayout.createSequentialGroup()
                .addGap(493, 493, 493)
                .addComponent(jLabel6)
                .addContainerGap(611, Short.MAX_VALUE))
        );
        borrowedPanelLayout.setVerticalGroup(
            borrowedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borrowedPanelLayout.createSequentialGroup()
                .addGap(262, 262, 262)
                .addComponent(jLabel6)
                .addContainerGap(287, Short.MAX_VALUE))
        );

        tabbedPane.addTab("tab3", borrowedPanel);

        jLabel5.setText("past due");

        javax.swing.GroupLayout pastduePanelLayout = new javax.swing.GroupLayout(pastduePanel);
        pastduePanel.setLayout(pastduePanelLayout);
        pastduePanelLayout.setHorizontalGroup(
            pastduePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pastduePanelLayout.createSequentialGroup()
                .addContainerGap(677, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(468, 468, 468))
        );
        pastduePanelLayout.setVerticalGroup(
            pastduePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pastduePanelLayout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jLabel5)
                .addContainerGap(354, Short.MAX_VALUE))
        );

        tabbedPane.addTab("tab4", pastduePanel);

        jLabel4.setText("add books");

        javax.swing.GroupLayout addBooksPanelLayout = new javax.swing.GroupLayout(addBooksPanel);
        addBooksPanel.setLayout(addBooksPanelLayout);
        addBooksPanelLayout.setHorizontalGroup(
            addBooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addBooksPanelLayout.createSequentialGroup()
                .addGap(477, 477, 477)
                .addComponent(jLabel4)
                .addContainerGap(658, Short.MAX_VALUE))
        );
        addBooksPanelLayout.setVerticalGroup(
            addBooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addBooksPanelLayout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(jLabel4)
                .addContainerGap(327, Short.MAX_VALUE))
        );

        tabbedPane.addTab("tab5", addBooksPanel);

        jLabel3.setText("add user");

        javax.swing.GroupLayout addUserPanelLayout = new javax.swing.GroupLayout(addUserPanel);
        addUserPanel.setLayout(addUserPanelLayout);
        addUserPanelLayout.setHorizontalGroup(
            addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addUserPanelLayout.createSequentialGroup()
                .addContainerGap(692, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(453, 453, 453))
        );
        addUserPanelLayout.setVerticalGroup(
            addUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addUserPanelLayout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(jLabel3)
                .addContainerGap(372, Short.MAX_VALUE))
        );

        tabbedPane.addTab("tab6", addUserPanel);

        mainPanel.add(tabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1190, 600));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 700));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pastdueBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pastdueBtnActionPerformed
        tabbedPane.setSelectedIndex(3);
    }//GEN-LAST:event_pastdueBtnActionPerformed

    private void addUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserBtnActionPerformed
        tabbedPane.setSelectedIndex(5);
    }//GEN-LAST:event_addUserBtnActionPerformed

    private void pendingBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendingBtnActionPerformed
        tabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_pendingBtnActionPerformed

    private void borrowedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowedBtnActionPerformed
        tabbedPane.setSelectedIndex(2);
    }//GEN-LAST:event_borrowedBtnActionPerformed

    private void dashboardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardBtnActionPerformed
        tabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_dashboardBtnActionPerformed

    private void addBooksBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBooksBtnActionPerformed
        tabbedPane.setSelectedIndex(4);
    }//GEN-LAST:event_addBooksBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBooksBtn;
    private javax.swing.JPanel addBooksPanel;
    private javax.swing.JButton addUserBtn;
    private javax.swing.JPanel addUserPanel;
    private javax.swing.JButton borrowedBtn;
    private javax.swing.JPanel borrowedPanel;
    private javax.swing.JPanel dashbboardPanel;
    private javax.swing.JButton dashboardBtn;
    private javax.swing.JLabel exitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel navBar;
    private javax.swing.JButton pastdueBtn;
    private javax.swing.JPanel pastduePanel;
    private javax.swing.JButton pendingBtn;
    private javax.swing.JPanel pendingPanel;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
