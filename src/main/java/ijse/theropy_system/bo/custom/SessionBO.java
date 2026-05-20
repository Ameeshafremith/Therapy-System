package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.dto.SessionDTO;

import java.time.LocalDate;
import java.util.List;

public interface SessionBO extends SuperBO {
    boolean bookSession(SessionDTO dto) throws Exception;
    boolean updateSession(SessionDTO dto) throws Exception;
    boolean cancelSession(int id) throws Exception;
    SessionDTO getSessionById(int id) throws Exception;
    List<SessionDTO> getAllSessions() throws Exception;
    List<SessionDTO> getSessionsByDate(LocalDate date) throws Exception;
    List<SessionDTO> getSessionsByPatient(String patientId) throws Exception;
    List<SessionDTO> getSessionsByTherapist(String therapistId) throws Exception;
}
