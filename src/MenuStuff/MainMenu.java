package MenuStuff;

import componentStuff.Button;
import componentStuff.RoundedSidebar;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

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

    private static class SidebarButtonDef {

        final String text;
        final String iconPath;
        final String cardName;
        final boolean adminOnly;

        SidebarButtonDef(String text, String iconPath, String cardName, boolean adminOnly) {
            this.text = text;
            this.iconPath = iconPath;
            this.cardName = cardName;
            this.adminOnly = adminOnly;
        }
    }

    //to add/remove/reorder sidebar menu buttons:
    private final List<SidebarButtonDef> buttonDefs = Arrays.asList(
            new SidebarButtonDef("Dashboard", "/imagestuff/mail.png", "Dashboard", false),
            new SidebarButtonDef("Sales", "/imagestuff/mail.png", "Sales", false),
            new SidebarButtonDef("Products", "/imagestuff/mail.png", "Products", false),
            new SidebarButtonDef("Products", "/imagestuff/mail.png", "Products", false),
            new SidebarButtonDef("User Management", "/imagestuff/mail.png", "UserMgmt", true)
    );
    //no more to edit further here
    private final List<Button> sidebarButtons = new ArrayList<>();

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

        // Use glue panels to center buttons vertically
        sidebar.add(Box.createVerticalGlue());
        for (SidebarButtonDef def : buttonDefs) {
            if (def.adminOnly && !isAdmin) {
                continue;
            }
            Button btn = createSidebarButton(def);
            sidebarButtons.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(5));
            mainContent.add(createMenuPanel(def.text), def.cardName);
        }
        sidebar.add(Box.createVerticalGlue());

        migLayout = new MigLayout(
                "insets 0, gap 0",
                "[left][grow,fill]",
                "[grow,fill]"
        );
        containerPanel = new JPanel(migLayout);
        containerPanel.add(sidebar, "w " + sidebarCurrentWidth + "!, h 100%, dock west");
        containerPanel.add(mainContent, "grow, push");

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

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (" + role + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

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

    private JPanel createMenuPanel(String name) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(name + " Menau");
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
        String constraint = "w " + width + "!, h 100%, dock west";
        migLayout.setComponentConstraints(sidebar, constraint);
        containerPanel.revalidate();
        containerPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu("Rin", "admin");
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
