package DAOstuff.products;

public class Product {
    private int prodId;
    private String prodBarCode;
    private String prodName;
    private double prodPrice;
    private double msrp;
    private String prodDesc;
    private int quantity;

    public Product(int prodId, String prodBarCode, String prodName, double prodPrice, double msrp, String prodDesc, int quantity) {
        this.prodId = prodId;
        this.prodBarCode = prodBarCode;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.msrp = msrp;
        this.prodDesc = prodDesc;
        this.quantity = quantity;
    }

    public int getProdId() { return prodId; }
    public String getProdBarCode() { return prodBarCode; }
    public String getProdName() { return prodName; }
    public double getProdPrice() { return prodPrice; }
    public double getMsrp() { return msrp; }
    public String getProdDesc() { return prodDesc; }
    public int getQuantity() { return quantity; }
}