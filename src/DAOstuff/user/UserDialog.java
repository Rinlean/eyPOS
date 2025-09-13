package DAOstuff.user;

import javax.swing.*;
import java.awt.*;

public class UserDialog extends JDialog {
    private JTextField tfUsername, tfPassword;
    private JComboBox<String> cbType;
    private boolean saved = false;
    private User user;

    public UserDialog(Window parent, User existing, boolean isEdit) {
        super(parent, isEdit ? "Edit User" : "Add User", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setSize(350, 220);
        setLocationRelativeTo(parent);

        tfUsername = new JTextField(20);
        tfPassword = new JTextField(20);
        cbType = new JComboBox<>(new String[] {"admin", "user"});

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Username:")); panel.add(tfUsername);
        panel.add(new JLabel("Password:")); panel.add(tfPassword);
        panel.add(new JLabel("Type:")); panel.add(cbType);
        add(panel, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn); btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        if (existing != null) {
            tfUsername.setText(existing.getUsername());
            tfPassword.setText(existing.getPassword());
            cbType.setSelectedItem(existing.getType());
            this.user = existing;
            if (isEdit && existing.getUsername().equals("admin")) {
                // Prevent changing username/type of master admin
                tfUsername.setEditable(false);
                cbType.setEnabled(false);
            }
        }

        saveBtn.addActionListener(e -> {
            try {
                String username = tfUsername.getText().trim();
                String password = tfPassword.getText();
                String type = (String) cbType.getSelectedItem();
                if (username.isEmpty() || password.isEmpty() || type.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required.");
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
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
        cancelBtn.addActionListener(e -> dispose());
    }

    public boolean isSaved() { return saved; }
    public User getUser() { return user; }
}