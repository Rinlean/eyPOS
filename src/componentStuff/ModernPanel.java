package componentStuff;

import javax.swing.*;
import java.awt.*;

public class ModernPanel extends JPanel {
    
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 20);
    private int cornerRadius = 12;
    private boolean hasShadow = true;
    
    public ModernPanel() {
        super();
        initializeStyle();
    }
    
    public ModernPanel(LayoutManager layout) {
        super(layout);
        initializeStyle();
    }
    
    public ModernPanel(boolean hasShadow) {
        super();
        this.hasShadow = hasShadow;
        initializeStyle();
    }
    
    public ModernPanel(LayoutManager layout, boolean hasShadow) {
        super(layout);
        this.hasShadow = hasShadow;
        initializeStyle();
    }
    
    private void initializeStyle() {
        setOpaque(false);
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setShadowEnabled(boolean enabled) {
        this.hasShadow = enabled;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow if enabled
        if (hasShadow) {
            g2.setColor(SHADOW_COLOR);
            g2.fillRoundRect(3, 3, width - 6, height - 6, cornerRadius, cornerRadius);
        }
        
        // Draw main background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width - (hasShadow ? 3 : 0), height - (hasShadow ? 3 : 0), cornerRadius, cornerRadius);
        
        // Draw border
        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, width - (hasShadow ? 4 : 1), height - (hasShadow ? 4 : 1), cornerRadius, cornerRadius);
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    public static JPanel createTitledPanel(String title, JComponent component) {
        ModernPanel panel = new ModernPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        
        return panel;
    }
    
    public static JPanel createCenteredPanel(JComponent component) {
        ModernPanel panel = new ModernPanel(new BorderLayout());
        
        // Create a wrapper panel to center the component
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.setOpaque(false);
        centerWrapper.add(component);
        
        panel.add(centerWrapper, BorderLayout.CENTER);
        return panel;
    }
}