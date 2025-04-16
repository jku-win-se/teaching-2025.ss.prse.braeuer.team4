package frontend.controller;
import java.io.IOException;

import backend.Exceptions.AuthenticationException;
import backend.logic.SessionManager;
import backend.model.User;
import backend.model.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class AdminDashboardController {

    @FXML
    private Text userNameText;
    @FXML
    MenuButton userDropDown;
    @FXML
    private UserDropDownController userDropDownController;

    @FXML
    void initialize() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/views/UserDropDown.fxml"));
        try {
            userDropDown = loader.load();
            userDropDownController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        User user = SessionManager.getCurrentUser();
        if (user != null) {
            userNameText.setText("Hallo, " + user.getName() + "!");
        } else {
            userNameText.setText("Nicht eingeloggt");
        }
    }

    @FXML
    void onklickOpenInvoiceSubmissionWindow(MouseEvent event) { //created by AI
    	try {
            // Lade die Upload.fxml Datei
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontend/views/InvoiceUpload.fxml"));
            Parent root = fxmlLoader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Neue Szene erstellen
            
            stage.setTitle("Upload Window");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickOpenModifyReimbursementWindow(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontend/views/ModifyReimbursement.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

            stage.setTitle("Modify Reimbursement");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void openCurrentReimbursement(MouseEvent event) {
    	try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/frontend/views/currReimbursements.fxml"));
            Parent root = fxmlLoader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Current Reimbursements");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
