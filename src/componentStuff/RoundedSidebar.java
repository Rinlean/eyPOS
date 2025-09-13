package componentStuff;

import javax.swing.*;
import java.awt.*;

public class RoundedSidebar extends JPanel {
    private int arcWidth = 30; // Adjust for roundness
    private int arcHeight = 30; // Adjust for roundness

    public RoundedSidebar() {
        setOpaque(false); // We paint the background ourselves
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sidebar background color (match your app)
        g2.setColor(getBackground());

        // Only round the right side
        int w = getWidth();
        int h = getHeight();
        int aw = arcWidth;
        int ah = arcHeight;
        // Draw a rectangle with rounded right edges
        g2.fillRoundRect(0, 0, w, h, aw, ah);
        // Cover the left corners with rectangles to make them square
        g2.fillRect(0, 0, aw / 2, h);

        g2.dispose();
        super.paintComponent(g);
    }

    public void setArc(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        repaint();
    }
}