package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.dto.PaymentDTO;

import java.time.LocalDate;
import java.util.List;

public interface PaymentBO extends SuperBO {
    boolean processPayment(PaymentDTO dto) throws Exception;
    boolean updatePayment(PaymentDTO dto) throws Exception;
    PaymentDTO getPaymentById(int id) throws Exception;
    List<PaymentDTO> getAllPayments() throws Exception;
    List<PaymentDTO> getPaymentsByPatient(String patientId) throws Exception;
    List<PaymentDTO> getPaymentsByDateRange(LocalDate from, LocalDate to) throws Exception;
    List<PaymentDTO> getPaymentsByStatus(String status) throws Exception;
    String generateInvoice(PaymentDTO dto) throws Exception;
}
