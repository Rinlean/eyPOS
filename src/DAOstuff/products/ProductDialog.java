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
        setSize(500, 550);
        setLocationRelativeTo(parent);
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
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize modern text fields
        tfBarcode = new MyTextField();
        tfBarcode.setPreferredSize(new Dimension(250, 35));
        tfName = new MyTextField();
        tfName.setPreferredSize(new Dimension(250, 35));
        tfPrice = new MyTextField();
        tfPrice.setPreferredSize(new Dimension(250, 35));
        tfMsrp = new MyTextField();
        tfMsrp.setPreferredSize(new Dimension(250, 35));
        tfQuantity = new MyTextField();
        tfQuantity.setPreferredSize(new Dimension(250, 35));
        
        // Description text area with modern styling
        taDesc = new JTextArea(3, 20);
        taDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        taDesc.setBorder(new EmptyBorder(8, 8, 8, 8));
        taDesc.setBackground(new Color(243, 224, 255));
        JScrollPane descScroll = new JScrollPane(taDesc);
        descScroll.setPreferredSize(new Dimension(250, 80));
        descScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Add components with improved layout
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        JLabel barcodeLabel = new JLabel("Barcode:");
        barcodeLabel.setFont(LABEL_FONT);
        contentPanel.add(barcodeLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(tfBarcode, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(LABEL_FONT);
        contentPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(tfName, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel priceLabel = new JLabel("Selling Price:");
        priceLabel.setFont(LABEL_FONT);
        contentPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(tfPrice, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel msrpLabel = new JLabel("Cost (MSRP):");
        msrpLabel.setFont(LABEL_FONT);
        contentPanel.add(msrpLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(tfMsrp, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(LABEL_FONT);
        contentPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(tfQuantity, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(LABEL_FONT);
        contentPanel.add(descLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(descScroll, gbc);
        
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
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() { return saved; }
    public Product getProduct() { return product; }
}