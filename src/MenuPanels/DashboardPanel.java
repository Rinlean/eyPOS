package MenuPanels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import componentStuff.DatabaseUtil;
import componentStuff.ProductUpdateListener;

public class DashboardPanel extends JPanel implements ProductUpdateListener {

    private JLabel totalTransactionsLabel;
    private JLabel profitVsRevenueLabel;
    private JTable topSellingProductsTable;
    private JTable lowStockTable;
    private JTable recentTransactionsTable;

    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // UI Structure
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        totalTransactionsLabel = new JLabel("Total Transactions: --");
        profitVsRevenueLabel = new JLabel("Profit: -- | Revenue: --");
        totalTransactionsLabel.setFont(totalTransactionsLabel.getFont().deriveFont(Font.BOLD, 16f));
        profitVsRevenueLabel.setFont(profitVsRevenueLabel.getFont().deriveFont(Font.BOLD, 16f));
        summaryPanel.add(totalTransactionsLabel);
        summaryPanel.add(profitVsRevenueLabel);

        JPanel midPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topSellingProductsTable = new JTable();
        lowStockTable = new JTable();
        midPanel.add(createTitledPanel("Top-Selling Products", new JScrollPane(topSellingProductsTable)));
        midPanel.add(createTitledPanel("Low Stock Alerts", new JScrollPane(lowStockTable)));

        recentTransactionsTable = new JTable();
        JPanel bottomPanel = createTitledPanel("Recent Transactions", new JScrollPane(recentTransactionsTable));

        add(summaryPanel, BorderLayout.NORTH);
        add(midPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void loadData() {
        // Number of Transactions
        int numTransactions = getTotalNumberOfTransactions();
        totalTransactionsLabel.setText("Total Transactions: " + numTransactions);

        // Profit vs Revenue
        double[] profitAndRevenue = getProfitAndRevenue();
        profitVsRevenueLabel.setText(String.format("Profit: Php %.2f | Revenue: Php %.2f", profitAndRevenue[0], profitAndRevenue[1]));

        // Top-Selling Products Table
        List<Object[]> topSellers = getTopSellingProducts();
        topSellingProductsTable.setModel(new DefaultTableModel(
                topSellers.toArray(new Object[0][]),
                new Object[]{"Product", "Units Sold", "Revenue"}
        ));

        // Low Stock Alerts Table
        List<Object[]> lowStock = getLowStockProducts();
        lowStockTable.setModel(new DefaultTableModel(
                lowStock.toArray(new Object[0][]),
                new Object[]{"Product", "Stock Left"}
        ));

        // Recent Transactions Table
        List<Object[]> recentSales = getRecentTransactions();
        recentTransactionsTable.setModel(new DefaultTableModel(
                recentSales.toArray(new Object[0][]),
                new Object[]{"Date", "Product", "Qty", "Total", "Customer"}
        ));
    }

    private int getTotalNumberOfTransactions() {
        String sql = "SELECT COUNT(*) FROM sales";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns [profit, revenue] as a double array.
     * - revenue = SUM(sales_items.quantity * sales_items.price)
     * - profit = SUM(sales_items.quantity * (sales_items.price - products.MSRP))
     *   (using MSRP as cost)
     */
    private double[] getProfitAndRevenue() {
        double revenue = 0;
        double profit = 0;
        String sql = """
            SELECT si.quantity, si.price, p.MSRP
            FROM sales_items si
            JOIN products p ON si.product_id = p.ProdId
        """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");      // sell price per unit
                double cost = 0.0;
                try {
                    cost = rs.getDouble("MSRP");           // cost per unit
                    if (rs.wasNull()) cost = 0.0;
                } catch (SQLException e) {
                    // If MSRP column doesn't exist, default to 0
                    cost = 0.0;
                }
                revenue += qty * price;
                profit += qty * (price - cost);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new double[]{profit, revenue};
    }

    /**
     * Returns a list of Object[]: [Product Name, Units Sold, Revenue]
     */
    private List<Object[]> getTopSellingProducts() {
        List<Object[]> results = new ArrayList<>();
        String sql = """
            SELECT p.ProdName, SUM(si.quantity) AS units_sold, SUM(si.quantity * si.price) AS revenue
            FROM sales_items si
            JOIN products p ON si.product_id = p.ProdId
            GROUP BY p.ProdId, p.ProdName
            ORDER BY units_sold DESC
            LIMIT 10
        """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("ProdName");
                int unitsSold = rs.getInt("units_sold");
                double revenue = rs.getDouble("revenue");
                results.add(new Object[]{name, unitsSold, String.format("Php %.2f", revenue)});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    /**
     * Returns a list of Object[]: [Product Name, Stock Left]
     */
    private List<Object[]> getLowStockProducts() {
        List<Object[]> results = new ArrayList<>();
        String sql = """
            SELECT ProdName, Quantity
            FROM products
            WHERE Quantity <= ?
            ORDER BY Quantity ASC
            LIMIT 10
        """;
        int lowStockThreshold = 50; // adjust as needed
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lowStockThreshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("ProdName");
                    int stock = rs.getInt("Quantity");
                    results.add(new Object[]{name, stock});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    /**
     * Returns a list of Object[]: [Date, Product, Qty, Total, Customer]
     * Note: There's no customer field in your sales_items or sales table, so this will use null/empty for Customer.
     */
    private List<Object[]> getRecentTransactions() {
        List<Object[]> results = new ArrayList<>();
        String sql = """
            SELECT s.sale_date, p.ProdName, si.quantity, (si.quantity * si.price) AS total
            FROM sales_items si
            JOIN sales s ON si.sale_id = s.sale_id
            JOIN products p ON si.product_id = p.ProdId
            ORDER BY s.sale_date DESC
            LIMIT 10
        """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String date = rs.getString("sale_date");
                String product = rs.getString("ProdName");
                int qty = rs.getInt("quantity");
                double total = rs.getDouble("total");
                results.add(new Object[]{
                        date,
                        product,
                        qty,
                        String.format("Php %.2f", total),
                        "" // Customer (not available)
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    @Override
    public void onProductsUpdated() {
        // Refresh the dashboard data when products are updated
        loadData();
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
