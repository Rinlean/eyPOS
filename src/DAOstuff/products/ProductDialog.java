package DAOstuff.products;

import componentStuff.ModernPanel;
import componentStuff.MyTextField;
import componentStuff.Button;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProductDialog extends JDialog {
    private MyTextField tfBarcode, tfName, tfPrice, tfMsrp, tfQuantity;
    private JTextArea taDesc;
    private boolean saved = false;
    private Product product;

    // Modern color scheme
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color HEADER_COLOR = new Color(52, 73, 94);
    private static final Color HEADER_TEXT_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    public ProductDialog(Window parent, Product existing) {
        super(parent, existing == null ? "Add Product" : "Edit Product", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(500, 520));
        setBackground(BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header panel
        ModernPanel headerPanel = new ModernPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel(existing == null ? "Add New Product" : "Edit Product");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(HEADER_TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Content panel with modern styling
        ModernPanel contentPanel = new ModernPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(25, 25, 20, 25));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Initialize modern text fields (use columns; MyTextField handles painting)
        tfBarcode = new MyTextField();
        tfBarcode.setColumns(20);
        tfBarcode.setHint("Barcode");

        tfName = new MyTextField();
        tfName.setColumns(20);
        tfName.setHint("Product name");

        tfPrice = new MyTextField();
        tfPrice.setColumns(12);
        tfPrice.setHorizontalAlignment(JTextField.RIGHT);
        tfPrice.setHint("0.00");

        tfMsrp = new MyTextField();
        tfMsrp.setColumns(12);
        tfMsrp.setHorizontalAlignment(JTextField.RIGHT);
        tfMsrp.setHint("0.00");

        tfQuantity = new MyTextField();
        tfQuantity.setColumns(10);
        tfQuantity.setHorizontalAlignment(JTextField.RIGHT);
        tfQuantity.setHint("Qty");

        // Description text area with modern styling
        taDesc = new JTextArea(3, 20);
        taDesc.setLineWrap(true);
        taDesc.setWrapStyleWord(true);
        taDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        taDesc.setBorder(new EmptyBorder(8, 8, 8, 8));
        taDesc.setBackground(new Color(243, 224, 255));
        JScrollPane descScroll = new JScrollPane(taDesc);
        descScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Add components with improved layout
        int row = 0;

        row = addRow(contentPanel, "Barcode:", tfBarcode, row);
        row = addRow(contentPanel, "Product Name:", tfName, row);
        row = addRow(contentPanel, "Selling Price:", tfPrice, row);
        row = addRow(contentPanel, "Cost (MSRP):", tfMsrp, row);
        row = addRow(contentPanel, "Quantity:", tfQuantity, row);

        // Description (allow vertical growth)
        GridBagConstraints cLabel = new GridBagConstraints();
        cLabel.insets = new Insets(8, 0, 8, 15);
        cLabel.anchor = GridBagConstraints.NORTHWEST;
        cLabel.gridx = 0;
        cLabel.gridy = row;
        cLabel.weightx = 0;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(LABEL_FONT);
        contentPanel.add(descLabel, cLabel);

        GridBagConstraints cField = new GridBagConstraints();
        cField.insets = new Insets(8, 0, 8, 15);
        cField.gridx = 1;
        cField.gridy = row;
        cField.weightx = 1.0;
        cField.weighty = 1.0;                 // let it take remaining vertical space
        cField.fill = GridBagConstraints.BOTH; // grow in both directions
        contentPanel.add(descScroll, cField);

        add(contentPanel, BorderLayout.CENTER);

        // Modern button panel
        ModernPanel btnPanel = new ModernPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setBackground(BACKGROUND_COLOR);

        Button saveBtn = new Button();
        saveBtn.setText("Save Product");
        saveBtn.setBackground(new Color(40, 167, 69));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 14));
        saveBtn.setPreferredSize(new Dimension(140, 40));

        Button cancelBtn = new Button();
        cancelBtn.setText("Cancel");
        cancelBtn.setBackground(new Color(108, 117, 125));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(100, 40));

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Populate fields if editing existing product
        if (existing != null) {
            tfBarcode.setText(existing.getProdBarCode());
            tfName.setText(existing.getProdName());
            tfPrice.setText(String.valueOf(existing.getProdPrice()));
            tfMsrp.setText(String.valueOf(existing.getMsrp()));
            taDesc.setText(existing.getProdDesc());
            tfQuantity.setText(String.valueOf(existing.getQuantity()));
            this.product = existing;
        }

        // Action listeners with modern error handling
        saveBtn.addActionListener(e -> {
            try {
                String barcode = tfBarcode.getText().trim();
                String name = tfName.getText().trim();
                String priceText = tfPrice.getText().trim();
                String msrpText = tfMsrp.getText().trim();
                String desc = taDesc.getText().trim();
                String quantityText = tfQuantity.getText().trim();

                if (barcode.isEmpty() || name.isEmpty()) {
                    showErrorDialog("Product barcode and name are required.");
                    return;
                }

                if (priceText.isEmpty() || msrpText.isEmpty() || quantityText.isEmpty()) {
                    showErrorDialog("Price, cost, and quantity are required.");
                    return;
                }

                double price = Double.parseDouble(priceText);
                double msrp = Double.parseDouble(msrpText);
                int quantity = Integer.parseInt(quantityText);

                if (price < 0 || msrp < 0 || quantity < 0) {
                    showErrorDialog("Price, cost, and quantity must be positive numbers.");
                    return;
                }

                if (product == null) {
                    product = new Product(0, barcode, name, price, msrp, desc, quantity);
                } else {
                    product = new Product(product.getProdId(), barcode, name, price, msrp, desc, quantity);
                }
                saved = true;
                dispose();
            } catch (NumberFormatException ex) {
                showErrorDialog("Please enter valid numbers for price, cost, and quantity.");
            } catch (Exception ex) {
                showErrorDialog("Error saving product: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        // Allow Enter key to save
        getRootPane().setDefaultButton(saveBtn);

        // Let layout compute sizes and then center
        pack();
        setLocationRelativeTo(parent);
    }

    // Helper to add one "label + field" row and return the next row index
    private int addRow(JPanel contentPanel, String labelText, JComponent field, int row) {
        GridBagConstraints labelC = new GridBagConstraints();
        labelC.insets = new Insets(8, 0, 8, 15);
        labelC.anchor = GridBagConstraints.WEST;
        labelC.gridx = 0;
        labelC.gridy = row;
        labelC.weightx = 0;
        labelC.fill = GridBagConstraints.NONE;

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(LABEL_FONT);
        contentPanel.add(lbl, labelC);

        GridBagConstraints fieldC = new GridBagConstraints();
        fieldC.insets = new Insets(8, 0, 8, 15);
        fieldC.gridx = 1;
        fieldC.gridy = row;
        fieldC.weightx = 1.0;
        fieldC.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(field, fieldC);

        return row + 1;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() { return saved; }
    public Product getProduct() { return product; }
}