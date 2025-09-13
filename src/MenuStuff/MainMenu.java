package MenuStuff;

import componentStuff.Button;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JFrame {

    private JPanel sidebar;
    private JPanel mainContent;
    private CardLayout cardLayout;
    private boolean sidebarCollapsed = false;
    private boolean isAdmin = false;
    private final int sidebarMaxWidth = 200;
    private final int sidebarMinWidth = 60;
    private int sidebarCurrentWidth = sidebarMaxWidth;
    private Timer sidebarTimer;
    private boolean isAnimating = false;
    private boolean lastCollapsed = false;

    private JPanel containerPanel;
    private MigLayout migLayout;

    private Button btnDashboard = new Button();
    private Button btnSales = new Button();
    private Button btnProducts = new Button();
    private Button btnSettings = new Button();
    private Button btnUserMgmt = new Button();

    private static final String CARD_DASHBOARD = "Dashboard";
    private static final String CARD_SALES = "Sales";
    private static final String CARD_PRODUCTS = "Products";
    private static final String CARD_SETTINGS = "Settings";
    private static final String CARD_USERMGMT = "UserMgmt";

    public MainMenu(String username, String role) {
        this.isAdmin = "admin".equalsIgnoreCase(role);

        setTitle("eyPOS Main Menu - " + role);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        migLayout = new MigLayout(
                "insets 0, gap 0",
                "[left][grow,fill]",
                "[grow,fill]"
        );
        containerPanel = new JPanel(migLayout);

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(60, 63, 65));
        sidebar.setPreferredSize(new Dimension(sidebarMaxWidth, getHeight()));

        setupSidebarButton(btnDashboard, "Dashboard", "/imagestuff/mail.png");
        setupSidebarButton(btnSales, "Sales", "/imagestuff/mail.png");
        setupSidebarButton(btnProducts, "Products", "/imagestuff/mail.png");
        setupSidebarButton(btnSettings, "Settings", "/imagestuff/mail.png");
        setupSidebarButton(btnUserMgmt, "User Management", "/imagestuff/mail.png");

        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnSales);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnProducts);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnSettings);
        sidebar.add(Box.createVerticalStrut(5));
        if (isAdmin) {
            sidebar.add(btnUserMgmt);
        }

        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);

        mainContent.add(createMenuPanel("Dashboard"), CARD_DASHBOARD);
        mainContent.add(createMenuPanel("Sales"), CARD_SALES);
        mainContent.add(createMenuPanel("Products"), CARD_PRODUCTS);
        mainContent.add(createMenuPanel("Settings"), CARD_SETTINGS);
        if (isAdmin) {
            mainContent.add(createMenuPanel("User Management"), CARD_USERMGMT);
        }

        containerPanel.add(sidebar, "w " + sidebarCurrentWidth + "!, h 100%, dock west");
        containerPanel.add(mainContent, "grow, push");

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        Button toggleButton = new Button();
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false); // Optional: transparent background
        toggleButton.setPreferredSize(new Dimension(40, 40));
        toggleButton.setBackground(new Color(41, 39, 76));
        toggleButton.setForeground(new Color(250, 250, 250));
