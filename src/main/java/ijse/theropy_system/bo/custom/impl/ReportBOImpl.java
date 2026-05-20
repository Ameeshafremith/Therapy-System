package ijse.theropy_system.bo.custom.impl;

import ijse.theropy_system.bo.custom.ReportBO;
import ijse.theropy_system.dao.custom.DAOFactory;
import ijse.theropy_system.dao.custom.PatientDAO;
import ijse.theropy_system.dao.custom.PaymentDAO;
import ijse.theropy_system.dao.custom.ProgramDAO;
import ijse.theropy_system.dao.custom.SessionDAO;
import ijse.theropy_system.dao.custom.TherapistDAO;
import ijse.theropy_system.dto.PatientDTO;
import ijse.theropy_system.dto.PaymentDTO;
import ijse.theropy_system.dto.SessionDTO;
import ijse.theropy_system.dto.TherapistDTO;
import ijse.theropy_system.entity.Patient;
import ijse.theropy_system.entity.Payment;
import ijse.theropy_system.entity.TherapySessions;
import ijse.theropy_system.entity.Therapist;

import java.time.LocalDate;
import java.util.*;

public class ReportBOImpl implements ReportBO {

    private final SessionDAO sessionDAO = (SessionDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.SESSION);
    private final PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PATIENT);
    private final TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.THERAPIST);
    private final ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PROGRAM);
    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PAYMENT);

    @Override
    public long getTotalSessions() throws Exception {
        return sessionDAO.findAll().size();
    }

    @Override
    public long getTotalPatients() throws Exception {
        return patientDAO.findAll().size();
    }

    @Override
    public long getTotalTherapists() throws Exception {
        return therapistDAO.findAll().size();
    }

    @Override
    public long getTotalPrograms() throws Exception {
        return programDAO.findAll().size();
    }

    @Override
    public TherapistDTO getTopTherapist() throws Exception {
        List<Therapist> therapists = therapistDAO.findAll();
        Therapist topTherapist = null;
        long maxSessions = 0;

        for (Therapist t : therapists) {
            long count = sessionDAO.findByTherapistId(t.getId()).size();
            if (count > maxSessions) {
                maxSessions = count;
                topTherapist = t;
            }
        }

        if (topTherapist == null) return null;

        TherapistDTO dto = new TherapistDTO();
        dto.setId(topTherapist.getId());
        dto.setName(topTherapist.getName());
        dto.setSpecialization(topTherapist.getSpecialization());
        dto.setEmail(topTherapist.getEmail());
        dto.setPhone(topTherapist.getPhone());
        dto.setHireDate(topTherapist.getHireDate());
        dto.setAvailability(topTherapist.getAvailability());
        return dto;
    }

    @Override
    public Map<String, Long> getSessionCountByTherapist() throws Exception {
        Map<String, Long> result = new LinkedHashMap<>();
        List<Therapist> therapists = therapistDAO.findAll();
        for (Therapist t : therapists) {
            long count = sessionDAO.findByTherapistId(t.getId()).size();
            result.put(t.getName(), count);
        }
        return result;
    }

    @Override
    public List<PaymentDTO> getFinancialReport(LocalDate from, LocalDate to) throws Exception {
        List<Payment> payments = paymentDAO.findByDateRange(from, to);
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment p : payments) {
            PaymentDTO dto = new PaymentDTO();
            dto.setId(p.getId());
            dto.setAmount(p.getAmount());
            dto.setDate(p.getDate());
            dto.setStatus(p.getStatus().name());
            dto.setPaymentMethod(p.getPaymentMethod());
            dto.setInvoiceNumber(p.getInvoiceNumber());
            if (p.getPatient() != null) {
                dto.setPatientId(p.getPatient().getId());
                dto.setPatientName(p.getPatient().getName());
            }
            if (p.getProgram() != null) {
                dto.setProgramId(p.getProgram().getId());
                dto.setProgramName(p.getProgram().getName());
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<PatientDTO> getPatientTherapyHistory(String patientId) throws Exception {
        List<Patient> patients = patientDAO.findPatientsWithPrograms();
        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getId().equals(patientId)) {
                PatientDTO dto = new PatientDTO();
                dto.setId(p.getId());
                dto.setName(p.getName());
                dto.setEmail(p.getEmail());
                dto.setPhone(p.getPhone());
                dto.setAddress(p.getAddress());
                dto.setDateOfBirth(p.getDateOfBirth());
                dto.setRegistrationDate(p.getRegistrationDate());
                if (p.getPrograms() != null) {
                    dto.setProgramIds(p.getPrograms().stream()
                            .map(prog -> prog.getId()).toList());
                }
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Override
    public List<SessionDTO> getTherapistSessionHistory(String therapistId) throws Exception {
        List<TherapySessions> sessions = sessionDAO.findByTherapistId(therapistId);
        List<SessionDTO> dtos = new ArrayList<>();
        for (TherapySessions s : sessions) {
            SessionDTO dto = new SessionDTO();
            dto.setId(s.getId());
            dto.setDate(s.getDate());
            dto.setTime(s.getTime());
            dto.setStatus(s.getStatus().name());
            dto.setNotes(s.getNotes());
            if (s.getTherapist() != null) {
                dto.setTherapistId(s.getTherapist().getId());
                dto.setTherapistName(s.getTherapist().getName());
            }
            if (s.getPatient() != null) {
                dto.setPatientId(s.getPatient().getId());
                dto.setPatientName(s.getPatient().getName());
            }
            if (s.getProgram() != null) {
                dto.setProgramId(s.getProgram().getId());
                dto.setProgramName(s.getProgram().getName());
            }
            dtos.add(dto);
        }
        return dtos;
    }
}
