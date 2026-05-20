package ijse.theropy_system.controller;

import ijse.theropy_system.HelloApplication;
import ijse.theropy_system.bo.custom.BOFactory;
import ijse.theropy_system.bo.custom.ReportBO;
import ijse.theropy_system.dto.PaymentDTO;
import ijse.theropy_system.dto.SessionDTO;
import ijse.theropy_system.dto.TherapistDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    @FXML private Label lblSessions;
    @FXML private Label lblTherapist;
    @FXML private Label lblRevenue;
    @FXML private VBox adminCard;
    @FXML private VBox financeCard;
    @FXML private ComboBox<String> cmbReportType;
    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;
    @FXML private Button btnGenerate;
    @FXML private BarChart<String, Number> reportChart;
    @FXML private TableView<SessionDTO> tblHistory;

    private final ReportBO reportBO = (ReportBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.REPORT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReportTypes();
        applyRoleBasedAccess();
        loadStatistics();
        setupTable();

        btnGenerate.setOnAction(e -> generateReport());
    }

    private void loadReportTypes() {
        cmbReportType.getItems().addAll(
                "Therapist Performance",
                "Financial Report",
                "Session Statistics",
                "Patient History"
        );
    }

    private void applyRoleBasedAccess() {
        String role = HelloApplication.getCurrentUserRole();
        if ("Admin".equals(role)) {
            // Admin sees therapist performance card
            adminCard.setDisable(false);
            financeCard.setDisable(false);
        } else {
            // Receptionist sees financial reports
            adminCard.setDisable(true);
            adminCard.setStyle("-fx-opacity: 0.4;");
            financeCard.setDisable(false);
        }
    }

    private void loadStatistics() {
        try {
            long totalSessions = reportBO.getTotalSessions();
            lblSessions.setText(String.valueOf(totalSessions));

            TherapistDTO topTherapist = reportBO.getTopTherapist();
            lblTherapist.setText(topTherapist != null ? topTherapist.getName() : "N/A");

            // Calculate revenue
            List<PaymentDTO> payments = reportBO.getFinancialReport(
                    LocalDate.now().minusMonths(1), LocalDate.now());
            BigDecimal totalRevenue = payments.stream()
                    .filter(p -> "COMPLETED".equals(p.getStatus()))
                    .map(PaymentDTO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            lblRevenue.setText("Rs. " + totalRevenue);

        } catch (Exception e) {
            lblSessions.setText("0");
            lblTherapist.setText("N/A");
            lblRevenue.setText("Rs. 0");
            e.printStackTrace();
        }
    }

    private void setupTable() {
        TableColumn<SessionDTO, String> colPatientId = new TableColumn<>("Patient ID");
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<SessionDTO, String> colPatientName = new TableColumn<>("Patient Name");
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<SessionDTO, String> colProgram = new TableColumn<>("Program");
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));

        TableColumn<SessionDTO, LocalDate> colDate = new TableColumn<>("Session Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<SessionDTO, String> colTherapist = new TableColumn<>("Therapist");
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));

        tblHistory.getColumns().clear();
        tblHistory.getColumns().addAll(colPatientId, colPatientName, colProgram, colDate, colTherapist);
    }

    private void generateReport() {
        String reportType = cmbReportType.getValue();
        if (reportType == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a report type");
            return;
        }

        try {
            reportChart.getData().clear();

            switch (reportType) {
                case "Therapist Performance":
                    generateTherapistPerformanceReport();
                    break;
                case "Financial Report":
                    generateFinancialReport();
                    break;
                case "Session Statistics":
                    generateSessionStatisticsReport();
                    break;
                case "Patient History":
                    loadPatientHistory();
                    break;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate report: " + e.getMessage());
        }
    }

    private void generateTherapistPerformanceReport() throws Exception {
        Map<String, Long> data = reportBO.getSessionCountByTherapist();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sessions per Therapist");

        for (Map.Entry<String, Long> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        reportChart.getData().add(series);
    }

    private void generateFinancialReport() throws Exception {
        LocalDate from = dpFrom.getValue() != null ? dpFrom.getValue() : LocalDate.now().minusMonths(1);
        LocalDate to = dpTo.getValue() != null ? dpTo.getValue() : LocalDate.now();

        List<PaymentDTO> payments = reportBO.getFinancialReport(from, to);

        long completed = payments.stream().filter(p -> "COMPLETED".equals(p.getStatus())).count();
        long pending = payments.stream().filter(p -> "PENDING".equals(p.getStatus())).count();
        long failed = payments.stream().filter(p -> "FAILED".equals(p.getStatus())).count();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Payment Status");
        series.getData().add(new XYChart.Data<>("Completed", completed));
        series.getData().add(new XYChart.Data<>("Pending", pending));
        series.getData().add(new XYChart.Data<>("Failed", failed));

        reportChart.getData().add(series);
    }

    private void generateSessionStatisticsReport() throws Exception {
        long total = reportBO.getTotalSessions();
        Map<String, Long> therapistData = reportBO.getSessionCountByTherapist();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Session Distribution");
        for (Map.Entry<String, Long> entry : therapistData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        reportChart.getData().add(series);
    }

    private void loadPatientHistory() throws Exception {
        List<SessionDTO> sessions = reportBO.getTherapistSessionHistory("");
        ObservableList<SessionDTO> list = FXCollections.observableArrayList(sessions);
        tblHistory.setItems(list);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
