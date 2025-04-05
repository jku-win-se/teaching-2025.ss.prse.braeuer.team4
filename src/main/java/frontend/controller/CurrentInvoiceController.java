package frontend.controller;

import backend.logic.InvoiceService;
import backend.model.Invoice;
import backend.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;

public class CurrentInvoiceController {

    public Text textFieldHeader;
    public VBox listContainer;
    public Text textFieldCategory;
    public Text textFieldDate;
    public Text textFieldPrice;
    public Circle statusReimbursementCircle;
    public Circle ButtonEditInvoice;
    public Circle backButton;

    public InvoiceService invoiceService;
    public User currentUser;

    @FXML
    public void goBack(MouseEvent mouseEvent) {
        //close Window? or really go back?
    }

    @FXML
    public void onMouseClickedEditInvoice(MouseEvent mouseEvent) {
        try {
            System.out.println("onMouseClickedEditInvoice");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Code from AI

    @FXML
    public void initialize() {
        if (currentUser == null) return;

        invoiceService = new InvoiceService(currentUser);
        List<Invoice> invoices = invoiceService.getInvoices();

        for (Invoice invoice : invoices) {
            HBox row = createInvoiceRow(invoice);
            listContainer.getChildren().add(row);
        }
    }

    private HBox createInvoiceRow(Invoice invoice) {
        HBox row = new HBox(20);

        Label categoryLabel = new Label(invoice.getCategory().toString());
        Label dateLabel = new Label(invoice.getDate().toString());
        Label amountLabel = new Label("€ " + String.format("%.2f", invoice.getAmount()));
        Label statusLabel = new Label(invoice.getState().toString());

        //row.getChildren().addAll(categoryLabel, dateLabel, amountLabel, statusLabel);
        return row;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
}

