package ijse.theropy_system.controller;

import ijse.theropy_system.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {
    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblUser;

    @FXML
    private Button btnPatients;

    @FXML
    private Button btnTherapist;

    @FXML
    private Button btnSession;

    @FXML
    private Button btnProgram;

    @FXML
    private Button btnReports;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String role = HelloApplication.getCurrentUserRole();
        String username = HelloApplication.getCurrentUsername();
        lblUser.setText("Welcome, " + (username != null ? username : role));

        // Role-based access control
        if ("Receptionist".equals(role)) {
            // Receptionist: can manage patients, sessions, reports (financial)
            // Cannot manage therapists and programs (Admin only)
            btnTherapist.setDisable(true);
            btnTherapist.setStyle("-fx-opacity: 0.4; -fx-cursor: default;");
            btnProgram.setDisable(true);
            btnProgram.setStyle("-fx-opacity: 0.4; -fx-cursor: default;");
        }

        try {
            contentArea.getChildren().setAll(HelloApplication.loadFXML("dashBoard"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxml) throws IOException {
        String role = HelloApplication.getCurrentUserRole();

        // Additional role-based check before loading
        if (("therapist".equals(fxml) || "program".equals(fxml)) && "Receptionist".equals(role)) {
            return;
        }

        Parent root = HelloApplication.loadFXML(fxml);
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void btnDashboard(ActionEvent event) throws IOException {
        loadPage("dashBoard");
    }

    @FXML
    void btnPatients(ActionEvent event) throws IOException {
        loadPage("patient");
    }

    @FXML
    void btnProgram(ActionEvent event) throws IOException {
        loadPage("program");
    }

    @FXML
    void btnReports(ActionEvent event) throws IOException {
        loadPage("report");
    }

    @FXML
    void btnSession(ActionEvent event) throws IOException {
        loadPage("session");
    }

    @FXML
    void btnTherapist(ActionEvent event) throws IOException {
        loadPage("therapist");
    }

    @FXML
    void btnLogout(ActionEvent event) {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        HelloApplication.logout(stage);
    }
}
