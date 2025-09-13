package DAOstuff.user;

import javax.swing.*;
import java.awt.*;

public class UserDialog extends JDialog {

    private JTextField tfUsername, tfPassword, tfType;
    private boolean saved = false;
    private User user;

    public UserDialog(Window parent, User existing) {
        super(parent, "Edit User", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setSize(350, 220);
        setLocationRelativeTo(parent);

        tfUsername = new JTextField(20);
        tfPassword = new JTextField(20);
        tfType = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        panel.add(tfUsername);
        panel.add(new JLabel("Password:"));
        panel.add(tfPassword);
        panel.add(new JLabel("Type:"));
        panel.add(tfType);
        add(panel, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        if (existing != null) {
            tfUsername.setText(existing.getUsername());
            tfPassword.setText(existing.getPassword());
            tfType.setText(existing.getType());
            this.user = existing;
        }

        saveBtn.addActionListener(e -> {
            try {
                String username = tfUsername.getText();
                String password = tfPassword.getText();
                String type = tfType.getText();
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

    public boolean isSaved() {
        return saved;
    }

    public User getUser() {
        return user;
    }
}
