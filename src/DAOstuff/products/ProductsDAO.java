package DAOstuff.products;

import componentStuff.DatabaseUtil;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO {

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("ProdId"),
                        rs.getString("ProdBarCode"),
                        rs.getString("ProdName"),
                        rs.getDouble("ProdPrice"),
                        rs.getString("ProdDesc"),
                        rs.getInt("Quantity")
                );
                products.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load products: " + ex.getMessage());
        }
        return products;
    }

    public static List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE ProdBarCode LIKE ? OR ProdName LIKE ? OR ProdDesc LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("ProdId"),
                        rs.getString("ProdBarCode"),
                        rs.getString("ProdName"),
                        rs.getDouble("ProdPrice"),
                        rs.getString("ProdDesc"),
                        rs.getInt("Quantity")
                );
                products.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Search failed: " + ex.getMessage());
        }
        return products;
    }

    public static boolean addProduct(Product product) {
        String sql = "INSERT INTO products (ProdBarCode, ProdName, ProdPrice, ProdDesc, Quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProdBarCode());
            stmt.setString(2, product.getProdName());
            stmt.setDouble(3, product.getProdPrice());
            stmt.setString(4, product.getProdDesc());
            stmt.setInt(5, product.getQuantity());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            if (ex.getSQLState() != null && ex.getSQLState().startsWith("23")) { // Integrity constraint violation
                JOptionPane.showMessageDialog(null, "Barcode must be unique! Another product already has this barcode.");
            } else {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to add product: " + ex.getMessage());
            }
            return false;
        }
    }

    public static boolean updateProduct(Product product) {
        String sql = "UPDATE products SET ProdBarCode=?, ProdName=?, ProdPrice=?, ProdDesc=?, Quantity=? WHERE ProdId=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProdBarCode());
            stmt.setString(2, product.getProdName());
            stmt.setDouble(3, product.getProdPrice());
            stmt.setString(4, product.getProdDesc());
            stmt.setInt(5, product.getQuantity());
            stmt.setInt(6, product.getProdId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            if (ex.getSQLState() != null && ex.getSQLState().startsWith("23")) {
                JOptionPane.showMessageDialog(null, "Barcode must be unique! Another product already has this barcode.");
            } else {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to update product: " + ex.getMessage());
            }
            return false;
        }
    }

    public static void deleteProduct(int prodId) {
        String sql = "DELETE FROM products WHERE ProdId=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prodId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete product: " + ex.getMessage());
        }
    }
}
