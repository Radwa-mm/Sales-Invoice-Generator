
package sig.controller;

import sig.model.InvoiceHeader;
import sig.model.HeaderTable;
import sig.model.InvoiceLine;
import sig.model.LineTable;
import sig.view.InvoiceJFrame;
import sig.view.NewInvoice;
import sig.view.NewLine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class ActionHandler implements ActionListener {

    private final InvoiceJFrame frame;
    private NewInvoice headerDialog;
    private NewLine lineDialog;

    public ActionHandler(InvoiceJFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //according to value received from action command the required method will run
        switch (e.getActionCommand()) {
            case "Save File":
                saveFiles();
                break;

            case "Load File":
                loadFiles();
                break;

            case "Create New Invoice":
                createNewInvoice();
                break;

            case "Delete Invoice":
                deleteInvoice();
                break;

            case "New Item":
                createNewItem();
                break;

            case "Delete Item":
                deleteItem();
                break;

            case "newInvoiceOK":
                newInvoiceOkButton();
                break;

            case "newInvoiceCancel":
                newInvoiceCancelButton();
                break;

            case "newLineOK":
                newLineOkButton();
                break;
                
            case "newLineCancel":
                newLineCancelButton();
                break;
        }
    }

    
    private void saveFiles() {
        ArrayList<InvoiceHeader> invoicesArray = frame.getInvoicesArray();
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                String headers = "";
                String lines = "";
                for (InvoiceHeader invoice : invoicesArray) {
                    headers += invoice.toString();
                    headers += "\n";
                    for (InvoiceLine line : invoice.getLines()) {
                        lines += line.toString();
                        lines += "\n";
                    }
                }
        
                headers = headers.substring(0, headers.length()-1);
                lines = lines.substring(0, lines.length()-1);
                result = fc.showSaveDialog(frame);
                File lineFile = fc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                hfw.write(headers);
                lfw.write(lines);
                hfw.close();
                lfw.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "An Error Happened", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadFiles() {
        
        try {
            JFileChooser fileChooser = new JFileChooser();
            
            int result = fileChooser.showOpenDialog(frame);
            //if user clicks open
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<>();
                for (String headerLine : headerLines) {
                    //spliting invoice header elements
                    String[] parts = headerLine.split(",");
                    int invid = Integer.parseInt(parts[0]);
                    Date invoiceDate = InvoiceJFrame.dateFormat.parse(parts[1]);
                    InvoiceHeader header = new InvoiceHeader(invid, parts[2], invoiceDate);
                    invoiceHeaders.add(header);
                }
                frame.setInvoicesArray(invoiceHeaders);

                result = fileChooser.showOpenDialog(frame);
                //if user clicks open
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileChooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
                    for (String lineLine : lineLines) {
                        //spliting invoice line elements
                        String[] parts = lineLine.split(",");
                        int invCode = Integer.parseInt(parts[0]);
                        double price = Double.parseDouble(parts[2]);
                        int count = Integer.parseInt(parts[3]);
                        InvoiceHeader inv = frame.getInvObject(invCode);
                        InvoiceLine line = new InvoiceLine(inv, parts[1], price, count);
                        inv.getLines().add(line);
                    }
                }
                HeaderTable headerTableModel = new HeaderTable(invoiceHeaders);
                frame.setHeaderTableModel(headerTableModel);
                frame.getinvHTable().setModel(headerTableModel);
                
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "An Error Happened", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "An Error Happened", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewInvoice() {
        headerDialog = new NewInvoice(frame);
        headerDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedInvoiceIndex = frame.getinvHTable().getSelectedRow();
        if (selectedInvoiceIndex != -1) {
            frame.getInvoicesArray().remove(selectedInvoiceIndex);
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getInvLTable().setModel(new LineTable(null));
            frame.setLinesArray(null);
            frame.getCustNameLbl().setText("");
            frame.getInvNumLbl().setText("");
            frame.getInvTotaLbl().setText("");
            frame.getInvDateLbl().setText("");
        }
    }

    private void createNewItem() {
        lineDialog = new NewLine(frame);
        lineDialog.setVisible(true);
    }

    private void deleteItem() {
        int selectedLineIndex = frame.getInvLTable().getSelectedRow();
        int selectedInvoiceIndex = frame.getinvHTable().getSelectedRow();
        if (selectedLineIndex != -1) {
            frame.getLinesArray().remove(selectedLineIndex);
            LineTable lineTableModel = (LineTable) frame.getInvLTable().getModel();
            lineTableModel.fireTableDataChanged();
            frame.getInvTotaLbl().setText("" + frame.getInvoicesArray().get(selectedInvoiceIndex).getInvoiceTotal());
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getinvHTable().setRowSelectionInterval(selectedInvoiceIndex, selectedInvoiceIndex);
        }
    }

   private void newInvoiceOkButton() {
        headerDialog.setVisible(false);

        String custName = headerDialog.getCustNameField().getText();
        String str = headerDialog.getInvDateField().getText();
        Date d = new Date();
        //handelling date entered by user
        try {
            d = InvoiceJFrame.dateFormat.parse(str);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Date format is not correct, will add today's date.", "An error happened", JOptionPane.ERROR_MESSAGE);
        }

        int invNum = 0;
        for (InvoiceHeader inv : frame.getInvoicesArray()) {
            if (inv.getInvoiceNumber() > invNum) {
                invNum = inv.getInvoiceNumber();
            }
        }
        invNum++;
        InvoiceHeader newInv = new InvoiceHeader(invNum, custName, d);
        frame.getInvoicesArray().add(newInv);
        frame.getHeaderTableModel().fireTableDataChanged();
        headerDialog.dispose();
        headerDialog = null;
    }
   
    private void newInvoiceCancelButton() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }
    
      private void newLineOkButton() {
        lineDialog.setVisible(false);

        String name = lineDialog.getItemNameField().getText();
        String str1 = lineDialog.getItemCountField().getText();
        String str2 = lineDialog.getItemPriceField().getText();
        int count = 1;
        double price = 1;
        //handelling item price and number of items entered by user
        try {
            count = Integer.parseInt(str1);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Count format is not correct!", "An error happened", JOptionPane.ERROR_MESSAGE);
        }

        try {
            price = Double.parseDouble(str2);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Price format is not correct!", "An error happened", JOptionPane.ERROR_MESSAGE);
        }
        int selectedInvHeader = frame.getinvHTable().getSelectedRow();
        if (selectedInvHeader != -1) {
            InvoiceHeader invHeader = frame.getInvoicesArray().get(selectedInvHeader);
            InvoiceLine line = new InvoiceLine(invHeader, name, price, count);
            //invHeader.getLines().add(line);
            frame.getLinesArray().add(line);
            LineTable lineTableModel = (LineTable) frame.getInvLTable().getModel();
            lineTableModel.fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
        }
        frame.getinvHTable().setRowSelectionInterval(selectedInvHeader, selectedInvHeader);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void newLineCancelButton() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

  

}

