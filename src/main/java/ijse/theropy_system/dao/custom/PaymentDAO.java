package ijse.theropy_system.dao.custom;

import ijse.theropy_system.dao.CrudDAO;
import ijse.theropy_system.entity.Payment;

import java.time.LocalDate;
import java.util.List;

public interface PaymentDAO extends CrudDAO<Payment, Integer> {
    List<Payment> findByPatientId(String patientId) throws Exception;
    List<Payment> findByDateRange(LocalDate from, LocalDate to) throws Exception;
    List<Payment> findByStatus(String status) throws Exception;
    String generateInvoiceNumber() throws Exception;
}
