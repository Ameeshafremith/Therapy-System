package ijse.theropy_system.bo.custom.impl;

import ijse.theropy_system.bo.custom.LoginBO;
import ijse.theropy_system.dao.custom.AdminDAO;
import ijse.theropy_system.dao.custom.DAOFactory;
import ijse.theropy_system.dao.custom.ReceptionistDAO;
import ijse.theropy_system.dto.LoginDTO;
import ijse.theropy_system.entity.Admin;
import ijse.theropy_system.entity.Receptionist;
import ijse.theropy_system.exception.LoginException;
import ijse.theropy_system.exception.RegistrationException;
import ijse.theropy_system.util.ValidationUtil;
import org.mindrot.jbcrypt.BCrypt;

public class LoginBOImpl implements LoginBO {

    private final AdminDAO adminDAO = (AdminDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.ADMIN);
    private final ReceptionistDAO receptionistDAO = (ReceptionistDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.RECEPTIONIST);

    @Override
    public LoginDTO authenticate(String username, String password, String role) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new LoginException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new LoginException("Password cannot be empty");
        }

        if ("Admin".equals(role)) {
            Admin admin = adminDAO.findByUsername(username);
            if (admin == null) {
                throw new LoginException("Invalid admin credentials");
            }
            if (!verifyPassword(password, admin.getPassword())) {
                throw new LoginException("Invalid admin credentials");
            }
            // Auto-migrate plain text password to BCrypt if needed
            if (!isBCryptHash(admin.getPassword())) {
                admin.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                updateAdminPassword(admin);
            }
            return new LoginDTO(admin.getUsername(), null, "Admin");

        } else if ("Receptionist".equals(role)) {
            Receptionist receptionist = receptionistDAO.findByEmail(username);
            if (receptionist == null) {
                throw new LoginException("Invalid receptionist credentials");
            }
            if (!verifyPassword(password, receptionist.getPassword())) {
                throw new LoginException("Invalid receptionist credentials");
            }
            // Auto-migrate plain text password to BCrypt if needed
            if (!isBCryptHash(receptionist.getPassword())) {
                receptionist.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                updateReceptionistPassword(receptionist);
            }
            return new LoginDTO(receptionist.getEmail(), null, "Receptionist");
        }

        throw new LoginException("Invalid role selected");
    }

    /**
     * Verifies password against stored hash. Supports both BCrypt hashes
     * and plain-text passwords (for backward compatibility with existing data).
     */
    private boolean verifyPassword(String plainPassword, String storedPassword) {
        if (isBCryptHash(storedPassword)) {
            return BCrypt.checkpw(plainPassword, storedPassword);
        } else {
            // Fallback: plain-text comparison for legacy data
            return plainPassword.equals(storedPassword);
        }
    }

    /**
     * Checks if a string looks like a BCrypt hash (starts with $2a$, $2b$, or $2y$).
     */
    private boolean isBCryptHash(String password) {
        return password != null && password.length() > 4
                && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

    private void updateAdminPassword(Admin admin) throws Exception {
        try (var session = ijse.theropy_system.util.HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(admin);
            session.getTransaction().commit();
        }
    }

    private void updateReceptionistPassword(Receptionist receptionist) throws Exception {
        try (var session = ijse.theropy_system.util.HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(receptionist);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean registerAdmin(String username, String password) throws Exception {
        ValidationUtil.validateRequired(username, "Username");
        ValidationUtil.validateRequired(password, "Password");

        Admin existing = adminDAO.findByUsername(username);
        if (existing != null) {
            throw new RegistrationException("Admin username already exists");
        }

        // BCrypt password encryption
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Admin admin = new Admin(username, hashedPassword);
        return adminDAO.save(admin);
    }

    @Override
    public boolean registerReceptionist(String name, String email, String phone, String password) throws Exception {
        ValidationUtil.validateName(name);
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePhone(phone);
        ValidationUtil.validateRequired(password, "Password");

        Receptionist existing = receptionistDAO.findByEmail(email);
        if (existing != null) {
            throw new RegistrationException("Receptionist with this email already exists");
        }

        // BCrypt password encryption
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Receptionist receptionist = new Receptionist(name, email, phone, hashedPassword);
        return receptionistDAO.save(receptionist);
    }
}
