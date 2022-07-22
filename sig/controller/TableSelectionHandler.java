
package sig.controller;

import sig.model.InvoiceHeader;
import sig.model.InvoiceLine;
import sig.model.LineTable;
import sig.view.InvoiceJFrame;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class TableSelectionHandler implements ListSelectionListener {

    private InvoiceJFrame frame;

    public TableSelectionHandler(InvoiceJFrame frame) {
        this.frame = frame;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedInvIndex = frame.getinvHTable().getSelectedRow();
        System.out.println("Invoice selected: " + selectedInvIndex);
        if (selectedInvIndex != -1) {
            InvoiceHeader selectedInv = frame.getInvoicesArray().get(selectedInvIndex);
            ArrayList<InvoiceLine> lines = selectedInv.getLines();
            LineTable lineTableModel = new LineTable(lines);
            frame.setLinesArray(lines);
            frame.getInvLTable().setModel(lineTableModel);
            //displaying selected invoice data 
            frame.getCustNameLbl().setText(selectedInv.getCustomerName());
            frame.getInvNumLbl().setText("" + selectedInv.getInvoiceNumber());
            frame.getInvTotaLbl().setText("" + selectedInv.getInvoiceTotal());
            frame.getInvDateLbl().setText(InvoiceJFrame.dateFormat.format(selectedInv.getInvoiceDate()));
        }
    }

}
