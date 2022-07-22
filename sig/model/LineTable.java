
package sig.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class LineTable extends AbstractTableModel {

    private ArrayList<InvoiceLine> linesArray;
    private String[] columns = {"Item Name", "Item Price", "Count", "Line Total"};

    public LineTable(ArrayList<InvoiceLine> linesArray) {
        this.linesArray = linesArray;
    }

    @Override
    public int getRowCount() {
        return linesArray == null ? 0 : linesArray.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (linesArray == null) {
            return "";
        } else {
            InvoiceLine line = linesArray.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return line.getItemName();
                case 1:
                    return line.getItemPrice();
                case 2:
                    return line.getCount();
                case 3:
                    return line.getLineTotal();
                default:
                    return "";
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

}
