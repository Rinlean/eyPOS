package DAOstuff.user;

import componentStuff.ModernPanel;
import componentStuff.MyTextField;
import componentStuff.Button;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserDialog extends JDialog {
    private MyTextField tfUsername, tfPassword;
    private JComboBox<String> cbType;
    private boolean saved = false;
    private User user;

    // Modern color scheme
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color HEADER_COLOR = new Color(52, 73, 94);
    private static final Color HEADER_TEXT_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    public UserDialog(Window parent, User existing, boolean isEdit) {
        super(parent, isEdit ? "Edit User" : "Add User", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setBackground(BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header panel
        ModernPanel headerPanel = new ModernPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel titleLabel = new JLabel(isEdit ? "Edit User Account" : "Add New User");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(HEADER_TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Content panel with modern styling
        ModernPanel contentPanel = new ModernPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(25, 25, 20, 25));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize modern text fields
        tfUsername = new MyTextField();
        tfUsername.setPreferredSize(new Dimension(250, 35));
        tfPassword = new MyTextField();
        tfPassword.setPreferredSize(new Dimension(250, 35));
        
        // Modern combo box styling
        cbType = new JComboBox<>(new String[] {"admin", "user"});
        cbType.setPreferredSize(new Dimension(250, 35));
        cbType.setFont(new Font("Arial", Font.PLAIN, 12));
        cbType.setBackground(new Color(243, 224, 255));

        // Add components with improved layout
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        contentPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(tfUsername, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        contentPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(tfPassword, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel typeLabel = new JLabel("User Type:");
        typeLabel.setFont(LABEL_FONT);
        contentPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(cbType, gbc);
        
        add(contentPanel, BorderLayout.CENTER);

        // Modern button panel
        ModernPanel btnPanel = new ModernPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setBackground(BACKGROUND_COLOR);
        
        Button saveBtn = new Button();
        saveBtn.setText("Save User");
        saveBtn.setBackground(new Color(40, 167, 69));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 14));
        saveBtn.setPreferredSize(new Dimension(120, 40));
        
        Button cancelBtn = new Button();
        cancelBtn.setText("Cancel");
        cancelBtn.setBackground(new Color(108, 117, 125));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Populate fields if editing existing user
        if (existing != null) {
            tfUsername.setText(existing.getUsername());
            tfPassword.setText(existing.getPassword());
            cbType.setSelectedItem(existing.getType());
            this.user = existing;
            if (isEdit && existing.getUsername().equals("admin")) {
                // Prevent changing username/type of master admin
                tfUsername.setEditable(false);
                cbType.setEnabled(false);
                
                // Add warning label for admin account
                row++;
                gbc.gridx = 0; gbc.gridy = row;
                gbc.gridwidth = 2;
                gbc.insets = new Insets(15, 0, 0, 0);
                JLabel warningLabel = new JLabel("⚠ Master admin username and type cannot be changed");
                warningLabel.setFont(new Font("Arial", Font.ITALIC, 11));
                warningLabel.setForeground(new Color(255, 193, 7));
                contentPanel.add(warningLabel, gbc);
            }
        }

        // Action listeners with modern error handling
        saveBtn.addActionListener(e -> {
            try {
                String username = tfUsername.getText().trim();
                String password = tfPassword.getText().trim();
                String type = (String) cbType.getSelectedItem();
                
                if (username.isEmpty() || password.isEmpty() || type.isEmpty()) {
                    showErrorDialog("All fields are required.");
                    return;
                }
                
                if (username.length() < 3) {
                    showErrorDialog("Username must be at least 3 characters long.");
                    return;
                }
                
                if (password.length() < 4) {
                    showErrorDialog("Password must be at least 4 characters long.");
                    return;
                }
                
                if (user == null) {
                    user = new User(0, username, password, type);
                } else {
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setType(type);
                }
                saved = true;
                dispose();
            } catch (Exception ex) {
                showErrorDialog("Error saving user: " + ex.getMessage());
            }
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        // Allow Enter key to save
        getRootPane().setDefaultButton(saveBtn);
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() { return saved; }
    public User getUser() { return user; }
}