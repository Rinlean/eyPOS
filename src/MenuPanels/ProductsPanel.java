package MenuPanels;

import DAOstuff.products.ProductDialog;
import DAOstuff.products.Product;
import DAOstuff.products.ProductsDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductsPanel extends JPanel {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private boolean isAdmin;
    private JTextField searchField;
    private javax.swing.Timer debounceTimer;

    public ProductsPanel(boolean isAdmin) {
        this.isAdmin = isAdmin;
        setLayout(new BorderLayout());

        // Search bar (always shown)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        setupSearchDebounce();

        // Table columns
        String[] columnNames = {"ID", "Barcode", "Name", "Price", "Description", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Admin controls
        if (isAdmin) {
            JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addBtn = new JButton("Add Product");
            JButton editBtn = new JButton("Edit Product");
            JButton delBtn = new JButton("Remove Product");
            adminPanel.add(addBtn);
            adminPanel.add(editBtn);
            adminPanel.add(delBtn);
            add(adminPanel, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> openProductDialog(null));
            editBtn.addActionListener(e -> {
                int row = productTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(this, "Select a product to edit.");
                    return;
                }
                Product p = getProductFromRow(row);
                openProductDialog(p);
            });
            delBtn.addActionListener(e -> {
                int row = productTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(this, "Select a product to remove.");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Remove Product", JOptionPane.YES_NO_OPTION);
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
            }
        }
    }

    private Product getProductFromRow(int row) {
        return new Product(
                (int) tableModel.getValueAt(row, 0),
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2),
                Double.parseDouble(tableModel.getValueAt(row, 3).toString()),
                (String) tableModel.getValueAt(row, 4),
                (int) tableModel.getValueAt(row, 5)
        );
    }

    public void refreshProductList() {
        setTableData(ProductsDAO.getAllProducts());
    }

    private void setTableData(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product prod : products) {
            tableModel.addRow(new Object[]{
                prod.getProdId(),
                prod.getProdBarCode(),
                prod.getProdName(),
                prod.getProdPrice(),
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
