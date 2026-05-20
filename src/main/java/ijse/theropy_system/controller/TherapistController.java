package ijse.theropy_system.controller;

import ijse.theropy_system.bo.custom.BOFactory;
import ijse.theropy_system.bo.custom.TherapistBO;
import ijse.theropy_system.bo.custom.ProgramBO;
import ijse.theropy_system.dto.TherapistDTO;
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
import java.util.List;
import java.util.ResourceBundle;

public class TherapistController implements Initializable {

    @FXML private TextField txtTherapistId;
    @FXML private TextField txtTherapistName;
    @FXML private TextField txtSpecialization;
    @FXML private ComboBox<String> cmbProgram;
    @FXML private DatePicker dpSchedule;
    @FXML private TextArea txtNotes;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private TableView<TherapistDTO> tblTherapists;

    private final TherapistBO therapistBO = (TherapistBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.THERAPIST);
    private final ProgramBO programBO = (ProgramBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.PROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProgramComboBox();
        setupTable();
        loadTableData();

        btnSave.setOnAction(e -> saveTherapist());
        btnUpdate.setOnAction(e -> updateTherapist());
        btnDelete.setOnAction(e -> deleteTherapist());

        tblTherapists.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillFields(newVal);
        });
    }

    private void loadProgramComboBox() {
        try {
            List<ProgramDTO> programs = programBO.getAllPrograms();
            cmbProgram.getItems().clear();
            for (ProgramDTO p : programs) {
                cmbProgram.getItems().add(p.getId() + " - " + p.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        TableColumn<TherapistDTO, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<TherapistDTO, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TherapistDTO, String> colSpec = new TableColumn<>("Specialization");
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        TableColumn<TherapistDTO, String> colAvail = new TableColumn<>("Availability");
        colAvail.setCellValueFactory(new PropertyValueFactory<>("availability"));

        TableColumn<TherapistDTO, LocalDate> colDate = new TableColumn<>("Hire Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        tblTherapists.getColumns().clear();
        tblTherapists.getColumns().addAll(colId, colName, colSpec, colAvail, colDate);
    }

    private void loadTableData() {
        try {
            List<TherapistDTO> therapists = therapistBO.getAllTherapists();
            ObservableList<TherapistDTO> list = FXCollections.observableArrayList(therapists);
            tblTherapists.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTherapist() {
        try {
            TherapistDTO dto = new TherapistDTO();
            dto.setId(txtTherapistId.getText().trim());
            dto.setName(txtTherapistName.getText().trim());
            dto.setSpecialization(txtSpecialization.getText().trim());
            dto.setAvailability("Available");
            dto.setEmail(dto.getId().toLowerCase() + "@therapy.lk");
            dto.setPhone("0000000000");
            dto.setHireDate(dpSchedule.getValue() != null ? dpSchedule.getValue() : LocalDate.now());

            String selectedProgram = cmbProgram.getValue();
            if (selectedProgram != null && !selectedProgram.isEmpty()) {
                String programId = selectedProgram.split(" - ")[0];
                dto.setProgramIds(List.of(programId));
            }

            if (therapistBO.saveTherapist(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Therapist saved successfully!");
                clearFields();
                loadTableData();
            }
        } catch (InvalidInputException | RegistrationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save therapist: " + e.getMessage());
        }
    }

    private void updateTherapist() {
        try {
            TherapistDTO dto = new TherapistDTO();
            dto.setId(txtTherapistId.getText().trim());
            dto.setName(txtTherapistName.getText().trim());
            dto.setSpecialization(txtSpecialization.getText().trim());
            dto.setAvailability("Available");
            dto.setEmail(dto.getId().toLowerCase() + "@therapy.lk");
            dto.setPhone("0000000000");
            dto.setHireDate(dpSchedule.getValue() != null ? dpSchedule.getValue() : LocalDate.now());

            String selectedProgram = cmbProgram.getValue();
            if (selectedProgram != null && !selectedProgram.isEmpty()) {
                String programId = selectedProgram.split(" - ")[0];
                dto.setProgramIds(List.of(programId));
            }

            if (therapistBO.updateTherapist(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Therapist updated successfully!");
                clearFields();
                loadTableData();
            }
        } catch (InvalidInputException | RegistrationException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update therapist: " + e.getMessage());
        }
    }

    private void deleteTherapist() {
        try {
            String id = txtTherapistId.getText().trim();
            if (id.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Select a therapist to delete");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setContentText("Are you sure you want to delete this therapist?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (therapistBO.deleteTherapist(id)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Therapist deleted successfully!");
                    clearFields();
                    loadTableData();
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete therapist: " + e.getMessage());
        }
    }

    private void fillFields(TherapistDTO dto) {
        txtTherapistId.setText(dto.getId());
        txtTherapistName.setText(dto.getName());
        txtSpecialization.setText(dto.getSpecialization());
        dpSchedule.setValue(dto.getHireDate());
    }

    private void clearFields() {
        txtTherapistId.clear();
        txtTherapistName.clear();
        txtSpecialization.clear();
        dpSchedule.setValue(null);
        cmbProgram.setValue(null);
        txtNotes.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
