package MenuStuff;

import componentStuff.Button;
import componentStuff.RoundedSidebar;
import componentStuff.ModernDialog;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import MenuPanels.*;

public class MainMenu extends JFrame {

    private JPanel sidebar;
    private JPanel mainContent;
    private CardLayout cardLayout;
    private boolean sidebarCollapsed = false;
    private boolean isAdmin = false;
    private final int sidebarMaxWidth = 200;
    private final int sidebarMinWidth = 60;
    private int sidebarCurrentWidth = sidebarMaxWidth;
    private javax.swing.Timer sidebarTimer;

    private boolean isAnimating = false;

    private JPanel containerPanel;
    private MigLayout migLayout;

    private final Border iconPadding = new EmptyBorder(0, 16, 0, 0);
    private final Border noPadding = new EmptyBorder(0, 0, 0, 0);

    // Only edit this definition to add/remove panels!
    private static class SidebarButtonDef {

        final String text;
        final String iconPath;
        final String cardName;
        final boolean adminOnly;
        final Supplier<JPanel> panelSupplier;

        SidebarButtonDef(String text, String iconPath, String cardName, boolean adminOnly, Supplier<JPanel> panelSupplier) {
            this.text = text;
            this.iconPath = iconPath;
            this.cardName = cardName;
            this.adminOnly = adminOnly;
            this.panelSupplier = panelSupplier;
        }
    }

    // --------- eyyy pag addn stuff ----------
    private final List<SidebarButtonDef> buttonDefs = Arrays.asList(
            new SidebarButtonDef("Check Out", "/imagestuff/mail.png", "ChckOut", false, CheckoutPanel::new),
            new SidebarButtonDef("Dashboard", "/imagestuff/mail.png", "Dashboard", false, DashboardPanel::new),
            new SidebarButtonDef("Sales", "/imagestuff/mail.png", "Sales", false, SalesPanel::new),
            new SidebarButtonDef("Products", "/imagestuff/mail.png", "Products", false, () -> new ProductsPanel(isAdmin)),
            new SidebarButtonDef("User Management", "/imagestuff/mail.png", "UserMgmt", true, UserMgmtPanel::new)
    );
    // ---------------------------------------------------------------

    private final List<Button> sidebarButtons = new ArrayList<>();
    
    // Store references to panels for cross-panel communication
    private CheckoutPanel checkoutPanel;
    private ProductsPanel productsPanel;
    private DashboardPanel dashboardPanel;

