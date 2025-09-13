package DAOstuff.products;

import javax.swing.*;
import java.awt.*;

public class ProductDialog extends JDialog {
    private JTextField tfBarcode, tfName, tfPrice, tfMsrp, tfQuantity;
    private JTextArea taDesc;
    private boolean saved = false;
    private Product product;

    public ProductDialog(Window parent, Product existing) {
        super(parent, "Product Details", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(parent);

        tfBarcode = new JTextField(20);
        tfName = new JTextField(20);
        tfPrice = new JTextField(20);
        tfMsrp = new JTextField(20);
        taDesc = new JTextArea(3, 20);
        tfQuantity = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Barcode:")); panel.add(tfBarcode);
        panel.add(new JLabel("Name:")); panel.add(tfName);
        panel.add(new JLabel("Price:")); panel.add(tfPrice);
        panel.add(new JLabel("MSRP (Cost):")); panel.add(tfMsrp);
        panel.add(new JLabel("Description:")); panel.add(new JScrollPane(taDesc));
        panel.add(new JLabel("Quantity:")); panel.add(tfQuantity);
        add(panel, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn); btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        if (existing != null) {
            tfBarcode.setText(existing.getProdBarCode());
            tfName.setText(existing.getProdName());
            tfPrice.setText(String.valueOf(existing.getProdPrice()));
            tfMsrp.setText(String.valueOf(existing.getMsrp()));
            taDesc.setText(existing.getProdDesc());
            tfQuantity.setText(String.valueOf(existing.getQuantity()));
            this.product = existing;
        }

        saveBtn.addActionListener(e -> {
            try {
                String barcode = tfBarcode.getText();
                String name = tfName.getText();
                double price = Double.parseDouble(tfPrice.getText());
                double msrp = Double.parseDouble(tfMsrp.getText());
                String desc = taDesc.getText();
                int quantity = Integer.parseInt(tfQuantity.getText());
                if (barcode.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Barcode/Name required.");
                    return;
                }
                if (product == null) {
                    product = new Product(0, barcode, name, price, msrp, desc, quantity);
                } else {
                    product = new Product(product.getProdId(), barcode, name, price, msrp, desc, quantity);
                }
                saved = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
        cancelBtn.addActionListener(e -> dispose());
    }

    public boolean isSaved() { return saved; }
    public Product getProduct() { return product; }
}