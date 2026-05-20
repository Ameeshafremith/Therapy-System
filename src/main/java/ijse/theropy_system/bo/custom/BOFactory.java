package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory instance;

    private BOFactory() {}

    public static BOFactory getInstance() {
        return instance == null ? instance = new BOFactory() : instance;
    }

    public enum BOTypes {
        LOGIN, PATIENT, THERAPIST, PROGRAM, SESSION, PAYMENT, REPORT
    }

    public SuperBO getBOFactory(BOTypes boTypes) {
        switch (boTypes) {
            case LOGIN:
                return new LoginBOImpl();
            case PATIENT:
                return new PatientBOImpl();
            case THERAPIST:
                return new TherapistBOImpl();
            case PROGRAM:
                return new ProgramBOImpl();
            case SESSION:
                return new SessionBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case REPORT:
                return new ReportBOImpl();
            default:
                return null;
        }
    }
}
