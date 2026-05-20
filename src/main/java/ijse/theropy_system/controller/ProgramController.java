package ijse.theropy_system.controller;

import ijse.theropy_system.bo.custom.BOFactory;
import ijse.theropy_system.bo.custom.ProgramBO;
import ijse.theropy_system.dto.ProgramDTO;
import ijse.theropy_system.exception.InvalidInputException;
import ijse.theropy_system.exception.RegistrationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProgramController implements Initializable {

    @FXML private TextField txtProgramId;
    @FXML private TextField txtProgramName;
    @FXML private TextField txtDuration;
    @FXML private TextField txtCost;
    @FXML private TextArea txtDescription;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private TableView<ProgramDTO> tblPrograms;

    private final ProgramBO programBO = (ProgramBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.PROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadTableData();

        btnSave.setOnAction(e -> saveProgram());
        btnUpdate.setOnAction(e -> updateProgram());
        btnDelete.setOnAction(e -> deleteProgram());

        tblPrograms.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillFields(newVal);
        });
    }

    private void setupTable() {
        TableColumn<ProgramDTO, String> colId = new TableColumn<>("Program ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ProgramDTO, String> colName = new TableColumn<>("Program Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ProgramDTO, Integer> colDuration = new TableColumn<>("Duration (weeks)");
        colDuration.setCellValueFactory(new PropertyValueFactory<>("durationWeeks"));

        TableColumn<ProgramDTO, BigDecimal> colCost = new TableColumn<>("Cost");
        colCost.setCellValueFactory(new PropertyValueFactory<>("fee"));

        TableColumn<ProgramDTO, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        tblPrograms.getColumns().clear();
        tblPrograms.getColumns().addAll(colId, colName, colDuration, colCost, colDesc);
    }

    private void loadTableData() {
        try {
            List<ProgramDTO> programs = programBO.getAllPrograms();
            ObservableList<ProgramDTO> list = FXCollections.observableArrayList(programs);
            tblPrograms.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveProgram() {
        try {
            ProgramDTO dto = new ProgramDTO();
            dto.setId(txtProgramId.getText().trim());
            dto.setName(txtProgramName.getText().trim());
            dto.setDurationWeeks(Integer.parseInt(txtDuration.getText().trim()));
            dto.setFee(new BigDecimal(txtCost.getText().trim()));
            dto.setDescription(txtDescription.getText().trim());

            if (programBO.saveProgram(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Program saved successfully!");
                clearFields();
                loadTableData();
            }
        } catch (InvalidInputException | RegistrationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format for duration or cost");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save program: " + e.getMessage());
        }
    }

    private void updateProgram() {
        try {
            ProgramDTO dto = new ProgramDTO();
            dto.setId(txtProgramId.getText().trim());
            dto.setName(txtProgramName.getText().trim());
            dto.setDurationWeeks(Integer.parseInt(txtDuration.getText().trim()));
            dto.setFee(new BigDecimal(txtCost.getText().trim()));
            dto.setDescription(txtDescription.getText().trim());

            if (programBO.updateProgram(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Program updated successfully!");
                clearFields();
                loadTableData();
            }
        } catch (InvalidInputException | RegistrationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format for duration or cost");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update program: " + e.getMessage());
        }
    }

    private void deleteProgram() {
        try {
            String id = txtProgramId.getText().trim();
            if (id.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Select a program to delete");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setContentText("Are you sure you want to delete this program?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (programBO.deleteProgram(id)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Program deleted successfully!");
                    clearFields();
                    loadTableData();
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete program: " + e.getMessage());
        }
    }

    private void fillFields(ProgramDTO dto) {
        txtProgramId.setText(dto.getId());
        txtProgramName.setText(dto.getName());
        txtDuration.setText(String.valueOf(dto.getDurationWeeks()));
        txtCost.setText(dto.getFee().toString());
        txtDescription.setText(dto.getDescription());
    }

    private void clearFields() {
        txtProgramId.clear();
        txtProgramName.clear();
        txtDuration.clear();
        txtCost.clear();
        txtDescription.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
