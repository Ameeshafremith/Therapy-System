package ijse.theropy_system.dao.custom;

import ijse.theropy_system.dao.CrudDAO;
import ijse.theropy_system.entity.TherapySessions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SessionDAO extends CrudDAO<TherapySessions, Integer> {
    List<TherapySessions> findByDate(LocalDate date) throws Exception;
    List<TherapySessions> findByPatientId(String patientId) throws Exception;
    List<TherapySessions> findByTherapistId(String therapistId) throws Exception;
    boolean hasSchedulingConflict(String therapistId, LocalDate date, LocalTime time) throws Exception;
}
