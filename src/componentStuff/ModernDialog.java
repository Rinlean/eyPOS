package componentStuff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ModernDialog extends JDialog {
    
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color HEADER_COLOR = new Color(52, 73, 94);
    private static final Color HEADER_TEXT_COLOR = Color.WHITE;
    private static final Color BUTTON_PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color BUTTON_SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 12);
    
    private int result = JOptionPane.CANCEL_OPTION;
    
    public ModernDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initializeDialog();
    }
    
    public ModernDialog(Frame parent, String title) {
        this(parent, title, true);
    }
    
    private void initializeDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // ESC key to close
        getRootPane().registerKeyboardAction(
            e -> {
                result = JOptionPane.CANCEL_OPTION;
                dispose();
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    public static int showConfirmDialog(Component parent, String message, String title) {
        return showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
    }
    
    public static int showConfirmDialog(Component parent, String message, String title, int optionType) {
        Frame parentFrame = getParentFrame(parent);
        ModernDialog dialog = new ModernDialog(parentFrame, title, true);
        
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(HEADER_TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(CONTENT_FONT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        if (optionType == JOptionPane.YES_NO_OPTION) {
            JButton yesButton = createModernButton("Yes", true);
            JButton noButton = createModernButton("No", false);
            
            yesButton.addActionListener(e -> {
                dialog.result = JOptionPane.YES_OPTION;
                dialog.dispose();
            });
            
            noButton.addActionListener(e -> {
                dialog.result = JOptionPane.NO_OPTION;
                dialog.dispose();
            });
            
            buttonPanel.add(noButton);
            buttonPanel.add(yesButton);
        } else {
            JButton okButton = createModernButton("OK", true);
            okButton.addActionListener(e -> {
                dialog.result = JOptionPane.OK_OPTION;
                dialog.dispose();
            });
            buttonPanel.add(okButton);
        }
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
        return dialog.result;
    }
    
    public static void showMessageDialog(Component parent, String message, String title, int messageType) {
        Frame parentFrame = getParentFrame(parent);
        ModernDialog dialog = new ModernDialog(parentFrame, title, true);
        
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(parent);
        
        // Header panel with icon based on message type
        JPanel headerPanel = new JPanel(new BorderLayout());
        Color headerColor = HEADER_COLOR;
        if (messageType == JOptionPane.ERROR_MESSAGE) {
            headerColor = new Color(220, 53, 69);
        } else if (messageType == JOptionPane.WARNING_MESSAGE) {
            headerColor = new Color(255, 193, 7);
        } else if (messageType == JOptionPane.INFORMATION_MESSAGE) {
            headerColor = new Color(23, 162, 184);
        }
        headerPanel.setBackground(headerColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(HEADER_TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(CONTENT_FONT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton okButton = createModernButton("OK", true);
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private static JButton createModernButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(CONTENT_FONT);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setBackground(isPrimary ? BUTTON_PRIMARY_COLOR : BUTTON_SECONDARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Rounded appearance
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                Color originalColor = button.getBackground();
                Color hoverColor = originalColor.darker();
                button.setBackground(hoverColor);
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(isPrimary ? BUTTON_PRIMARY_COLOR : BUTTON_SECONDARY_COLOR);
                button.repaint();
            }
        });
        
        // Custom painting for rounded button
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 8, 8);
                
                super.paint(g, c);
                g2.dispose();
            }
        });
        
        return button;
    }
    
    public static String showInputDialog(Component parent, String message, String title, String defaultValue) {
        Frame parentFrame = getParentFrame(parent);
        ModernDialog dialog = new ModernDialog(parentFrame, title, true);
        
        dialog.setLayout(new BorderLayout());
        dialog.setSize(420, 200); // Slightly larger to ensure visibility
        dialog.setLocationRelativeTo(parent);
        // Ensure dialog is always on top and modal
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(HEADER_TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(CONTENT_FONT);
        contentPanel.add(messageLabel, BorderLayout.NORTH);
        
        // Make input field more prominent and visible with increased height
        JTextField inputField = new JTextField(defaultValue);
        inputField.setFont(new Font("Arial", Font.PLAIN, 16)); // Larger font for better visibility
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15) // Increased padding for taller field
        ));
        inputField.setPreferredSize(new Dimension(300, 45)); // Increased height from 35 to 45
        inputField.setMinimumSize(new Dimension(300, 45)); // Ensure minimum size
        contentPanel.add(inputField, BorderLayout.CENTER);
        
        final String[] result = {null};
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton cancelButton = createModernButton("Cancel", false);
        JButton okButton = createModernButton("OK", true);
        
        cancelButton.addActionListener(e -> dialog.dispose());
        okButton.addActionListener(e -> {
            result[0] = inputField.getText();
            dialog.dispose();
        });
        
        inputField.addActionListener(e -> {
            result[0] = inputField.getText();
            dialog.dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        inputField.requestFocus();
        inputField.selectAll();
        
        dialog.setVisible(true);
        return result[0];
    }
    
    private static Frame getParentFrame(Component parent) {
        while (parent != null && !(parent instanceof Frame)) {
            parent = parent.getParent();
        }
        return (Frame) parent;
    }
}