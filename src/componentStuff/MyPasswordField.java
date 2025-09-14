package componentStuff;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;

public class MyPasswordField extends JPasswordField {

    private Icon prefixIcon;
    private Icon suffixIcon;
    private String hint = "";

    private final Icon showIcon;
    private final Icon hideIcon;
    private boolean showPassword = false;
    private final char defaultEchoChar;

    public MyPasswordField() {
        super();

        // Load icons (null-safe handling later)
        this.showIcon = new ImageIcon(getClass().getResource("/imageStuff/pass1.png"));
        this.hideIcon = new ImageIcon(getClass().getResource("/imageStuff/pass2.png"));

        this.defaultEchoChar = getEchoChar();

        // Visuals aligned with MyTextField
        setOpaque(false);                                // we paint our own background
        setBackground(new Color(243, 224, 255));         // same soft background
        setForeground(Color.decode("#000000"));
        setCaretColor(getForeground());
        setFont(new java.awt.Font("sansserif", 0, 13));
        setSelectionColor(new Color(75, 175, 152));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Start "hidden" with eye icon
        setSuffixIcon(hideIcon);

        // Ensure hint hides/shows immediately when focus changes
        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { repaint(); }
            @Override public void focusLost(FocusEvent e) { repaint(); }
        });

        // Make sure clicking anywhere requests focus (so hint hides on click)
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
            @Override public void mouseClicked(MouseEvent e) {
                // Toggle when clicking the suffix icon
                if (suffixIcon != null) {
                    int iconX = getWidth() - suffixIcon.getIconWidth() - 10;
                    int iconY = (getHeight() - suffixIcon.getIconHeight()) / 2;
                    Rectangle iconBounds = new Rectangle(iconX, iconY, suffixIcon.getIconWidth(), suffixIcon.getIconHeight());
                    if (iconBounds.contains(e.getPoint())) {
                        togglePasswordVisibility();
                    }
                }
            }
        });

        // Hand cursor when hovering the suffix icon
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                if (suffixIcon != null) {
                    int iconX = getWidth() - suffixIcon.getIconWidth() - 10;
                    int iconY = (getHeight() - suffixIcon.getIconHeight()) / 2;
                    Rectangle iconBounds = new Rectangle(iconX, iconY, suffixIcon.getIconWidth(), suffixIcon.getIconHeight());
                    setCursor(iconBounds.contains(e.getPoint())
                            ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                            : Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                }
            }
        });
    }

    // Toggle echo char and update icon
    private void togglePasswordVisibility() {
        showPassword = !showPassword;
        if (showPassword) {
            setEchoChar((char) 0);
            setSuffixIcon(showIcon);
        } else {
            setEchoChar(defaultEchoChar);
            setSuffixIcon(hideIcon);
        }
        repaint();
    }

    // Accessors aligned with MyTextField
    public String getHint() { return hint; }
    public void setHint(String hint) { this.hint = (hint == null) ? "" : hint; repaint(); }

    public Icon getPrefixIcon() { return prefixIcon; }
    public void setPrefixIcon(Icon prefixIcon) { this.prefixIcon = prefixIcon; initBorder(); repaint(); }

    public Icon getSuffixIcon() { return suffixIcon; }
    public void setSuffixIcon(Icon suffixIcon) { this.suffixIcon = suffixIcon; initBorder(); repaint(); }

    // Paint like MyTextField: background, text, icons, then hint
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Rounded background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            // Default text/caret/selection painting
            super.paintComponent(g2);

            // Icons
            paintIcons(g2);

            // Hint (only when empty and not focused)
            if (getPassword().length == 0 && !isFocusOwner() && !hint.isEmpty()) {
                Insets ins = getInsets();
                FontMetrics fm = g2.getFontMetrics(getFont());
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.setColor(new Color(150, 150, 150));
                g2.drawString(hint, ins.left, y);
            }
        } finally {
            g2.dispose();
        }
    }

    // Ensure a comfortable min height (width from columns/layout)
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = Math.max(d.height, 35);
        return d;
    }

    private void paintIcons(Graphics2D g2) {
        if (prefixIcon != null) {
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            prefixIcon.paintIcon(this, g2, 10, y);
        }
        if (suffixIcon != null) {
            int x = getWidth() - suffixIcon.getIconWidth() - 10;
            int y = (getHeight() - suffixIcon.getIconHeight()) / 2;
            suffixIcon.paintIcon(this, g2, x, y);
        }
    }

    private void initBorder() {
        int left = 10;
        int right = 10;
        if (prefixIcon != null) left = prefixIcon.getIconWidth() + 20;
        if (suffixIcon != null) right = suffixIcon.getIconWidth() + 20;
        setBorder(BorderFactory.createEmptyBorder(10, left, 10, right));
    }
}