package test;


import LibraryTest.SendEmail;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {
    String dbUrl = "jdbc:mysql://localhost:3306/library";
    String dbUser = "root";
    String dbPass = "";
    String userEmail;
    
    public Login() {
        initComponents();
    }
    
    public void login(String email, String pass){
        String query = "SELECT * FROM user WHERE email = ? AND pass = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                userEmail = email;
                new test.User(userEmail).setVisible(true);
                setVisible(false);
            }else{
                JOptionPane.showMessageDialog(mainPanel, "Username or password are incorrect");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void signup(String name, String address, String contact, String email ,String pass){
        try{
            Connection conn = null;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            Statement state = (Statement) conn.createStatement();        
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            int otp = Integer.parseInt(String.format("%06d",number));
            
            if(emailChecker(email)){
                JOptionPane.showMessageDialog(mainPanel, "Name is Already Used");
            }else{
                String insert = "INSERT INTO user (name, address, contact, email ,pass, otp) VALUES('"+name+"', '"+ address +"', '"+ contact +"','"+email+"', '"+pass+"', "+otp+");";
                state.executeUpdate(insert);
                JOptionPane.showMessageDialog(mainPanel, "Registration Successful");
            }
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    public boolean emailChecker(String name){
        String query = "SELECT email FROM user WHERE email = ?";
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
    
    public void sendOtp(String email) {
        String otp;

        // Generate a 6-digit OTP
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        otp = String.format("%06d", number); // e.g. "083245"

        String updateQuery = "UPDATE user SET otp = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, otp); // Store OTP as String
            stmt.setString(2, email);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(mainPanel, "Email not found");
                return;
            }

            // Send email with OTP
            String body = "The OTP for password change is - " + otp + " - . Do not share it with anyone.";
            SendEmail sm = new SendEmail(email, "Change Password", body);
            JOptionPane.showMessageDialog(mainPanel, "OTP sent successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Error occurred while sending OTP");
        }
    }
    
    public boolean otpChecker(String email, int otp){
        String query = "SELECT otp FROM user WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int rsOtp = rs.getInt("otp");
                return rsOtp == otp;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void changePassword(String email, String password) {
        String query = "UPDATE user SET pass = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, password); // set password
            stmt.setString(2, email);    // set email

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(mainPanel, "Password changed successfully");
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Email not found. Password not changed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "Error changing password");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        exitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        tabbedPane = new javax.swing.JTabbedPane();
        loginPanel = new javax.swing.JPanel();
        inputEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        inputPassword = new javax.swing.JTextField();
        loginBtn = new javax.swing.JButton();
        signupBtn = new javax.swing.JLabel();
        forgotPassBtn = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        signPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        emailInput = new javax.swing.JTextField();
        passInput = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        registerBtn = new javax.swing.JButton();
        nameInput = new javax.swing.JTextField();
        numberInput = new javax.swing.JTextField();
        addressInput = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        signBackBtn = new javax.swing.JButton();
        forgotPanel = new javax.swing.JPanel();
        forgotEmailInput = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        otpInput = new javax.swing.JTextField();
        newPassInput = new javax.swing.JTextField();
        conPassInput = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        changeBtn = new javax.swing.JButton();
        sendOtpBtn = new javax.swing.JButton();
        forgotBackBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setMinimumSize(new java.awt.Dimension(1190, 700));
        setPreferredSize(new java.awt.Dimension(1190, 700));

        mainPanel.setMinimumSize(new java.awt.Dimension(1190, 700));
        mainPanel.setPreferredSize(new java.awt.Dimension(1190, 700));
        mainPanel.setLayout(null);

        exitButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        exitButton.setText("X");
        exitButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel1.setText("SRMLIB");

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel2.setText("LIBRARY MANAGEMENT PORTAL");

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1210, Short.MAX_VALUE)
            .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(topPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(topPanelLayout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(470, 470, 470)
                            .addComponent(jLabel1))
                        .addGroup(topPanelLayout.createSequentialGroup()
                            .addGap(480, 480, 480)
                            .addComponent(jLabel2))
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
            .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(topPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(topPanelLayout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(jLabel1)))
                    .addComponent(jLabel2)
                    .addGap(31, 31, 31)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        mainPanel.add(topPanel);
        topPanel.setBounds(0, 0, 1200, 170);

        inputEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputEmailActionPerformed(evt);
            }
        });

        jLabel4.setText("Email");

        jLabel3.setText("Password");

        inputPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputPasswordActionPerformed(evt);
            }
        });

        loginBtn.setText("Login");
        loginBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        signupBtn.setForeground(new java.awt.Color(0, 153, 255));
        signupBtn.setText("Sign-Up");
        signupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signupBtnMouseClicked(evt);
            }
        });

        forgotPassBtn.setForeground(new java.awt.Color(0, 153, 255));
        forgotPassBtn.setText("Forgot Password");
        forgotPassBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                forgotPassBtnMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Log-In");

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap(551, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(loginPanelLayout.createSequentialGroup()
                                .addComponent(forgotPassBtn)
                                .addGap(8, 8, 8)))
                        .addGap(539, 539, 539))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(573, 573, 573))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addComponent(signupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(567, 567, 567))))
            .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(loginPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(43, 43, 43)
                            .addComponent(inputEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(loginPanelLayout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(signupBtn)
                .addGap(18, 18, 18)
                .addComponent(forgotPassBtn)
                .addGap(179, 179, 179))
            .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(loginPanelLayout.createSequentialGroup()
                    .addGap(0, 70, Short.MAX_VALUE)
                    .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(inputEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(30, 30, 30)
                    .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 335, Short.MAX_VALUE)))
        );

        tabbedPane.addTab("tab1", loginPanel);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Sign-Up");

        jLabel6.setText("Email");

        emailInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailInputActionPerformed(evt);
            }
        });

        passInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passInputActionPerformed(evt);
            }
        });

        jLabel7.setText("Password");

        registerBtn.setText("Register");
        registerBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        nameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameInputActionPerformed(evt);
            }
        });

        numberInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberInputActionPerformed(evt);
            }
        });

        addressInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressInputActionPerformed(evt);
            }
        });

        jLabel9.setText("Name");

        jLabel10.setText("Number");

        jLabel11.setText("Address");

        signBackBtn.setText("Back");
        signBackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signBackBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout signPanelLayout = new javax.swing.GroupLayout(signPanel);
        signPanel.setLayout(signPanelLayout);
        signPanelLayout.setHorizontalGroup(
            signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(signBackBtn)
                .addGap(452, 452, 452)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signPanelLayout.createSequentialGroup()
                .addContainerGap(396, Short.MAX_VALUE)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signPanelLayout.createSequentialGroup()
                        .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numberInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(432, 432, 432))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signPanelLayout.createSequentialGroup()
                        .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(524, 524, 524))))
        );
        signPanelLayout.setVerticalGroup(
            signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signPanelLayout.createSequentialGroup()
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(signPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel8))
                    .addGroup(signPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(signBackBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(signPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        tabbedPane.addTab("tab2", signPanel);

        forgotEmailInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotEmailInputActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Forgot Password");

        jLabel13.setText("New Password");

        otpInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otpInputActionPerformed(evt);
            }
        });

        newPassInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPassInputActionPerformed(evt);
            }
        });

        conPassInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conPassInputActionPerformed(evt);
            }
        });

        jLabel14.setText("Email");

        jLabel16.setText("Confirm Password");

        changeBtn.setText("Change Password");
        changeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeBtnActionPerformed(evt);
            }
        });

        sendOtpBtn.setText("Send OTP");
        sendOtpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendOtpBtnActionPerformed(evt);
            }
        });

        forgotBackBtn.setText("Back");
        forgotBackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotBackBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout forgotPanelLayout = new javax.swing.GroupLayout(forgotPanel);
        forgotPanel.setLayout(forgotPanelLayout);
        forgotPanelLayout.setHorizontalGroup(
            forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, forgotPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, forgotPanelLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(530, 530, 530))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, forgotPanelLayout.createSequentialGroup()
                        .addComponent(changeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(511, 511, 511))))
            .addGroup(forgotPanelLayout.createSequentialGroup()
                .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(forgotPanelLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendOtpBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(conPassInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newPassInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(otpInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(forgotEmailInput, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(forgotPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(forgotBackBtn)))
                .addContainerGap(409, Short.MAX_VALUE))
        );
        forgotPanelLayout.setVerticalGroup(
            forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(forgotPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(forgotBackBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(70, 70, 70)
                .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forgotEmailInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newPassInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conPassInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(forgotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(otpInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendOtpBtn))
                .addGap(28, 28, 28)
                .addComponent(changeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        tabbedPane.addTab("tab3", forgotPanel);

        mainPanel.add(tabbedPane);
        tabbedPane.setBounds(0, 110, 1200, 550);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed

        String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,
            "Are you sure you want to exit?", "Library Management System",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
            ObjButtons,ObjButtons[1]);
        if(PromptResult==0)
        {
            System.exit(0);
        }
    }//GEN-LAST:event_exitButtonActionPerformed

    private void inputEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputEmailActionPerformed

    private void inputPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputPasswordActionPerformed

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        login(inputEmail.getText(), inputPassword.getText());
        inputEmail.setText("");
        inputPassword.setText("");
    }//GEN-LAST:event_loginBtnActionPerformed

    private void signupBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signupBtnMouseClicked
        tabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_signupBtnMouseClicked

    private void forgotPassBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPassBtnMouseClicked
        tabbedPane.setSelectedIndex(2);
    }//GEN-LAST:event_forgotPassBtnMouseClicked

    private void emailInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailInputActionPerformed

    private void passInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passInputActionPerformed

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        signup(nameInput.getText(), addressInput.getText(), numberInput.getText(), emailInput.getText(), passInput.getText());
        tabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_registerBtnActionPerformed

    private void nameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameInputActionPerformed

    private void numberInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numberInputActionPerformed

    private void addressInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressInputActionPerformed

    private void forgotEmailInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotEmailInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_forgotEmailInputActionPerformed

    private void otpInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otpInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_otpInputActionPerformed

    private void newPassInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPassInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newPassInputActionPerformed

    private void conPassInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conPassInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_conPassInputActionPerformed

    private void sendOtpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendOtpBtnActionPerformed
        sendOtp(forgotEmailInput.getText());
    }//GEN-LAST:event_sendOtpBtnActionPerformed

    private void changeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeBtnActionPerformed
        try {
            int otpValue = Integer.parseInt(otpInput.getText());
            if(!newPassInput.getText().equals(conPassInput.getText())){
                JOptionPane.showMessageDialog(mainPanel, "New Password and Confirm Password must be the same");
            }else{
                if (otpChecker(forgotEmailInput.getText(), otpValue)) {
                    changePassword(forgotEmailInput.getText(), newPassInput.getText());
                    forgotEmailInput.setText("");
                    newPassInput.setText("");
                    conPassInput.setText("");
                    otpInput.setText("");
                    tabbedPane.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid OTP");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "OTP must be a number");
        }
    }//GEN-LAST:event_changeBtnActionPerformed

    private void forgotBackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotBackBtnActionPerformed
        tabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_forgotBackBtnActionPerformed

    private void signBackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signBackBtnActionPerformed
        tabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_signBackBtnActionPerformed

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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressInput;
    private javax.swing.JButton changeBtn;
    private javax.swing.JTextField conPassInput;
    private javax.swing.JTextField emailInput;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton forgotBackBtn;
    private javax.swing.JTextField forgotEmailInput;
    private javax.swing.JPanel forgotPanel;
    private javax.swing.JLabel forgotPassBtn;
    private javax.swing.JTextField inputEmail;
    private javax.swing.JTextField inputPassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameInput;
    private javax.swing.JTextField newPassInput;
    private javax.swing.JTextField numberInput;
    private javax.swing.JTextField otpInput;
    private javax.swing.JTextField passInput;
    private javax.swing.JButton registerBtn;
    private javax.swing.JButton sendOtpBtn;
    private javax.swing.JButton signBackBtn;
    private javax.swing.JPanel signPanel;
    private javax.swing.JLabel signupBtn;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
