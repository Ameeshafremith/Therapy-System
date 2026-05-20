package ijse.theropy_system.bo.custom.impl;

import ijse.theropy_system.bo.custom.SessionBO;
import ijse.theropy_system.dao.custom.DAOFactory;
import ijse.theropy_system.dao.custom.PatientDAO;
import ijse.theropy_system.dao.custom.ProgramDAO;
import ijse.theropy_system.dao.custom.SessionDAO;
import ijse.theropy_system.dao.custom.TherapistDAO;
import ijse.theropy_system.dto.SessionDTO;
import ijse.theropy_system.entity.Patient;
import ijse.theropy_system.entity.TheropyPrograms;
import ijse.theropy_system.entity.Therapist;
import ijse.theropy_system.entity.TherapySessions;
import ijse.theropy_system.exception.SchedulingException;
import ijse.theropy_system.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SessionBOImpl implements SessionBO {

    private final SessionDAO sessionDAO = (SessionDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.SESSION);
    private final PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PATIENT);
    private final TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.THERAPIST);
    private final ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PROGRAM);

    @Override
    public boolean bookSession(SessionDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getPatientId(), "Patient");
        ValidationUtil.validateRequired(dto.getTherapistId(), "Therapist");
        ValidationUtil.validateRequired(dto.getProgramId(), "Program");

        if (dto.getDate() == null) {
            throw new SchedulingException("Session date is required");
        }
        if (dto.getTime() == null) {
            throw new SchedulingException("Session time is required");
        }

        // Check scheduling conflict
        if (sessionDAO.hasSchedulingConflict(dto.getTherapistId(), dto.getDate(), dto.getTime())) {
            throw new SchedulingException("Therapist already has a session at this date and time. Please choose a different time.");
        }

        Patient patient = patientDAO.findById(dto.getPatientId());
        if (patient == null) {
            throw new SchedulingException("Patient not found");
        }
        Therapist therapist = therapistDAO.findById(dto.getTherapistId());
        if (therapist == null) {
            throw new SchedulingException("Therapist not found");
        }
        TheropyPrograms program = programDAO.findById(dto.getProgramId());
        if (program == null) {
            throw new SchedulingException("Program not found");
        }

        TherapySessions session = new TherapySessions();
        session.setDate(dto.getDate());
        session.setTime(dto.getTime());
        session.setStatus(TherapySessions.SessionStatus.valueOf(dto.getStatus()));
        session.setNotes(dto.getNotes());
        session.setRoom(dto.getRoom());
        session.setPatient(patient);
        session.setTherapist(therapist);
        session.setProgram(program);

        return sessionDAO.save(session);
    }

    @Override
    public boolean updateSession(SessionDTO dto) throws Exception {
        TherapySessions session = sessionDAO.findById(dto.getId());
        if (session == null) {
            throw new SchedulingException("Session not found");
        }

        // Check scheduling conflict if date/time/therapist changed
        if (!session.getTherapist().getId().equals(dto.getTherapistId()) ||
            !session.getDate().equals(dto.getDate()) ||
            !session.getTime().equals(dto.getTime())) {

            if (sessionDAO.hasSchedulingConflict(dto.getTherapistId(), dto.getDate(), dto.getTime())) {
                throw new SchedulingException("Therapist already has a session at this date and time.");
            }
        }

        Patient patient = patientDAO.findById(dto.getPatientId());
        Therapist therapist = therapistDAO.findById(dto.getTherapistId());
        TheropyPrograms program = programDAO.findById(dto.getProgramId());

        session.setDate(dto.getDate());
        session.setTime(dto.getTime());
        session.setStatus(TherapySessions.SessionStatus.valueOf(dto.getStatus()));
        session.setNotes(dto.getNotes());
        session.setRoom(dto.getRoom());
        session.setPatient(patient);
        session.setTherapist(therapist);
        session.setProgram(program);

        return sessionDAO.update(session);
    }

    @Override
    public boolean cancelSession(int id) throws Exception {
        TherapySessions session = sessionDAO.findById(id);
        if (session == null) {
            throw new SchedulingException("Session not found");
        }
        session.setStatus(TherapySessions.SessionStatus.CANCELLED);
        return sessionDAO.update(session);
    }

    @Override
    public SessionDTO getSessionById(int id) throws Exception {
        TherapySessions session = sessionDAO.findById(id);
        return session != null ? convertToDTO(session) : null;
    }

    @Override
    public List<SessionDTO> getAllSessions() throws Exception {
        List<TherapySessions> sessions = sessionDAO.findAll();
        List<SessionDTO> dtos = new ArrayList<>();
        for (TherapySessions s : sessions) {
            dtos.add(convertToDTO(s));
        }
        return dtos;
    }

    @Override
    public List<SessionDTO> getSessionsByDate(LocalDate date) throws Exception {
        List<TherapySessions> sessions = sessionDAO.findByDate(date);
        List<SessionDTO> dtos = new ArrayList<>();
        for (TherapySessions s : sessions) {
            dtos.add(convertToDTO(s));
        }
        return dtos;
    }

    @Override
    public List<SessionDTO> getSessionsByPatient(String patientId) throws Exception {
        List<TherapySessions> sessions = sessionDAO.findByPatientId(patientId);
        List<SessionDTO> dtos = new ArrayList<>();
        for (TherapySessions s : sessions) {
            dtos.add(convertToDTO(s));
        }
        return dtos;
    }

    @Override
    public List<SessionDTO> getSessionsByTherapist(String therapistId) throws Exception {
        List<TherapySessions> sessions = sessionDAO.findByTherapistId(therapistId);
        List<SessionDTO> dtos = new ArrayList<>();
        for (TherapySessions s : sessions) {
            dtos.add(convertToDTO(s));
        }
        return dtos;
    }

    private SessionDTO convertToDTO(TherapySessions session) {
        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setDate(session.getDate());
        dto.setTime(session.getTime());
        dto.setStatus(session.getStatus().name());
        dto.setNotes(session.getNotes());
        dto.setRoom(session.getRoom());
        if (session.getTherapist() != null) {
            dto.setTherapistId(session.getTherapist().getId());
            dto.setTherapistName(session.getTherapist().getName());
        }
        if (session.getPatient() != null) {
            dto.setPatientId(session.getPatient().getId());
            dto.setPatientName(session.getPatient().getName());
        }
        if (session.getProgram() != null) {
            dto.setProgramId(session.getProgram().getId());
            dto.setProgramName(session.getProgram().getName());
        }
        return dto;
    }
}
