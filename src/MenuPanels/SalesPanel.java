package MenuPanels;

import DAOstuff.sales.SaleDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

public class SalesPanel extends JPanel {
    private SalesChartPanel chartPanel;
    private SaleDAO saleDAO;
    private JButton refreshButton;
    
    public SalesPanel() {
        saleDAO = new SaleDAO();
        initializeComponents();
        loadSalesData();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Sales Panel", SwingConstants.CENTER));
        
        // Create refresh button
        refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> loadSalesData());
        titlePanel.add(refreshButton);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Create chart panel
        chartPanel = new SalesChartPanel();
        add(chartPanel, BorderLayout.CENTER);
    }
    
    private void loadSalesData() {
        SwingUtilities.invokeLater(() -> {
            try {
                Map<String, Double> salesData = saleDAO.getDailySalesTotals();
                chartPanel.setSalesData(salesData);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading sales data: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
