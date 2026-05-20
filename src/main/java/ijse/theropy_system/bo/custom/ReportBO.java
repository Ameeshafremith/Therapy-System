package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.dto.PatientDTO;
import ijse.theropy_system.dto.SessionDTO;
import ijse.theropy_system.dto.PaymentDTO;
import ijse.theropy_system.dto.TherapistDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportBO extends SuperBO {
    long getTotalSessions() throws Exception;
    long getTotalPatients() throws Exception;
    long getTotalTherapists() throws Exception;
    long getTotalPrograms() throws Exception;
    TherapistDTO getTopTherapist() throws Exception;
    Map<String, Long> getSessionCountByTherapist() throws Exception;
    List<PaymentDTO> getFinancialReport(LocalDate from, LocalDate to) throws Exception;
    List<PatientDTO> getPatientTherapyHistory(String patientId) throws Exception;
    List<SessionDTO> getTherapistSessionHistory(String therapistId) throws Exception;
}
