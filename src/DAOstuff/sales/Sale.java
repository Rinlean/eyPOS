package DAOstuff.sales;

import java.util.Date;
import java.util.List;

public class Sale {

    private Date date;
    private double total;
    private double amountReceived;
    private double changeGiven;
    private List<SaleItem> items;

    public Sale(Date date, double total, double amountReceived, double changeGiven, List<SaleItem> items) {
        this.date = date;
        this.total = total;
        this.amountReceived = amountReceived;
        this.changeGiven = changeGiven;
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public double getChangeGiven() {
        return changeGiven;
    }

    public List<SaleItem> getItems() {
        return items;
    }
}
