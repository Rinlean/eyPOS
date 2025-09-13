package MenuPanels;

import DAOstuff.sales.SaleDAO;
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
    private JButton refreshButton;
    private JButton exportButton;

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

        // Create export button
        exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(e -> exportSalesDataToExcel());
        titlePanel.add(exportButton);

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
                JOptionPane.showMessageDialog(this, "Exported sales data to Excel successfully!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to export Excel: " + ex.getMessage());
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
