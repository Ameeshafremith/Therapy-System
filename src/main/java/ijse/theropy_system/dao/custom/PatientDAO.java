package ijse.theropy_system.dao.custom;

import ijse.theropy_system.dao.CrudDAO;
import ijse.theropy_system.entity.Patient;

import java.util.List;

public interface PatientDAO extends CrudDAO<Patient, String> {
    List<Patient> searchByName(String name) throws Exception;
    List<Patient> findPatientsEnrolledInAllPrograms() throws Exception;
    List<Patient> findPatientsWithPrograms() throws Exception;
}
