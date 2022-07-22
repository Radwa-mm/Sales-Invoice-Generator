
package sig.model;

import sig.view.InvoiceJFrame;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class HeaderTable extends AbstractTableModel {

    private ArrayList<InvoiceHeader> invoicesArray;
   
    private String[] columns = {"Invoice Num", "Invoice Date", "Customer Name", "Invoice Total"};
    
    public HeaderTable(ArrayList<InvoiceHeader> invoicesArray) {
        this.invoicesArray = invoicesArray;
    }

    @Override
    public int getRowCount() {
        return invoicesArray.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader inv = invoicesArray.get(rowIndex);
        switch (columnIndex) {
            case 0: 
                return inv.getInvoiceNumber();
            case 1: 
                return InvoiceJFrame.dateFormat.format(inv.getInvoiceDate());
            case 2: 
                return inv.getCustomerName();
            case 3: 
                return inv.getInvoiceTotal();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
}
