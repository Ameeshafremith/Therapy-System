package ijse.theropy_system.bo.custom.impl;

import ijse.theropy_system.bo.custom.PaymentBO;
import ijse.theropy_system.dao.custom.DAOFactory;
import ijse.theropy_system.dao.custom.PatientDAO;
import ijse.theropy_system.dao.custom.PaymentDAO;
import ijse.theropy_system.dao.custom.ProgramDAO;
import ijse.theropy_system.dao.custom.SessionDAO;
import ijse.theropy_system.dto.PaymentDTO;
import ijse.theropy_system.entity.Patient;
import ijse.theropy_system.entity.Payment;
import ijse.theropy_system.entity.TheropyPrograms;
import ijse.theropy_system.entity.TherapySessions;
import ijse.theropy_system.exception.PaymentException;
import ijse.theropy_system.util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PAYMENT);
    private final PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PATIENT);
    private final ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PROGRAM);
    private final SessionDAO sessionDAO = (SessionDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.SESSION);

    @Override
    public boolean processPayment(PaymentDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getPatientId(), "Patient");
        ValidationUtil.validateRequired(dto.getProgramId(), "Program");
        ValidationUtil.validatePositiveNumber(dto.getAmount(), "Amount");
        ValidationUtil.validateRequired(dto.getPaymentMethod(), "Payment Method");

        Patient patient = patientDAO.findById(dto.getPatientId());
        if (patient == null) {
            throw new PaymentException("Patient not found");
        }
        TheropyPrograms program = programDAO.findById(dto.getProgramId());
        if (program == null) {
            throw new PaymentException("Program not found");
        }

        TherapySessions session = null;
        if (dto.getSessionId() > 0) {
            session = sessionDAO.findById(dto.getSessionId());
        }

        String invoiceNumber = paymentDAO.generateInvoiceNumber();

        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
        payment.setStatus(Payment.PaymentStatus.valueOf(dto.getStatus()));
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setInvoiceNumber(invoiceNumber);
        payment.setPatient(patient);
        payment.setProgram(program);
        payment.setSession(session);

        return paymentDAO.save(payment);
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) throws Exception {
        Payment payment = paymentDAO.findById(dto.getId());
        if (payment == null) {
            throw new PaymentException("Payment not found");
        }

        payment.setAmount(dto.getAmount());
        payment.setDate(dto.getDate());
        payment.setStatus(Payment.PaymentStatus.valueOf(dto.getStatus()));
        payment.setPaymentMethod(dto.getPaymentMethod());

        return paymentDAO.update(payment);
    }

    @Override
    public PaymentDTO getPaymentById(int id) throws Exception {
        Payment payment = paymentDAO.findById(id);
        return payment != null ? convertToDTO(payment) : null;
    }

    @Override
    public List<PaymentDTO> getAllPayments() throws Exception {
        List<Payment> payments = paymentDAO.findAll();
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment p : payments) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    @Override
    public List<PaymentDTO> getPaymentsByPatient(String patientId) throws Exception {
        List<Payment> payments = paymentDAO.findByPatientId(patientId);
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment p : payments) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    @Override
    public List<PaymentDTO> getPaymentsByDateRange(LocalDate from, LocalDate to) throws Exception {
        List<Payment> payments = paymentDAO.findByDateRange(from, to);
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment p : payments) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    @Override
    public List<PaymentDTO> getPaymentsByStatus(String status) throws Exception {
        List<Payment> payments = paymentDAO.findByStatus(status);
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment p : payments) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    @Override
    public String generateInvoice(PaymentDTO dto) throws Exception {
        Payment payment = paymentDAO.findById(dto.getId());
        if (payment == null) {
            throw new PaymentException("Payment not found");
        }
        if (payment.getInvoiceNumber() == null) {
            String invoiceNumber = paymentDAO.generateInvoiceNumber();
            payment.setInvoiceNumber(invoiceNumber);
            paymentDAO.update(payment);
        }
        return payment.getInvoiceNumber();
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setDate(payment.getDate());
        dto.setStatus(payment.getStatus().name());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setInvoiceNumber(payment.getInvoiceNumber());
        if (payment.getPatient() != null) {
            dto.setPatientId(payment.getPatient().getId());
            dto.setPatientName(payment.getPatient().getName());
        }
        if (payment.getProgram() != null) {
            dto.setProgramId(payment.getProgram().getId());
            dto.setProgramName(payment.getProgram().getName());
        }
        if (payment.getSession() != null) {
            dto.setSessionId(payment.getSession().getId());
        }
        return dto;
    }
}
