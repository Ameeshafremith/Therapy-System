package ijse.theropy_system.dao.custom;

import ijse.theropy_system.dao.CrudDAO;
import ijse.theropy_system.entity.Therapist;

import java.util.List;

public interface TherapistDAO extends CrudDAO<Therapist, String> {
    List<Therapist> findByAvailability(String availability) throws Exception;
    List<Therapist> findByProgramId(String programId) throws Exception;
}