// Set the icon image
        toggleButton.setIcon(new ImageIcon(getClass().getResource("/imagestuff/mail.png")));
        toggleButton.addActionListener(e -> toggleSidebar());
        leftPanel.add(toggleButton);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (" + role + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        btnDashboard.addActionListener(e -> cardLayout.show(mainContent, CARD_DASHBOARD));
        btnSales.addActionListener(e -> cardLayout.show(mainContent, CARD_SALES));
        btnProducts.addActionListener(e -> cardLayout.show(mainContent, CARD_PRODUCTS));
        btnSettings.addActionListener(e -> cardLayout.show(mainContent, CARD_SETTINGS));
        if (isAdmin) {
            btnUserMgmt.addActionListener(e -> cardLayout.show(mainContent, CARD_USERMGMT));
        }

        cardLayout.show(mainContent, CARD_DASHBOARD);

        updateSidebarButtonsForCollapse(sidebarCollapsed);

        setVisible(true);
    }

    private void setupSidebarButton(Button btn, String text, String iconPath) {
        btn.setBackground(new Color(41, 39, 76)); // A dark purple color
        btn.setForeground(new Color(250, 250, 250));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setText(text);
        btn.setIconTextGap(15);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Remove focus/press highlight and border
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);

        // Set icon from the provided path
        try {
            btn.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
            btn.setText(text.substring(0, 1));
        }
    }

    private final Border iconPadding = new EmptyBorder(0, 16, 0, 0); // 16px left padding
    private final Border noPadding = new EmptyBorder(0, 0, 0, 0);

    private void updateSidebarButtonsForCollapse(boolean collapsed) {
        if (collapsed) {
            btnDashboard.setText(null);
            btnDashboard.setHorizontalAlignment(SwingConstants.CENTER);
            btnDashboard.setBorder(noPadding);
            btnSales.setText(null);
            btnSales.setHorizontalAlignment(SwingConstants.CENTER);
            btnSales.setBorder(noPadding);
            btnProducts.setText(null);
            btnProducts.setHorizontalAlignment(SwingConstants.CENTER);
            btnProducts.setBorder(noPadding);
            btnSettings.setText(null);
            btnSettings.setHorizontalAlignment(SwingConstants.CENTER);
            btnSettings.setBorder(noPadding);
            if (isAdmin) {
                btnUserMgmt.setText(null);
                btnUserMgmt.setHorizontalAlignment(SwingConstants.CENTER);
                btnUserMgmt.setBorder(noPadding);
            }
        } else {
            btnDashboard.setText("Dashboard");
            btnDashboard.setHorizontalAlignment(SwingConstants.LEFT);
            btnDashboard.setBorder(iconPadding);
            btnSales.setText("Sales");
            btnSales.setHorizontalAlignment(SwingConstants.LEFT);
            btnSales.setBorder(iconPadding);
            btnProducts.setText("Products");
            btnProducts.setHorizontalAlignment(SwingConstants.LEFT);
            btnProducts.setBorder(iconPadding);
            btnSettings.setText("Settings");
            btnSettings.setHorizontalAlignment(SwingConstants.LEFT);
            btnSettings.setBorder(iconPadding);
            if (isAdmin) {
                btnUserMgmt.setText("User Management");
                btnUserMgmt.setHorizontalAlignment(SwingConstants.LEFT);
                btnUserMgmt.setBorder(iconPadding);
            }
        }
    }

    private JPanel createMenuPanel(String name) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(name + " Menu");
        label.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(label, new GridBagConstraints());
        return panel;
    }

    private void toggleSidebar() {
        if (isAnimating) {
            return;
        }
        isAnimating = true;
        sidebarCollapsed = !sidebarCollapsed;

        final int targetWidth = sidebarCollapsed ? sidebarMinWidth : sidebarMaxWidth;
        final int step = sidebarCollapsed ? -14 : 14;

        if (sidebarTimer != null && sidebarTimer.isRunning()) {
            sidebarTimer.stop();
        }

        // Only update buttons ONCE at start/end:
        if (sidebarCollapsed) {
            // Collapsing: remove padding before animation starts
            updateSidebarButtonsForCollapse(true);
        }

        sidebarTimer = new Timer(10, null);
        sidebarTimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sidebarCurrentWidth += step;
                boolean reached = sidebarCollapsed
                        ? (sidebarCurrentWidth <= targetWidth)
                        : (sidebarCurrentWidth >= targetWidth);
                if (reached) {
                    sidebarCurrentWidth = targetWidth;
                    sidebarTimer.stop();
                    isAnimating = false;
                    if (!sidebarCollapsed) {
                        // Expanding: add padding after animation ends
                        updateSidebarButtonsForCollapse(false);
                    }
                }
                updateSidebarWidth(sidebarCurrentWidth);
            }
        });
        sidebarTimer.start();
    }

    private void updateSidebarWidth(int width) {
        String constraint = "w " + width + "!, h 100%, dock west";
        migLayout.setComponentConstraints(sidebar, constraint);
        containerPanel.revalidate();
        containerPanel.repaint();
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu("Alice", "admin"); // Try "user" and "admin"
        });
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
