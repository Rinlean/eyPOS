package MenuPanels;

import DAOstuff.products.ProductDialog;
import DAOstuff.products.Product;
import DAOstuff.products.ProductsDAO;
import componentStuff.ProductUpdateListener;
import componentStuff.ModernTable;
import componentStuff.ModernScrollPane;
import componentStuff.ModernPanel;
import componentStuff.ModernDialog;
import componentStuff.MyTextField;
import componentStuff.Button;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ProductsPanel extends JPanel {

    private ModernTable productTable;
    private DefaultTableModel tableModel;
    private boolean isAdmin;
    private MyTextField searchField;
    private javax.swing.Timer debounceTimer;
    private List<ProductUpdateListener> updateListeners = new ArrayList<>();

    public ProductsPanel(boolean isAdmin) {
        this.isAdmin = isAdmin;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(248, 249, 250));

        // Search bar (always shown)
        ModernPanel searchPanel = new ModernPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchField = new MyTextField();
        searchField.setColumns(25);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchLabel.setForeground(new Color(52, 73, 94));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        setupSearchDebounce();

        // Table columns
        String[] columnNames = {"ID", "Barcode", "Name", "Price", "MSRP", "Description", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new ModernTable(tableModel);
        // Increase table size for better visibility
        productTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
        productTable.setRowHeight(35); // Increase row height for better readability
        ModernScrollPane scrollPane = new ModernScrollPane(productTable);
        JPanel tablePanel = ModernPanel.createCenteredPanel(scrollPane);
        add(tablePanel, BorderLayout.CENTER);

        // Admin controls
        if (isAdmin) {
            ModernPanel adminPanel = new ModernPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            Button addBtn = new Button();
            addBtn.setText("Add Product");
            addBtn.setBackground(new Color(40, 167, 69));
            addBtn.setForeground(Color.WHITE);
            addBtn.setFont(new Font("Arial", Font.BOLD, 14));
            addBtn.setPreferredSize(new Dimension(130, 35));
            
            Button editBtn = new Button();
            editBtn.setText("Edit Product");
            editBtn.setBackground(new Color(255, 193, 7));
            editBtn.setForeground(Color.BLACK);
            editBtn.setFont(new Font("Arial", Font.BOLD, 14));
            editBtn.setPreferredSize(new Dimension(130, 35));
            
            Button delBtn = new Button();
            delBtn.setText("Remove Product");
            delBtn.setBackground(new Color(220, 53, 69));
            delBtn.setForeground(Color.WHITE);
            delBtn.setFont(new Font("Arial", Font.BOLD, 14));
            delBtn.setPreferredSize(new Dimension(150, 35));
            
            adminPanel.add(addBtn);
            adminPanel.add(editBtn);
            adminPanel.add(delBtn);
            add(adminPanel, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> openProductDialog(null));
            editBtn.addActionListener(e -> {
                int row = productTable.getSelectedRow();
                if (row == -1) {
                    ModernDialog.showMessageDialog(this, "Select a product to edit.", "Edit Product", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                Product p = getProductFromRow(row);
                openProductDialog(p);
            });
            delBtn.addActionListener(e -> {
                int row = productTable.getSelectedRow();
                if (row == -1) {
                    ModernDialog.showMessageDialog(this, "Select a product to remove.", "Remove Product", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int confirm = ModernDialog.showConfirmDialog(this, "Are you sure you want to remove this product?", "Remove Product");
                if (confirm == JOptionPane.YES_OPTION) {
                    int prodId = (int) tableModel.getValueAt(row, 0);
                    ProductsDAO.deleteProduct(prodId);
                    refreshProductList();
                }
            });
        }

        refreshProductList();
    }

    private void setupSearchDebounce() {
        debounceTimer = new javax.swing.Timer(300, e -> searchProducts());
        debounceTimer.setRepeats(false);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                restartDebounce();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                restartDebounce();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                restartDebounce();
            }
        });
    }

    private void restartDebounce() {
        debounceTimer.restart();
    }

    private void searchProducts() {
        String keyword = searchField.getText().trim();
        List<Product> products;
        if (keyword.isEmpty()) {
            products = ProductsDAO.getAllProducts();
        } else {
            products = ProductsDAO.searchProducts(keyword);
        }
        setTableData(products);
    }

    private void openProductDialog(Product product) {
        ProductDialog dialog = new ProductDialog(SwingUtilities.getWindowAncestor(this), product);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            boolean success;
            if (product == null) {
                success = ProductsDAO.addProduct(dialog.getProduct());
            } else {
                success = ProductsDAO.updateProduct(dialog.getProduct());
            }
            if (success) {
                refreshProductList();
                notifyUpdateListeners(); // Notify other panels
            }
        }
    }

    private Product getProductFromRow(int row) {
        return new Product(
                (int) tableModel.getValueAt(row, 0),
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2),
                Double.parseDouble(tableModel.getValueAt(row, 3).toString()),
                Double.parseDouble(tableModel.getValueAt(row, 4).toString()),
                (String) tableModel.getValueAt(row, 5),
                (int) tableModel.getValueAt(row, 6)
        );
    }

    public void refreshProductList() {
        setTableData(ProductsDAO.getAllProducts());
    }

    public void refreshAndNotify() {
        refreshProductList();
        notifyUpdateListeners();
    }

    public void addUpdateListener(ProductUpdateListener listener) {
        updateListeners.add(listener);
    }

    private void notifyUpdateListeners() {
        for (ProductUpdateListener listener : updateListeners) {
            listener.onProductsUpdated();
        }
    }

    private void setTableData(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product prod : products) {
            tableModel.addRow(new Object[]{
                prod.getProdId(),
                prod.getProdBarCode(),
                prod.getProdName(),
                prod.getProdPrice(),
                prod.getMsrp(),
                prod.getProdDesc(),
                prod.getQuantity()
            });
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
