package MenuPanels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import DAOstuff.user.*;

public class UserMgmtPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserMgmtPanel() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Username", "Password", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton editBtn = new JButton("Edit User");
        adminPanel.add(editBtn);
        add(adminPanel, BorderLayout.NORTH);

        editBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a user to edit.");
                return;
            }
            User u = getUserFromRow(row);
            UserDialog dialog = new UserDialog(SwingUtilities.getWindowAncestor(this), u);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                boolean success = UserDAO.updateUser(dialog.getUser());
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
