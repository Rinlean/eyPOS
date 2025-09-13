package test;

import MenuPanels.SalesChartPanel;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple test to verify the SalesChartPanel works correctly
 */
public class SalesChartTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sales Chart Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            
            // Create test data
            Map<String, Double> testData = new HashMap<>();
            testData.put("2024-01-15", 1250.75);
            testData.put("2024-01-16", 980.50);
            testData.put("2024-01-17", 1450.25);
            testData.put("2024-01-18", 1100.00);
            testData.put("2024-01-19", 1750.80);
            testData.put("2024-01-20", 890.40);
            testData.put("2024-01-21", 1350.60);
            
            SalesChartPanel chartPanel = new SalesChartPanel();
            chartPanel.setSalesData(testData);
            
            frame.add(chartPanel);
            frame.setVisible(true);
        });
    }
}