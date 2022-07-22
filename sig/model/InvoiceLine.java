
package sig.model;

public class InvoiceLine {
    private InvoiceHeader invoiceNumber;
    private String itemName;
    private double itemPrice;
    private int count;
    

    public InvoiceLine() {
    }

    public InvoiceLine(InvoiceHeader invoiceNumber, String itemName, double itemPrice, int count) {
        this.invoiceNumber = invoiceNumber;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
       
    }

    public InvoiceHeader getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(InvoiceHeader invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public double getLineTotal() {
        return itemPrice * count;
    }

    @Override
    public String toString() {
        return invoiceNumber.getInvoiceNumber() + "," + itemName + "," + itemPrice + "," + count;
    }

    
    
}
