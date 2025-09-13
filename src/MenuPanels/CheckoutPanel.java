package MenuPanels;

import DAOstuff.products.Product;
import DAOstuff.products.ProductsDAO;
import DAOstuff.sales.Sale;
import DAOstuff.sales.SaleItem;
import DAOstuff.sales.SaleDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class CheckoutPanel extends JPanel {

    private JTable productTable;
    private JTable cartTable;
    private DefaultTableModel productModel;
    private DefaultTableModel cartModel;
    private JLabel totalLabel;
    private List<Product> products;
    private List<CartItem> cart = new ArrayList<>();
    private ProductsDAO productDAO = new ProductsDAO();
    private SaleDAO saleDAO = new SaleDAO();

    // Payment and numpad fields
    private JTextField amountReceivedField;
    private JTextField changeField;
    private JPanel numpadPanel;

    public CheckoutPanel() {
        setLayout(new BorderLayout());

        // Product Table
        productModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Stock"}, 0);
        productTable = new JTable(productModel);
        JScrollPane productScroll = new JScrollPane(productTable);

        // Cart Table
        cartModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Qty", "Subtotal"}, 0);
        cartTable = new JTable(cartModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);

        // Buttons and total
        JButton addToCartBtn = new JButton("Add to Cart");
        JButton removeFromCartBtn = new JButton("Remove Selected");
        JButton checkoutBtn = new JButton("Check Out");
        totalLabel = new JLabel("Total: 0.00");

        addToCartBtn.addActionListener(e -> addToCart());
        removeFromCartBtn.addActionListener(e -> removeFromCart());
        checkoutBtn.addActionListener(e -> checkout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Products"), BorderLayout.NORTH);
        leftPanel.add(productScroll, BorderLayout.CENTER);
        leftPanel.add(addToCartBtn, BorderLayout.SOUTH);

        // --- RIGHT PANEL LAYOUT ---
        JPanel cartBtnPanel = new JPanel();
        cartBtnPanel.add(removeFromCartBtn);
        cartBtnPanel.add(totalLabel);
        cartBtnPanel.add(checkoutBtn);

        JPanel rightMainPanel = new JPanel(new BorderLayout());
        rightMainPanel.add(new JLabel("Cart"), BorderLayout.NORTH);
        rightMainPanel.add(cartScroll, BorderLayout.CENTER);
        rightMainPanel.add(cartBtnPanel, BorderLayout.SOUTH);

        // Payment fields vertically stacked above numpad
        JPanel paymentFieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        paymentFieldsPanel.add(new JLabel("Amount Received:"));
        amountReceivedField = new JTextField();
        paymentFieldsPanel.add(amountReceivedField);

        paymentFieldsPanel.add(new JLabel("Change:"));
        changeField = new JTextField();
        changeField.setEditable(false);
        paymentFieldsPanel.add(changeField);

        // Numpad setup (with backspace)
        numpadPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] buttons = {
            "7", "8", "9", "←",
            "4", "5", "6", "C",
            "1", "2", "3", "",
            "0", ".", "", ""
        };
        for (String text : buttons) {
            JButton btn = new JButton(text);
            if (!text.isEmpty()) {
                btn.addActionListener(e -> handleNumpadInput(text));
            } else {
                btn.setEnabled(false);
            }
            numpadPanel.add(btn);
        }

        // Payment section panel: payment fields + numpad stacked vertically
        JPanel paymentSectionPanel = new JPanel();
        paymentSectionPanel.setLayout(new BoxLayout(paymentSectionPanel, BoxLayout.Y_AXIS));
        paymentFieldsPanel.setMaximumSize(new Dimension(220, 50));
        paymentSectionPanel.add(paymentFieldsPanel);
        paymentSectionPanel.add(Box.createVerticalStrut(10));
        paymentSectionPanel.add(numpadPanel);

        // Align payment section to bottom right
        JPanel rightWithNumpadPanel = new JPanel(new BorderLayout());
        rightWithNumpadPanel.add(rightMainPanel, BorderLayout.CENTER);
        rightWithNumpadPanel.add(paymentSectionPanel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightWithNumpadPanel);
        split.setDividerLocation(400);

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
                total += item.qty * item.product.getProdPrice();
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

        String qtyStr = JOptionPane.showInputDialog(this, "Quantity:", "1");
        if (qtyStr == null) {
            return;
        }
        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
            return;
        }
        if (qty <= 0 || qty > p.getQuantity()) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
            return;
        }
        // If already in cart, update quantity
        for (CartItem item : cart) {
            if (item.product.getProdId() == p.getProdId()) {
                if (item.qty + qty > p.getQuantity()) {
                    JOptionPane.showMessageDialog(this, "Not enough stock!");
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
            double subtotal = item.qty * item.product.getProdPrice();
            cartModel.addRow(new Object[]{
                    item.product.getProdId(),
                    item.product.getProdName(),
                    item.product.getProdPrice(),
                    item.qty,
                    subtotal
            });
            total += subtotal;
        }
        totalLabel.setText("Total: " + String.format("%.2f", total));
        updateChange();
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        double total = 0;
        for (CartItem item : cart) {
            total += item.qty * item.product.getProdPrice();
        }
        double received;
        try {
            received = Double.parseDouble(amountReceivedField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount received!");
            return;
        }
        if (received < total) {
            JOptionPane.showMessageDialog(this, "Amount received is less than total!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Total: " + String.format("%.2f", total)
                        + "\nReceived: " + String.format("%.2f", received)
                        + "\nChange: " + String.format("%.2f", received - total)
                        + "\n\nConfirm checkout?", "Checkout", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        for (CartItem item : cart) {
            if (item.qty > item.product.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Not enough stock for " + item.product.getProdName());
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
                    item.product.getProdPrice()
            ));
        }

        double change = received - total;
        // Create Sale object with new fields
        Sale sale = new Sale(new java.util.Date(), total, received, change, saleItems);

        // Save sale and sale items in DB
        try {
            saleDAO.saveSale(sale);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to record sale: " + e.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(this, "Checkout successful!\nChange: " + String.format("%.2f", change));
        cart.clear();
        updateCartTable();
        loadProducts();
        amountReceivedField.setText("");
        changeField.setText("");
    }

    private static class CartItem {

        Product product;
        int qty;

        CartItem(Product product, int qty) {
            this.product = product;
            this.qty = qty;
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
