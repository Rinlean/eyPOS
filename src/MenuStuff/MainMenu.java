package MenuStuff;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private JPanel adminPanel;
    private JMenuItem manageUsersMenuItem;

    public MainMenu(String username, String role) {
        setTitle("Main Menu - " + role);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        manageUsersMenuItem = new JMenuItem("Manage Users");
        fileMenu.add(manageUsersMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (" + role + ")");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        adminPanel = new JPanel();
        adminPanel.add(new JLabel("Admin Controls Here"));
        mainPanel.add(adminPanel, BorderLayout.CENTER);

        add(mainPanel);

        setupRoleBasedUI(role);
    }

    private void setupRoleBasedUI(String role) {
        if ("user".equalsIgnoreCase(role)) {
            adminPanel.setVisible(false);
            manageUsersMenuItem.setEnabled(false);
        }
        // If admin, leave everything visible & enabled
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
