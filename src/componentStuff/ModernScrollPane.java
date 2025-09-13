package componentStuff;

import javax.swing.*;
import java.awt.*;

public class ModernScrollPane extends JScrollPane {
    
    private static final Color SCROLLBAR_TRACK_COLOR = new Color(240, 240, 240);
    private static final Color SCROLLBAR_THUMB_COLOR = new Color(180, 180, 180);
    private static final Color SCROLLBAR_THUMB_HOVER_COLOR = new Color(150, 150, 150);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    
    public ModernScrollPane() {
        super();
        initializeStyle();
    }
    
    public ModernScrollPane(Component view) {
        super(view);
        initializeStyle();
    }
    
    public ModernScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        initializeStyle();
    }
    
    private void initializeStyle() {
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        setBackground(Color.WHITE);
        getViewport().setBackground(Color.WHITE);
        
        // Style vertical scrollbar
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setUI(new ModernScrollBarUI());
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBlockIncrement(64);
        
        // Style horizontal scrollbar
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setUI(new ModernScrollBarUI());
        horizontalScrollBar.setUnitIncrement(16);
        horizontalScrollBar.setBlockIncrement(64);
    }
    
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        
        @Override
        protected void configureScrollBarColors() {
            trackColor = SCROLLBAR_TRACK_COLOR;
            thumbColor = SCROLLBAR_THUMB_COLOR;
            thumbHighlightColor = SCROLLBAR_THUMB_HOVER_COLOR;
            thumbLightShadowColor = SCROLLBAR_THUMB_COLOR;
            thumbDarkShadowColor = SCROLLBAR_THUMB_COLOR;
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color color = thumbColor;
            if (isDragging) {
                color = thumbHighlightColor;
            } else if (isThumbRollover()) {
                color = thumbHighlightColor;
            }
            
            g2.setColor(color);
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                int x = thumbBounds.x + 2;
                int y = thumbBounds.y;
                int width = thumbBounds.width - 4;
                int height = thumbBounds.height;
                g2.fillRoundRect(x, y, width, height, 6, 6);
            } else {
                int x = thumbBounds.x;
                int y = thumbBounds.y + 2;
                int width = thumbBounds.width;
                int height = thumbBounds.height - 4;
                g2.fillRoundRect(x, y, width, height, 6, 6);
            }
            
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            
            g2.dispose();
        }
    }
}