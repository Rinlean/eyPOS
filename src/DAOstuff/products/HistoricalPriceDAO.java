package DAOstuff.products;

import componentStuff.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricalPriceDAO {

    public static boolean addPriceHistory(HistoricalPrice history) {
        String sql = "INSERT INTO product_price_history (product_id, old_price, new_price, change_date, changed_by) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, history.getProductId());
            stmt.setDouble(2, history.getOldPrice());
            stmt.setDouble(3, history.getNewPrice());
            stmt.setTimestamp(4, new Timestamp(history.getChangeDate().getTime()));
            stmt.setString(5, history.getChangedBy());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<HistoricalPrice> getPriceHistory(int productId) {
        List<HistoricalPrice> history = new ArrayList<>();
        String sql = "SELECT * FROM product_price_history WHERE product_id = ? ORDER BY change_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                HistoricalPrice price = new HistoricalPrice(
                    rs.getInt("history_id"),
                    rs.getInt("product_id"),
                    rs.getDouble("old_price"),
                    rs.getDouble("new_price"),
                    rs.getTimestamp("change_date"),
                    rs.getString("changed_by")
                );
                history.add(price);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return history;
    }
}