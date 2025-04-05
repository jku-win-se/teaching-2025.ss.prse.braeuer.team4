package frontend.controller;
import java.io.IOException;

import backend.model.Admin;
import backend.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AdminDashboardController {

    public Rectangle openCurrentInvoicesButton;

    public Rectangle openInvoiceUploadButton;
    private User currentUser;

    @FXML
    void onklickOpenInvoiceSubmissionWindow(MouseEvent event) { //created by AI
    	try {
            // Lade die Upload.fxml Datei
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontend/views/InvoiceUpload.fxml"));
            Parent root = fxmlLoader.load();

            // Neue Szene erstellen
            Stage stage = new Stage();
            stage.setTitle("Upload Window");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onMouseClickedOpenInvoicesButton(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontend/views/CurrentInvoices.fxml"));
            Parent root = fxmlLoader.load();
            CurrentInvoiceController currentInvoiceController = fxmlLoader.getController();
            currentInvoiceController.setUser(this.currentUser);
            Stage stage = new Stage();
            stage.setTitle("Current Invoices");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
