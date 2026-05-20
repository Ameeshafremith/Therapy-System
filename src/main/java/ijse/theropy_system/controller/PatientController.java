package ijse.theropy_system.controller;

import ijse.theropy_system.bo.custom.BOFactory;
import ijse.theropy_system.bo.custom.PatientBO;
import ijse.theropy_system.bo.custom.ProgramBO;
import ijse.theropy_system.dto.PatientDTO;
import ijse.theropy_system.dto.ProgramDTO;
import ijse.theropy_system.exception.InvalidInputException;
import ijse.theropy_system.exception.RegistrationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtAge;
    @FXML private TextField txtContact;
    @FXML private TextArea txtAddress;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private TableView<PatientDTO> tblPatients;

    private final PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.PATIENT);
    private final ProgramBO programBO = (ProgramBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.PROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadTableData();

        btnSave.setOnAction(e -> savePatient());
        btnUpdate.setOnAction(e -> updatePatient());
        btnDelete.setOnAction(e -> deletePatient());

        tblPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillFields(newVal);
        });
    }

    private void setupTable() {
        TableColumn<PatientDTO, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PatientDTO, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientDTO, String> colContact = new TableColumn<>("Contact");
        colContact.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<PatientDTO, String> colAddress = new TableColumn<>("Address");
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<PatientDTO, LocalDate> colDate = new TableColumn<>("Registered Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        tblPatients.getColumns().clear();
        tblPatients.getColumns().addAll(colId, colName, colContact, colAddress, colDate);
    }

    private void loadTableData() {
        try {
            List<PatientDTO> patients = patientBO.getAllPatients();
            ObservableList<PatientDTO> list = FXCollections.observableArrayList(patients);
            tblPatients.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePatient() {
        try {
            PatientDTO dto = new PatientDTO();
            dto.setId(txtId.getText().trim());
            dto.setName(txtName.getText().trim());
            dto.setPhone(txtContact.getText().trim());
            dto.setAddress(txtAddress.getText().trim());
            dto.setEmail(txtId.getText().trim().toLowerCase() + "@therapy.lk"); // auto-generate email
            dto.setDateOfBirth(LocalDate.now().minusYears(Integer.parseInt(txtAge.getText().trim())));
            dto.setRegistrationDate(LocalDate.now());

            if (patientBO.savePatient(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient saved successfully!");
                clearFields();
                loadTableData();
            }
        } catch (InvalidInputException | RegistrationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save patient: " + e.getMessage());
        }
    }

    private void updatePatient() {
        try {
            PatientDTO dto = new PatientDTO();
            dto.setId(txtId.getText().trim());
            dto.setName(txtName.getText().trim());
            dto.setPhone(txtContact.getText().trim());
            dto.setAddress(txtAddress.getText().trim());
            dto.setDateOfBirth(LocalDate.now().minusYears(Integer.parseInt(txtAge.getText().trim())));

            if (patientBO.updatePatient(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient updated successfully!");
                clearFields();
                loadTableData();
            }
        } catch (InvalidInputException | RegistrationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update patient: " + e.getMessage());
        }
    }

    private void deletePatient() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Select a patient to delete");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setContentText("Are you sure you want to delete this patient?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (patientBO.deletePatient(id)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Patient deleted successfully!");
                    clearFields();
                    loadTableData();
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete patient: " + e.getMessage());
        }
    }

    private void fillFields(PatientDTO dto) {
        txtId.setText(dto.getId());
        txtName.setText(dto.getName());
        txtContact.setText(dto.getPhone());
        txtAddress.setText(dto.getAddress());
        if (dto.getDateOfBirth() != null) {
            txtAge.setText(String.valueOf(LocalDate.now().getYear() - dto.getDateOfBirth().getYear()));
        }
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtAge.clear();
        txtContact.clear();
        txtAddress.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
