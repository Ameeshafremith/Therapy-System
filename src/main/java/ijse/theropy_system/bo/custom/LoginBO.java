package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.dto.LoginDTO;

public interface LoginBO extends SuperBO {
    LoginDTO authenticate(String username, String password, String role) throws Exception;
    boolean registerAdmin(String username, String password) throws Exception;
    boolean registerReceptionist(String name, String email, String phone, String password) throws Exception;
}
