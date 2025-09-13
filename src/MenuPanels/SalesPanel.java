package MenuPanels;

import DAOstuff.sales.SaleDAO;
import componentStuff.ModernPanel;
import componentStuff.ModernDialog;
import componentStuff.Button;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;
import java.io.FileOutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;

// Apache POI imports for Excel export
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SalesPanel extends JPanel {
    private SalesChartPanel chartPanel;
    private SaleDAO saleDAO;
    private Button refreshButton;
    private Button exportButton;

    public SalesPanel() {
        saleDAO = new SaleDAO();
        initializeComponents();
        loadSalesData();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new java.awt.Color(248, 249, 250));

        // Create title panel
        ModernPanel titlePanel = new ModernPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JLabel titleLabel = new JLabel("Sales Analytics");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        titleLabel.setForeground(new java.awt.Color(52, 73, 94));
        titlePanel.add(titleLabel);

        // Create refresh button - made larger for better text fit
        refreshButton = new Button();
        refreshButton.setText("Refresh Data");
        refreshButton.setBackground(new java.awt.Color(23, 162, 184));
        refreshButton.setForeground(java.awt.Color.WHITE);
        refreshButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        refreshButton.setPreferredSize(new Dimension(150, 40));
        refreshButton.addActionListener(e -> loadSalesData());
        titlePanel.add(refreshButton);

        // Create export button - made larger for better text fit
        exportButton = new Button();
        exportButton.setText("Export to Excel");
        exportButton.setBackground(new java.awt.Color(40, 167, 69));
        exportButton.setForeground(java.awt.Color.WHITE);
        exportButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        exportButton.setPreferredSize(new Dimension(170, 40));
        exportButton.addActionListener(e -> exportSalesDataToExcel());
        titlePanel.add(exportButton);

        add(titlePanel, BorderLayout.NORTH);

        // Create chart panel in a modern panel wrapper
        chartPanel = new SalesChartPanel();
        ModernPanel chartWrapper = new ModernPanel(new BorderLayout());
        chartWrapper.add(chartPanel, BorderLayout.CENTER);
        add(chartWrapper, BorderLayout.CENTER);
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

    private void exportSalesDataToExcel() {
        try {
            Map<String, Double> salesData = saleDAO.getDailySalesTotals();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as Excel");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                try (Workbook workbook = new XSSFWorkbook()) {
                    Sheet sheet = workbook.createSheet("Sales Data");
                    Row headerRow = sheet.createRow(0);
                    headerRow.createCell(0).setCellValue("Date");
                    headerRow.createCell(1).setCellValue("Total Sales");
                    int rowIdx = 1;
                    for (Map.Entry<String, Double> entry : salesData.entrySet()) {
                        Row row = sheet.createRow(rowIdx++);
                        row.createCell(0).setCellValue(entry.getKey());
                        row.createCell(1).setCellValue(entry.getValue());
                    }
                    sheet.autoSizeColumn(0);
                    sheet.autoSizeColumn(1);

                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        workbook.write(fos);
                    }
                }
                ModernDialog.showMessageDialog(this, "Exported sales data to Excel successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ModernDialog.showMessageDialog(this, "Failed to export Excel: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
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
