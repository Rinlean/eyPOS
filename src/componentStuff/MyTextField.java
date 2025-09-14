package componentStuff;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTextField;

public class MyTextField extends JTextField {

    private Icon prefixIcon;
    private Icon suffixIcon;
    private String hint = "";

    public MyTextField() {
        super();
        // We paint our own background; keep component non-opaque so UI doesn't clear it.
        setOpaque(false);
        setBackground(new Color(243, 224, 255));      // soft background
        setForeground(Color.decode("#000000"));
        setCaretColor(getForeground());
        setFont(new java.awt.Font("sansserif", 0, 13));
        setSelectionColor(new Color(75, 175, 152));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ensure hint hides/shows immediately when focus changes
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });

        // Make sure clicking anywhere requests focus (so hint hides on click)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = (hint == null) ? "" : hint;
        repaint();
    }

    public Icon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        initBorder();
        repaint();
    }

    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        initBorder();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            // Paint text/caret/selection
            super.paintComponent(g2);

            // Icons (drawn in the padded area defined by our border)
            paintIcons(g2);

            // Hint when empty and not focused
            if (getText().isEmpty() && !isFocusOwner() && !hint.isEmpty()) {
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

    // Provide a comfortable default height; width still based on columns/layout
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
        if (prefixIcon != null) {
            left = prefixIcon.getIconWidth() + 20;
        }
        if (suffixIcon != null) {
            right = suffixIcon.getIconWidth() + 20;
        }
        setBorder(BorderFactory.createEmptyBorder(10, left, 10, right));
    }
}
