package ijse.theropy_system.controller;

import ijse.theropy_system.HelloApplication;
import ijse.theropy_system.bo.custom.BOFactory;
import ijse.theropy_system.bo.custom.ReportBO;
import ijse.theropy_system.dto.TherapistDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label lblPatients;

    @FXML
    private Label lblDoctors;

    @FXML
    private Label lblAppointments;

    @FXML
    private Label lblSessions;

    private final ReportBO reportBO = (ReportBO) BOFactory.getInstance().getBOFactory(BOFactory.BOTypes.REPORT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            long patientCount = reportBO.getTotalPatients();
            long therapistCount = reportBO.getTotalTherapists();
            long sessionCount = reportBO.getTotalSessions();
            long programCount = reportBO.getTotalPrograms();

            lblPatients.setText(String.valueOf(patientCount));
            lblDoctors.setText(String.valueOf(therapistCount));
            lblAppointments.setText(String.valueOf(sessionCount));
            lblSessions.setText(String.valueOf(programCount));

        } catch (Exception e) {
            // Set defaults if database is not yet populated
            lblPatients.setText("0");
            lblDoctors.setText("0");
            lblAppointments.setText("0");
            lblSessions.setText("0");
            e.printStackTrace();
        }
    }
}
