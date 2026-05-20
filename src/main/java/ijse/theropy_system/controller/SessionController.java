package ijse.theropy_system.controller;

import ijse.theropy_system.bo.custom.*;
import ijse.theropy_system.dto.*;
import ijse.theropy_system.exception.SchedulingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class SessionController implements Initializable {

    @FXML private TextField txtSessionId;
    @FXML private ComboBox<String> cmbPatient;
    @FXML private ComboBox<String> cmbProgram;
    @FXML private ComboBox<String> cmbTherapist;
    @FXML private ComboBox<String> cmbTimeSlot;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private DatePicker dpSessionDate;
    @FXML private TextField txtRoom;
    @FXML private TextArea txtNotes;
    @FXML private Button btnBook;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private TableView<SessionDTO> tblSessions;

    private final SessionBO sessionBO = (SessionBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.SESSION);
    private final PatientBO patientBO = (PatientBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.PATIENT);
    private final TherapistBO therapistBO = (TherapistBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.THERAPIST);
    private final ProgramBO programBO = (ProgramBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.PROGRAM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBoxes();
        setupTable();
        loadTableData();

        btnBook.setOnAction(e -> bookSession());
        btnUpdate.setOnAction(e -> updateSession());
        btnDelete.setOnAction(e -> cancelSession());

        tblSessions.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillFields(newVal);
        });
    }

    private void loadComboBoxes() {
        try {
            // Load patients
            List<PatientDTO> patients = patientBO.getAllPatients();
            for (PatientDTO p : patients) {
                cmbPatient.getItems().add(p.getId() + " - " + p.getName());
            }

            // Load therapists
            List<TherapistDTO> therapists = therapistBO.getAllTherapists();
            for (TherapistDTO t : therapists) {
                cmbTherapist.getItems().add(t.getId() + " - " + t.getName());
            }

            // Load programs
            List<ProgramDTO> programs = programBO.getAllPrograms();
            for (ProgramDTO p : programs) {
                cmbProgram.getItems().add(p.getId() + " - " + p.getName());
            }

            // Load time slots
            cmbTimeSlot.getItems().addAll(
                    "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                    "12:00", "13:00", "13:30", "14:00", "14:30", "15:00",
                    "15:30", "16:00", "16:30", "17:00"
            );

            // Load statuses
            cmbStatus.getItems().addAll("SCHEDULED", "COMPLETED", "CANCELLED");
            cmbStatus.setValue("SCHEDULED");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        TableColumn<SessionDTO, Integer> colId = new TableColumn<>("Session ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<SessionDTO, String> colPatient = new TableColumn<>("Patient");
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<SessionDTO, String> colTherapist = new TableColumn<>("Therapist");
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));

        TableColumn<SessionDTO, String> colProgram = new TableColumn<>("Program");
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));

        TableColumn<SessionDTO, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<SessionDTO, LocalTime> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<SessionDTO, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblSessions.getColumns().clear();
        tblSessions.getColumns().addAll(colId, colPatient, colTherapist, colProgram, colDate, colTime, colStatus);
    }

    private void loadTableData() {
        try {
            List<SessionDTO> sessions = sessionBO.getAllSessions();
            ObservableList<SessionDTO> list = FXCollections.observableArrayList(sessions);
            tblSessions.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookSession() {
        try {
            SessionDTO dto = new SessionDTO();
            dto.setPatientId(extractId(cmbPatient.getValue()));
            dto.setTherapistId(extractId(cmbTherapist.getValue()));
            dto.setProgramId(extractId(cmbProgram.getValue()));
            dto.setDate(dpSessionDate.getValue());
            dto.setTime(LocalTime.parse(cmbTimeSlot.getValue()));
            dto.setStatus(cmbStatus.getValue());
            dto.setRoom(txtRoom.getText().trim());
            dto.setNotes(txtNotes.getText().trim());

            if (sessionBO.bookSession(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Session booked successfully!");
                clearFields();
                loadTableData();
            }
        } catch (SchedulingException e) {
            showAlert(Alert.AlertType.ERROR, "Scheduling Conflict", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to book session: " + e.getMessage());
        }
    }

    private void updateSession() {
        try {
            SessionDTO dto = new SessionDTO();
            dto.setId(Integer.parseInt(txtSessionId.getText().trim()));
            dto.setPatientId(extractId(cmbPatient.getValue()));
            dto.setTherapistId(extractId(cmbTherapist.getValue()));
            dto.setProgramId(extractId(cmbProgram.getValue()));
            dto.setDate(dpSessionDate.getValue());
            dto.setTime(LocalTime.parse(cmbTimeSlot.getValue()));
            dto.setStatus(cmbStatus.getValue());
            dto.setRoom(txtRoom.getText().trim());
            dto.setNotes(txtNotes.getText().trim());

            if (sessionBO.updateSession(dto)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Session updated successfully!");
                clearFields();
                loadTableData();
            }
        } catch (SchedulingException e) {
            showAlert(Alert.AlertType.ERROR, "Scheduling Conflict", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update session: " + e.getMessage());
        }
    }

    private void cancelSession() {
        try {
            if (txtSessionId.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Select a session to cancel");
                return;
            }

            int id = Integer.parseInt(txtSessionId.getText().trim());
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Cancellation");
            confirm.setContentText("Are you sure you want to cancel this session?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (sessionBO.cancelSession(id)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Session cancelled successfully!");
                    clearFields();
                    loadTableData();
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to cancel session: " + e.getMessage());
        }
    }

    private void fillFields(SessionDTO dto) {
        txtSessionId.setText(String.valueOf(dto.getId()));
        dpSessionDate.setValue(dto.getDate());
        if (dto.getTime() != null) {
            cmbTimeSlot.setValue(dto.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        cmbStatus.setValue(dto.getStatus());
        txtRoom.setText(dto.getRoom());
        txtNotes.setText(dto.getNotes());

        // Select the correct combo box items
        selectComboBoxItem(cmbPatient, dto.getPatientId(), dto.getPatientName());
        selectComboBoxItem(cmbTherapist, dto.getTherapistId(), dto.getTherapistName());
        selectComboBoxItem(cmbProgram, dto.getProgramId(), dto.getProgramName());
    }

    private void selectComboBoxItem(ComboBox<String> comboBox, String id, String name) {
        if (id != null && name != null) {
            String item = id + " - " + name;
            comboBox.setValue(item);
        }
    }

    private String extractId(String comboBoxValue) {
        if (comboBoxValue == null || comboBoxValue.isEmpty()) return null;
        return comboBoxValue.split(" - ")[0];
    }

    private void clearFields() {
        txtSessionId.clear();
        cmbPatient.setValue(null);
        cmbTherapist.setValue(null);
        cmbProgram.setValue(null);
        cmbTimeSlot.setValue(null);
        cmbStatus.setValue("SCHEDULED");
        dpSessionDate.setValue(null);
        txtRoom.clear();
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
