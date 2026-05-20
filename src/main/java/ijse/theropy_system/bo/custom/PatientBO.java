package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.dto.PatientDTO;

import java.util.List;

public interface PatientBO extends SuperBO {
    boolean savePatient(PatientDTO dto) throws Exception;
    boolean updatePatient(PatientDTO dto) throws Exception;
    boolean deletePatient(String id) throws Exception;
    PatientDTO getPatientById(String id) throws Exception;
    List<PatientDTO> getAllPatients() throws Exception;
    List<PatientDTO> searchPatientsByName(String name) throws Exception;
    List<PatientDTO> getPatientsEnrolledInAllPrograms() throws Exception;
    List<PatientDTO> getPatientsWithPrograms() throws Exception;
}
