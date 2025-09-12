package componentStuff;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;

public class MyPasswordField extends JPasswordField {

    private final Icon showIcon;
    private final Icon hideIcon;
    private boolean showPassword = false;
    private final char defaultEchoChar;

    public MyPasswordField() {
        // --- Start of New Code ---

        // TODO: Replace with your actual icon paths
        this.showIcon = new ImageIcon(getClass().getResource("/imageStuff/pass1.png"));
        this.hideIcon = new ImageIcon(getClass().getResource("/imageStuff/pass2.png"));
        this.defaultEchoChar = getEchoChar(); // Store the default echo character (usually '*')

        // Set the initial suffix icon to the "hide" icon
        setSuffixIcon(hideIcon);

        // Add a mouse listener to handle clicks on the icon
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Define the clickable area for the suffix icon
                if (suffixIcon != null) {
                    int iconX = getWidth() - suffixIcon.getIconWidth() - 10;
                    int iconY = (getHeight() - suffixIcon.getIconHeight()) / 2;
                    int iconWidth = suffixIcon.getIconWidth();
                    int iconHeight = suffixIcon.getIconHeight();
                    Rectangle iconBounds = new Rectangle(iconX, iconY, iconWidth, iconHeight);

                    // Check if the click was inside the icon's bounds
                    if (iconBounds.contains(e.getPoint())) {
                        togglePasswordVisibility();
                    }
                }
            }
        });
        // --- End of New Code ---

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(0, 0, 0, 0));
        setForeground(Color.decode("#7A8C8D"));
        setFont(new java.awt.Font("sansserif", 0, 13));
        setSelectionColor(new Color(75, 175, 152));
    }
    
    // Method to toggle password visibility
    private void togglePasswordVisibility() {
        showPassword = !showPassword;
        if (showPassword) {
            // Show password: set echo char to 0 and switch to the "show" icon
            setEchoChar((char) 0);
            setSuffixIcon(showIcon);
        } else {
            // Hide password: restore default echo char and switch to the "hide" icon
            setEchoChar(defaultEchoChar);
            setSuffixIcon(hideIcon);
        }
    }
    
    // --- The rest of your existing code ---

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Icon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        initBorder();
    }

    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        initBorder();
    }

    private Icon prefixIcon;
    private Icon suffixIcon;
    private String hint = "";

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(243, 224, 255));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        paintIcon(g);
        super.paintComponent(g);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getPassword().length == 0) {
            int h = getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            g.setColor(new Color(200, 200, 200));
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    private void paintIcon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (prefixIcon != null) {
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            g2.drawImage(prefix, 10, y, this);
        }
        if (suffixIcon != null) {
            Image suffix = ((ImageIcon) suffixIcon).getImage();
            int y = (getHeight() - suffixIcon.getIconHeight()) / 2;
            g2.drawImage(suffix, getWidth() - suffixIcon.getIconWidth() - 10, y, this);
        }
    }

    private void initBorder() {
        int left = 15;
        int right = 15;
        if (prefixIcon != null) {
            left = prefixIcon.getIconWidth() + 15;
        }
        if (suffixIcon != null) {
            right = suffixIcon.getIconWidth() + 15;
        }
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, left, 10, right));
    }
}