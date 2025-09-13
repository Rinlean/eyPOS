package DAOstuff.sales;

import componentStuff.DatabaseUtil;
import java.sql.*;

public class SaleDAO {

    public int saveSale(Sale sale) throws SQLException {
        String insertSale = "INSERT INTO sales (sale_date, total_amount, amount_received, change_given) VALUES (?, ?, ?, ?)";
        String insertSaleItem = "INSERT INTO sales_items (sale_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        int saleId = -1;

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            // Insert sale and get generated sale_id
            try (PreparedStatement saleStmt = conn.prepareStatement(insertSale, Statement.RETURN_GENERATED_KEYS)) {
                saleStmt.setTimestamp(1, new Timestamp(sale.getDate().getTime()));
                saleStmt.setDouble(2, sale.getTotal());
                saleStmt.setDouble(3, sale.getAmountReceived());
                saleStmt.setDouble(4, sale.getChangeGiven());
                saleStmt.executeUpdate();

                try (ResultSet generatedKeys = saleStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        saleId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating sale failed, no ID obtained.");
                    }
                }
            }

            // Insert all sale items
            try (PreparedStatement itemStmt = conn.prepareStatement(insertSaleItem)) {
                for (SaleItem item : sale.getItems()) {
                    itemStmt.setInt(1, saleId);
                    itemStmt.setInt(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit();
            return saleId;
        } catch (SQLException e) {
            throw e;
        }
    }
}