    public MainMenu(String username, String role) {
        this.isAdmin = "admin".equalsIgnoreCase(role);

        setTitle("eyPOS Main Menu - " + role);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        sidebar = new RoundedSidebar();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(60, 63, 65));
        sidebar.setPreferredSize(new Dimension(sidebarMaxWidth, getHeight()));

        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);

        // Create panels first so we can connect them
        productsPanel = new ProductsPanel(isAdmin);
        checkoutPanel = new CheckoutPanel(productsPanel);
        dashboardPanel = new DashboardPanel();
        
        // Connect panels - products panel notifies checkout and dashboard panels of updates
        productsPanel.addUpdateListener(checkoutPanel);
        productsPanel.addUpdateListener(dashboardPanel);

        sidebar.add(Box.createVerticalGlue());
        for (SidebarButtonDef def : buttonDefs) {
            if (def.adminOnly && !isAdmin) {
                continue;
            }
            Button btn = createSidebarButton(def);
            sidebarButtons.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(5));
            
            // Add the appropriate panel instance
            JPanel panel;
            if ("ChckOut".equals(def.cardName)) {
                panel = checkoutPanel;
            } else if ("Products".equals(def.cardName)) {
                panel = productsPanel;
            } else if ("Dashboard".equals(def.cardName)) {
                panel = dashboardPanel;
            } else {
                panel = def.panelSupplier.get();
            }
            mainContent.add(panel, def.cardName);
        }
        sidebar.add(Box.createVerticalGlue());

        migLayout = new MigLayout(
                "insets 0, gap 0, fillx, filly",
                "[" + sidebarCurrentWidth + "!][0:0,grow,fill]",
                "[grow,fill]"
        );
        containerPanel = new JPanel(migLayout);
        containerPanel.add(sidebar, "cell 0 0, grow, wmin " + sidebarCurrentWidth + ", wmax " + sidebarCurrentWidth);
        containerPanel.add(mainContent, "cell 1 0, grow, push");

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        Button toggleButton = new Button();
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setPreferredSize(new Dimension(40, 40));
        toggleButton.setBackground(new Color(41, 39, 76));
        toggleButton.setForeground(new Color(250, 250, 250));
        try {
            ImageIcon menuIcon = new ImageIcon(getClass().getResource("/imagestuff/menu.png"));
            Image scaled = menuIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            toggleButton.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            toggleButton.setText("☰");
        }
        toggleButton.addActionListener(e -> toggleSidebar());
        leftPanel.add(toggleButton);

        // --- ADD THIS SECTION FOR LOGOUT ---
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (" + role + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> {
            int confirm = ModernDialog.showConfirmDialog(this, "Are you sure you want to logout?", "Logout");
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginMenu().setVisible(true);
            }
        });

        rightPanel.add(welcomeLabel);
        rightPanel.add(logoutButton);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        int i = 0;
        for (SidebarButtonDef def : buttonDefs) {
            if (def.adminOnly && !isAdmin) {
                continue;
            }
            final String card = def.cardName;
            sidebarButtons.get(i).addActionListener(e -> cardLayout.show(mainContent, card));
            i++;
        }

        updateSidebarButtonsForCollapse(sidebarCollapsed);
        cardLayout.show(mainContent, buttonDefs.get(0).cardName);
        
        // Add component listener to ensure layout stability during window resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ensure layout is maintained when window is resized or maximized
                containerPanel.revalidate();
                containerPanel.repaint();
            }
        });
        
        setVisible(true);
    }

    private Button createSidebarButton(SidebarButtonDef def) {
        Button btn = new Button();
        btn.setBackground(new Color(41, 39, 76));
        btn.setForeground(new Color(250, 250, 250));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setText(def.text);
        btn.setIconTextGap(15);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);

        try {
            btn.setIcon(new ImageIcon(getClass().getResource(def.iconPath)));
        } catch (Exception e) {
            System.err.println("Icon not found: " + def.iconPath);
            btn.setText(def.text.substring(0, 1));
        }

        return btn;
    }

    private void configureSidebarButton(Button btn, String text, boolean collapsed) {
        if (collapsed) {
            btn.setText("");
            btn.setHorizontalAlignment(SwingConstants.CENTER);
            btn.setBorder(noPadding);
        } else {
            btn.setText(text);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(iconPadding);
        }
        btn.revalidate();
        btn.repaint();
    }

    private void updateSidebarButtonsForCollapse(boolean collapsed) {
        int i = 0;
        for (SidebarButtonDef def : buttonDefs) {
            if (def.adminOnly && !isAdmin) {
                continue;
            }
            configureSidebarButton(sidebarButtons.get(i), def.text, collapsed);
            i++;
        }
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

        if (sidebarCollapsed) {
            updateSidebarButtonsForCollapse(true);
        }

        sidebarTimer = new javax.swing.Timer(10, (ActionListener) null);
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
                        updateSidebarButtonsForCollapse(false);
                    }
                }
                updateSidebarWidth(sidebarCurrentWidth);
            }
        });
        sidebarTimer.start();
    }

    private void updateSidebarWidth(int width) {
        // Update the column constraint to maintain stable layout
        migLayout.setColumnConstraints("[" + width + "!][0:0,grow,fill]");
        migLayout.setComponentConstraints(sidebar, "cell 0 0, grow, wmin " + width + ", wmax " + width);
        containerPanel.revalidate();
        containerPanel.repaint();
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
