package MenuPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple bar chart component to display daily sales data
 */
public class SalesChartPanel extends JPanel {
    private Map<String, Double> salesData;
    private String title = "Daily Sales Totals";
    
    public SalesChartPanel() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.WHITE);
    }
    
    public void setSalesData(Map<String, Double> salesData) {
        this.salesData = salesData;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (salesData == null || salesData.isEmpty()) {
            drawNoDataMessage(g);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawChart(g2d);
        
        g2d.dispose();
    }
    
    private void drawNoDataMessage(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        String message = "No sales data available";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
    }
    
    private void drawChart(Graphics2D g2d) {
        // Chart dimensions and margins
        int margin = 50;
        int chartWidth = getWidth() - 2 * margin;
        int chartHeight = getHeight() - 2 * margin - 40; // Extra space for title
        int chartX = margin;
        int chartY = margin + 30; // Space for title
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics titleFm = g2d.getFontMetrics();
        int titleX = (getWidth() - titleFm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 25);
        
        // Convert data to lists for easier processing
        List<String> dates = new ArrayList<>(salesData.keySet());
        List<Double> values = new ArrayList<>();
        
        // Find max value for scaling
        double maxValue = 0;
        for (String date : dates) {
            double value = salesData.get(date);
            values.add(value);
            maxValue = Math.max(maxValue, value);
        }
        
        if (maxValue == 0) {
            drawNoDataMessage(g2d);
            return;
        }
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        // Y-axis
        g2d.drawLine(chartX, chartY, chartX, chartY + chartHeight);
        // X-axis
        g2d.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight);
        
        // Calculate bar dimensions
        int barCount = dates.size();
        if (barCount == 0) return;
        
        int barWidth = Math.max(20, (chartWidth - 10) / barCount - 10);
        int barSpacing = (chartWidth - barCount * barWidth) / (barCount + 1);
        
        // Draw bars
        g2d.setColor(new Color(66, 139, 202)); // Nice blue color
        Font labelFont = new Font("Arial", Font.PLAIN, 10);
        g2d.setFont(labelFont);
        FontMetrics labelFm = g2d.getFontMetrics();
        
        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            double value = values.get(i);
            
            // Calculate bar height (proportional to value)
            int barHeight = (int) ((value / maxValue) * (chartHeight - 20));
            
            // Calculate bar position
            int barX = chartX + barSpacing + i * (barWidth + barSpacing);
            int barY = chartY + chartHeight - barHeight;
            
            // Draw bar
            Rectangle2D bar = new Rectangle2D.Double(barX, barY, barWidth, barHeight);
            g2d.fill(bar);
            
            // Draw value on top of bar with currency formatting
            g2d.setColor(Color.BLACK);
            String valueStr = "Php " + String.format("%.0f", value);
            int valueX = barX + (barWidth - labelFm.stringWidth(valueStr)) / 2;
            int valueY = barY - 5;
            if (valueY > chartY) {
                g2d.drawString(valueStr, valueX, valueY);
            }
            
            // Draw date label (simplified - just show last part of date)
            String dateLabel = date.length() > 5 ? date.substring(5) : date; // Show MM-DD
            int labelX = barX + (barWidth - labelFm.stringWidth(dateLabel)) / 2;
            int labelY = chartY + chartHeight + 15;
            g2d.drawString(dateLabel, labelX, labelY);
            
            // Reset color for next bar
            g2d.setColor(new Color(66, 139, 202));
        }
        
        // Draw Y-axis labels with currency formatting
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
        FontMetrics axisFm = g2d.getFontMetrics();
        
        // Draw max value at top with Php prefix
        String maxLabel = "Php " + String.format("%.0f", maxValue);
        g2d.drawString(maxLabel, chartX - axisFm.stringWidth(maxLabel) - 5, chartY + 5);
        
        // Draw 0 at bottom with Php prefix
        String zeroLabel = "Php 0";
        g2d.drawString(zeroLabel, chartX - axisFm.stringWidth(zeroLabel) - 5, chartY + chartHeight + 5);
        
        // Draw middle value with Php prefix
        String midLabel = "Php " + String.format("%.0f", maxValue / 2);
        g2d.drawString(midLabel, chartX - axisFm.stringWidth(midLabel) - 5, chartY + chartHeight / 2 + 5);
    }
}