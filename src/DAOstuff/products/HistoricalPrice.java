package DAOstuff.products;

import java.util.Date;

public class HistoricalPrice {
    private int historyId;
    private int productId;
    private double oldPrice;
    private double newPrice;
    private Date changeDate;
    private String changedBy;

    public HistoricalPrice(int productId, double oldPrice, double newPrice, String changedBy) {
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.changedBy = changedBy;
        this.changeDate = new Date();
    }

    public HistoricalPrice(int historyId, int productId, double oldPrice, double newPrice, Date changeDate, String changedBy) {
        this.historyId = historyId;
        this.productId = productId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.changeDate = changeDate;
        this.changedBy = changedBy;
    }

    // Getters
    public int getHistoryId() { return historyId; }
    public int getProductId() { return productId; }
    public double getOldPrice() { return oldPrice; }
    public double getNewPrice() { return newPrice; }
    public Date getChangeDate() { return changeDate; }
    public String getChangedBy() { return changedBy; }
}