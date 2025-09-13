package MenuPanels;

import componentStuff.ModernTable;
import componentStuff.ModernScrollPane;
import componentStuff.ModernPanel;
import componentStuff.ModernDialog;
import componentStuff.Button;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import DAOstuff.user.*;

public class UserMgmtPanel extends JPanel {
    private ModernTable userTable;
    private DefaultTableModel tableModel;

    public UserMgmtPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(248, 249, 250));

        String[] columnNames = {"ID", "Username", "Password", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new ModernTable(tableModel);
        // Increase table size for better visibility
        userTable.setPreferredScrollableViewportSize(new Dimension(600, 350));
        userTable.setRowHeight(35); // Increase row height for better readability
        ModernScrollPane scrollPane = new ModernScrollPane(userTable);
        JPanel tablePanel = ModernPanel.createCenteredPanel(scrollPane);
        add(tablePanel, BorderLayout.CENTER);

        ModernPanel adminPanel = new ModernPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        Button addBtn = new Button();
        addBtn.setText("Add User");
        addBtn.setBackground(new Color(40, 167, 69));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.setPreferredSize(new Dimension(120, 35));
        
        Button editBtn = new Button();
        editBtn.setText("Edit User");
        editBtn.setBackground(new Color(255, 193, 7));
        editBtn.setForeground(Color.BLACK);
        editBtn.setFont(new Font("Arial", Font.BOLD, 14));
        editBtn.setPreferredSize(new Dimension(120, 35));
        
        Button delBtn = new Button();
        delBtn.setText("Remove User");
        delBtn.setBackground(new Color(220, 53, 69));
        delBtn.setForeground(Color.WHITE);
        delBtn.setFont(new Font("Arial", Font.BOLD, 14));
        delBtn.setPreferredSize(new Dimension(140, 35));
        
        adminPanel.add(addBtn);
        adminPanel.add(editBtn);
        adminPanel.add(delBtn);
        add(adminPanel, BorderLayout.NORTH);

        addBtn.addActionListener(e -> {
            UserDialog dialog = new UserDialog(SwingUtilities.getWindowAncestor(this), null, false);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                boolean success = UserDAO.addUser(dialog.getUser());
                if (success) refreshUserList();
            }
        });

        editBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row == -1) {
                ModernDialog.showMessageDialog(this, "Select a user to edit.", "Edit User", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            User u = getUserFromRow(row);
            UserDialog dialog = new UserDialog(SwingUtilities.getWindowAncestor(this), u, true);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                boolean success = UserDAO.updateUser(dialog.getUser());
                if (success) refreshUserList();
            }
        });

        delBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row == -1) {
                ModernDialog.showMessageDialog(this, "Select a user to remove.", "Remove User", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            User u = getUserFromRow(row);
            if (u.getUsername().equals("admin")) {
                ModernDialog.showMessageDialog(this, "Cannot remove the main admin account.", "Remove User", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = ModernDialog.showConfirmDialog(this, "Are you sure you want to remove this user?", "Remove User");
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = UserDAO.deleteUser(u.getUserId());
                if (success) refreshUserList();
            }
        });

        refreshUserList();
    }

    private User getUserFromRow(int row) {
        return new User(
            (int) tableModel.getValueAt(row, 0),
            (String) tableModel.getValueAt(row, 1),
            (String) tableModel.getValueAt(row, 2),
            (String) tableModel.getValueAt(row, 3)
        );
    }

    public void refreshUserList() {
        tableModel.setRowCount(0);
        List<User> users = UserDAO.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getType()
            });
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
