package ijse.theropy_system.dao.custom;

import ijse.theropy_system.dao.SuperDAO;
import ijse.theropy_system.entity.Receptionist;

public interface ReceptionistDAO extends SuperDAO {
    Receptionist findByEmail(String email) throws Exception;
    boolean save(Receptionist receptionist) throws Exception;
}
