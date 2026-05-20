package ijse.theropy_system.dao.custom;

import ijse.theropy_system.dao.SuperDAO;
import ijse.theropy_system.entity.Admin;

public interface AdminDAO extends SuperDAO {
    Admin findByUsername(String username) throws Exception;
    boolean save(Admin admin) throws Exception;
}
