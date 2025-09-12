package componentStuff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    public PanelLoginAndRegister() {
        initComponents();
        initRegister();
        initLogin();
        login.setVisible(false);
        register.setVisible(true);

    }

    private void initRegister() {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("Arial Black", 1, 30));
        label.setForeground(new Color(167, 134, 193));
        register.add(label);

        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/imageStuff/user.png")));
        txtUser.setHint("Username");
        register.add(txtUser, "w 60%");

        MyPasswordField txtPasswordField = new MyPasswordField();
        txtPasswordField.setPrefixIcon(new ImageIcon(getClass().getResource("/imageStuff/pass.png")));
        txtPasswordField.setHint("Password");
        register.add(txtPasswordField, "w 60%");

        MyPasswordField txtchkPasswordField = new MyPasswordField();
        txtchkPasswordField.setPrefixIcon(new ImageIcon(getClass().getResource("/imageStuff/pass.png")));
        txtchkPasswordField.setHint("Confirm Password");
        register.add(txtchkPasswordField, "w 60%");

        Button signupButton = new Button();
        signupButton.setBackground(new Color(167, 134, 193));
        signupButton.setForeground(new Color(250, 250, 250));
        signupButton.setText("Sign Up");
        signupButton.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty!");
                return;
            }

            try (Connection conn = DatabaseUtil.getConnection()) {
                // Check if username exists
                String checkSql = "SELECT * FROM users WHERE username=?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username already taken!");
                } else {
                    // Insert new user
                    String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, password); // For real apps, hash the password!
                    int rows = insertStmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Registration successful!");
                        // Optionally switch to login panel here
                    } else {
                        JOptionPane.showMessageDialog(this, "Registration failed!");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });
        register.add(signupButton, "w 40%, h 40");

        txtUser.addActionListener(e -> txtPasswordField.requestFocusInWindow());
        txtPasswordField.addActionListener(e -> txtchkPasswordField.requestFocusInWindow());
        txtchkPasswordField.addActionListener(e -> signupButton.doClick());
    }

    private void initLogin() {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]25[]push"));
        JLabel label = new JLabel("Login");
        label.setFont(new Font("Arial Black", 1, 30));
        label.setForeground(new Color(7, 164, 121));
        login.add(label);

        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/imageStuff/user.png")));
        txtUser.setHint("Username");
        login.add(txtUser, "w 60%");

        MyPasswordField txtPasswordField = new MyPasswordField();
        txtPasswordField.setPrefixIcon(new ImageIcon(getClass().getResource("/imageStuff/pass.png")));
        txtPasswordField.setHint("Password");
        login.add(txtPasswordField, "w 60%");

        Button loginButton = new Button();
        loginButton.setBackground(new Color(41, 39, 76));
        loginButton.setForeground(new Color(250, 250, 250));
        loginButton.setText("Login");
        loginButton.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPasswordField.getPassword());
            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password); // For real apps, use hashed passwords!
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    // Proceed to main form here
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });
        login.add(loginButton, "w 40%, h 40");
        
        txtUser.addActionListener(e -> txtPasswordField.requestFocusInWindow());
        txtPasswordField.addActionListener(e -> loginButton.doClick());
    }

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 301, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 301, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
