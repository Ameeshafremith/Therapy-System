package ijse.theropy_system.controller;

import ijse.theropy_system.bo.custom.BOFactory;
import ijse.theropy_system.bo.custom.LoginBO;
import ijse.theropy_system.dto.LoginDTO;
import ijse.theropy_system.exception.LoginException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ijse.theropy_system.HelloApplication;

import java.io.IOException;

public class LoginController {

    @FXML
    private ToggleButton btnAdmin;

    @FXML
    private ToggleButton btnTherapist;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    private final LoginBO loginBO = (LoginBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.LOGIN);

    private String selectedRole = "Admin";

    @FXML
    public void initialize() {
        btnAdmin.setSelected(true);
        btnAdmin.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white;");

        btnAdmin.setOnAction(event -> {
            selectedRole = "Admin";
            txtUsername.setPromptText("Username");
            btnAdmin.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white;");
            btnTherapist.setStyle(null);
        });

        btnTherapist.setText("Receptionist");
        btnTherapist.setOnAction(event -> {
            selectedRole = "Receptionist";
            txtUsername.setPromptText("Email");
            btnTherapist.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white;");
            btnAdmin.setStyle(null);
        });

        btnLogin.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        try {
            LoginDTO loginDTO = loginBO.authenticate(username, password, selectedRole);

            // Store current user role globally
            HelloApplication.setCurrentUserRole(loginDTO.getRole());
            HelloApplication.setCurrentUsername(
                    "Admin".equals(loginDTO.getRole()) ? loginDTO.getUsername() : username);

            // Navigate to layout
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            HelloApplication.loadLayout(stage);

        } catch (LoginException e) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
