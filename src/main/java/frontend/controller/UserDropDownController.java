package frontend.controller;

import backend.logic.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDropDownController {
    @FXML
    private ImageView userImageView;
    @FXML
    public MenuButton userDropDown;

    @FXML
    public void initialize() {
        userImageView.setOnMouseClicked(event -> {
            userDropDown.show();
        });
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.logout();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/views/LoginPage.fxml"));
            Parent root = loader.load();
            Scene loginScene = new Scene(root);

            Stage stage = (Stage) userDropDown.getScene().getWindow();
            stage.setScene(loginScene);
            stage.centerOnScreen(); // Zentriert die neue Szene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
