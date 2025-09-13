package MenuPanels;

import DAOstuff.products.Product;
import DAOstuff.products.ProductsDAO;
import DAOstuff.sales.Sale;
import DAOstuff.sales.SaleItem;
import DAOstuff.sales.SaleDAO;
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
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class CheckoutPanel extends JPanel implements ProductUpdateListener {

    private ModernTable productTable;
    private ModernTable cartTable;
    private DefaultTableModel productModel;
    private DefaultTableModel cartModel;
    private JLabel totalLabel;
    private List<Product> products;
    private List<CartItem> cart = new ArrayList<>();
    private ProductsDAO productDAO = new ProductsDAO();
    private SaleDAO saleDAO = new SaleDAO();
    private ProductsPanel productsPanel; // Reference to trigger notifications

    // Payment and numpad fields
    private MyTextField amountReceivedField;
    private MyTextField changeField;
    private JPanel numpadPanel;

    public CheckoutPanel() {
        this(null); // Default constructor for backward compatibility
    }

    public CheckoutPanel(ProductsPanel productsPanel) {
        this.productsPanel = productsPanel;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(248, 249, 250));

        // Product Table
        productModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Stock"}, 0);
        productTable = new ModernTable(productModel);
        productTable.setRowHeight(30); // Increase row height for better readability
        ModernScrollPane productScroll = new ModernScrollPane(productTable);

        // Cart Table
        cartModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Qty", "Subtotal"}, 0);
        cartTable = new ModernTable(cartModel);
        cartTable.setRowHeight(30); // Increase row height for better readability
        ModernScrollPane cartScroll = new ModernScrollPane(cartTable);

        // Buttons and total - increased sizes for better visibility and usability
        Button addToCartBtn = new Button();
        addToCartBtn.setText("Add to Cart");
        addToCartBtn.setBackground(new Color(40, 167, 69));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFont(new Font("Arial", Font.BOLD, 16));
        addToCartBtn.setPreferredSize(new Dimension(140, 45));
        
        Button removeFromCartBtn = new Button();
        removeFromCartBtn.setText("Remove Selected");
        removeFromCartBtn.setBackground(new Color(220, 53, 69));
        removeFromCartBtn.setForeground(Color.WHITE);
        removeFromCartBtn.setFont(new Font("Arial", Font.BOLD, 16));
        removeFromCartBtn.setPreferredSize(new Dimension(160, 45));
        
        Button checkoutBtn = new Button();
        checkoutBtn.setText("Check Out");
        checkoutBtn.setBackground(new Color(0, 123, 255));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 18));
        checkoutBtn.setPreferredSize(new Dimension(140, 50));
        
        totalLabel = new JLabel("Total: Php 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(52, 73, 94));

        addToCartBtn.addActionListener(e -> addToCart());
        removeFromCartBtn.addActionListener(e -> removeFromCart());
        checkoutBtn.addActionListener(e -> checkout());

        ModernPanel leftPanel = new ModernPanel(new BorderLayout(10, 10));
        JLabel productsLabel = new JLabel("Products");
        productsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productsLabel.setForeground(new Color(52, 73, 94));
        productsLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        leftPanel.add(productsLabel, BorderLayout.NORTH);
        leftPanel.add(productScroll, BorderLayout.CENTER);
        
        ModernPanel addButtonPanel = new ModernPanel(new FlowLayout(FlowLayout.CENTER));
        addButtonPanel.add(addToCartBtn);
        leftPanel.add(addButtonPanel, BorderLayout.SOUTH);

        // --- RIGHT PANEL LAYOUT ---
        ModernPanel cartBtnPanel = new ModernPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cartBtnPanel.add(removeFromCartBtn);
        cartBtnPanel.add(totalLabel);
        cartBtnPanel.add(checkoutBtn);

        ModernPanel rightMainPanel = new ModernPanel(new BorderLayout(10, 10));
        JLabel cartLabel = new JLabel("Cart");
        cartLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cartLabel.setForeground(new Color(52, 73, 94));
        cartLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        rightMainPanel.add(cartLabel, BorderLayout.NORTH);
        rightMainPanel.add(cartScroll, BorderLayout.CENTER);
        rightMainPanel.add(cartBtnPanel, BorderLayout.SOUTH);

        // Payment fields vertically stacked above numpad
        ModernPanel paymentFieldsPanel = new ModernPanel(new GridLayout(2, 2, 10, 10));
        JLabel amountLabel = new JLabel("Amount Received:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        paymentFieldsPanel.add(amountLabel);
        amountReceivedField = new MyTextField();
        paymentFieldsPanel.add(amountReceivedField);

        JLabel changeLabel = new JLabel("Change:");
        changeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        paymentFieldsPanel.add(changeLabel);
        changeField = new MyTextField();
        changeField.setEditable(false);
        paymentFieldsPanel.add(changeField);

        // Numpad setup (with backspace)
        numpadPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        numpadPanel.setOpaque(false);
        String[] buttons = {
            "7", "8", "9", "←",
            "4", "5", "6", "C",
            "1", "2", "3", "",
            "0", ".", "", ""
        };
        for (String text : buttons) {
            if (!text.isEmpty()) {
                Button btn = new Button();
                btn.setText(text);
                btn.setBackground(new Color(108, 117, 125));
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Arial", Font.BOLD, 14));
                btn.addActionListener(e -> handleNumpadInput(text));
                numpadPanel.add(btn);
            } else {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setOpaque(false);
                numpadPanel.add(emptyPanel);
            }
        }

        // Payment section panel: payment fields + numpad stacked vertically
        ModernPanel paymentSectionPanel = new ModernPanel();
        paymentSectionPanel.setLayout(new BoxLayout(paymentSectionPanel, BoxLayout.Y_AXIS));
        paymentFieldsPanel.setMaximumSize(new Dimension(220, 80));
        paymentSectionPanel.add(paymentFieldsPanel);
        paymentSectionPanel.add(Box.createVerticalStrut(15));
        paymentSectionPanel.add(numpadPanel);

        // Align payment section to bottom right
        ModernPanel rightWithNumpadPanel = new ModernPanel(new BorderLayout(10, 10));
        rightWithNumpadPanel.add(rightMainPanel, BorderLayout.CENTER);
        rightWithNumpadPanel.add(paymentSectionPanel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightWithNumpadPanel);
        split.setDividerLocation(0.5); // Use proportional sizing for better responsiveness
        split.setResizeWeight(0.5); // Equal resize weight for both panels
        split.setBorder(null);
        split.setOpaque(false);
        split.setContinuousLayout(true); // Smooth resizing

        add(split, BorderLayout.CENTER);

        // amountReceivedField listener
        amountReceivedField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateChange(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateChange(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateChange(); }
        });

        loadProducts();
    }

    private void handleNumpadInput(String input) {
        if ("C".equals(input)) {
            amountReceivedField.setText("");
        } else if ("←".equals(input)) {
            String current = amountReceivedField.getText();
            if (!current.isEmpty()) {
                amountReceivedField.setText(current.substring(0, current.length() - 1));
            }
        } else {
            amountReceivedField.setText(amountReceivedField.getText() + input);
        }
        updateChange();
    }

    private void updateChange() {
        try {
            double total = 0;
            for (CartItem item : cart) {
                total += item.qty * item.priceAtTimeOfSale; // Use stored price
            }
            double received = Double.parseDouble(amountReceivedField.getText());
            double change = received - total;
            changeField.setText(String.format("%.2f", change));
        } catch (NumberFormatException ex) {
            changeField.setText("");
        }
    }

    private void loadProducts() {
        productModel.setRowCount(0);
        products = productDAO.getAllProducts();
        for (Product p : products) {
            productModel.addRow(new Object[]{p.getProdId(), p.getProdName(), p.getProdPrice(), p.getQuantity()});
        }
    }

    private void addToCart() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        Product p = products.get(row);

        String qtyStr = ModernDialog.showInputDialog(this, "Enter quantity:", "Add to Cart", "1");
        if (qtyStr == null) {
            return;
        }
        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ex) {
            ModernDialog.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (qty <= 0 || qty > p.getQuantity()) {
            ModernDialog.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // If already in cart, update quantity
        for (CartItem item : cart) {
            if (item.product.getProdId() == p.getProdId()) {
                if (item.qty + qty > p.getQuantity()) {
                    ModernDialog.showMessageDialog(this, "Not enough stock!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                item.qty += qty;
                updateCartTable();
                return;
            }
        }
        cart.add(new CartItem(p, qty));
        updateCartTable();
    }

    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        cart.remove(row);
        updateCartTable();
    }

    private void updateCartTable() {
        cartModel.setRowCount(0);
        double total = 0;
        for (CartItem item : cart) {
            double subtotal = item.qty * item.priceAtTimeOfSale; // Use stored price
            cartModel.addRow(new Object[]{
                    item.product.getProdId(),
                    item.product.getProdName(),
                    item.priceAtTimeOfSale, // Show stored price
                    item.qty,
                    subtotal
            });
            total += subtotal;
        }
        totalLabel.setText("Total: Php " + String.format("%.2f", total));
        updateChange();
    }

    private void checkout() {
        if (cart.isEmpty()) {
            ModernDialog.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        double total = 0;
        for (CartItem item : cart) {
            total += item.qty * item.priceAtTimeOfSale; // Use stored price
        }
        double received;
        try {
            received = Double.parseDouble(amountReceivedField.getText());
        } catch (NumberFormatException e) {
            ModernDialog.showMessageDialog(this, "Please enter a valid amount received!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (received < total) {
            ModernDialog.showMessageDialog(this, "Amount received is less than total!", "Insufficient Payment", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = ModernDialog.showConfirmDialog(this,
                "Total: Php " + String.format("%.2f", total)
                        + "\nReceived: Php " + String.format("%.2f", received)
                        + "\nChange: Php " + String.format("%.2f", received - total)
                        + "\n\nConfirm checkout?", "Confirm Checkout");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        for (CartItem item : cart) {
            if (item.qty > item.product.getQuantity()) {
                ModernDialog.showMessageDialog(this, "Not enough stock for " + item.product.getProdName(), "Stock Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // Update inventory
        for (CartItem item : cart) {
            ProductsDAO.updateProductStock(item.product.getProdId(), item.product.getQuantity() - item.qty);
        }

        // Prepare sale items for DAO
        List<SaleItem> saleItems = new ArrayList<>();
        for (CartItem item : cart) {
            saleItems.add(new SaleItem(
                    item.product.getProdId(),
                    item.qty,
                    item.priceAtTimeOfSale // Use stored price, not current product price
            ));
        }

        double change = received - total;
        // Create Sale object with new fields
        Sale sale = new Sale(new java.util.Date(), total, received, change, saleItems);

        // Save sale and sale items in DB
        try {
            saleDAO.saveSale(sale);
        } catch (SQLException e) {
            ModernDialog.showMessageDialog(this, "Failed to record sale: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ModernDialog.showMessageDialog(this, "Checkout successful!\nChange: Php " + String.format("%.2f", change), "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);
        cart.clear();
        updateCartTable();
        loadProducts();
        amountReceivedField.setText("");
        changeField.setText("");
        
        // Notify other panels of product changes (stock updates)
        if (productsPanel != null) {
            productsPanel.refreshAndNotify();
        }
    }

    private static class CartItem {

        Product product;
        int qty;
        double priceAtTimeOfSale; // Store price when added to cart

        CartItem(Product product, int qty) {
            this.product = product;
            this.qty = qty;
            this.priceAtTimeOfSale = product.getProdPrice(); // Capture current price
        }
    }

    @Override
    public void onProductsUpdated() {
        // Refresh the product list when products are updated
        loadProducts();
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
