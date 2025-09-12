package componentStuff;

import java.awt.*;

public class RoundButton extends Button {
    public RoundButton() {
        super();
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setBackground(new Color(167, 134, 193));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int diameter = Math.min(getWidth(), getHeight());
        g2.setColor(getBackground());
        g2.fillOval(0, 0, diameter, diameter);

        // Draw the "X" text centered
        String text = getText();
        Font font = getFont().deriveFont(Font.BOLD, diameter * 0.6f); // Scales font with button
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textAscent = fm.getAscent();
        int textDescent = fm.getDescent();
        int x = (diameter - textWidth) / 2;
        int y = (diameter + textAscent - textDescent) / 2;

        g2.setColor(getForeground());
        g2.drawString(text, x, y);

        g2.dispose();
    }
}