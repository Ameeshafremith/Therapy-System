package ijse.theropy_system.dao.custom;

import ijse.theropy_system.dao.SuperDAO;
import ijse.theropy_system.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory instance;

    public static DAOFactory getInstance() {
        return instance == null ? instance = new DAOFactory() : instance;
    }

    public enum DAOType {
        ADMIN, RECEPTIONIST, PATIENT, THERAPIST, PROGRAM, SESSION, PAYMENT
    }

    public SuperDAO getDAOFactory(DAOType daoType) {
        switch (daoType) {
            case ADMIN:
                return new AdminDAOImpl();
            case RECEPTIONIST:
                return new ReceptionistDAOImpl();
            case PATIENT:
                return new PatientDAOImpl();
            case THERAPIST:
                return new TherapistDAOImpl();
            case PROGRAM:
                return new ProgramDAOImpl();
            case SESSION:
                return new SessionDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            default:
                return null;
        }
    }
}
