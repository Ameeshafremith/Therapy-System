package ijse.theropy_system.bo.custom.impl;

import ijse.theropy_system.bo.custom.PatientBO;
import ijse.theropy_system.dao.custom.DAOFactory;
import ijse.theropy_system.dao.custom.PatientDAO;
import ijse.theropy_system.dao.custom.ProgramDAO;
import ijse.theropy_system.dto.PatientDTO;
import ijse.theropy_system.entity.Patient;
import ijse.theropy_system.entity.TheropyPrograms;
import ijse.theropy_system.exception.RegistrationException;
import ijse.theropy_system.util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientBOImpl implements PatientBO {

    private final PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PATIENT);
    private final ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PROGRAM);

    @Override
    public boolean savePatient(PatientDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getId(), "Patient ID");
        ValidationUtil.validateName(dto.getName());
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.validatePhone(dto.getPhone());

        Patient existing = patientDAO.findById(dto.getId());
        if (existing != null) {
            throw new RegistrationException("Patient ID already exists");
        }

        Patient patient = new Patient(dto.getId(), dto.getName(), dto.getEmail(),
                dto.getPhone(), dto.getAddress(), dto.getDateOfBirth(), dto.getRegistrationDate());

        if (dto.getProgramIds() != null) {
            for (String programId : dto.getProgramIds()) {
                TheropyPrograms program = programDAO.findById(programId);
                if (program != null) {
                    patient.getPrograms().add(program);
                }
            }
        }

        return patientDAO.save(patient);
    }

    @Override
    public boolean updatePatient(PatientDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getId(), "Patient ID");
        ValidationUtil.validateName(dto.getName());
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.validatePhone(dto.getPhone());

        Patient patient = patientDAO.findById(dto.getId());
        if (patient == null) {
            throw new RegistrationException("Patient not found");
        }

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getProgramIds() != null) {
            patient.getPrograms().clear();
            for (String programId : dto.getProgramIds()) {
                TheropyPrograms program = programDAO.findById(programId);
                if (program != null) {
                    patient.getPrograms().add(program);
                }
            }
        }

        return patientDAO.update(patient);
    }

    @Override
    public boolean deletePatient(String id) throws Exception {
        return patientDAO.delete(id);
    }

    @Override
    public PatientDTO getPatientById(String id) throws Exception {
        Patient patient = patientDAO.findById(id);
        return patient != null ? convertToDTO(patient) : null;
    }

    @Override
    public List<PatientDTO> getAllPatients() throws Exception {
        List<Patient> patients = patientDAO.findAll();
        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    @Override
    public List<PatientDTO> searchPatientsByName(String name) throws Exception {
        List<Patient> patients = patientDAO.searchByName(name);
        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    @Override
    public List<PatientDTO> getPatientsEnrolledInAllPrograms() throws Exception {
        List<Patient> patients = patientDAO.findPatientsEnrolledInAllPrograms();
        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    @Override
    public List<PatientDTO> getPatientsWithPrograms() throws Exception {
        List<Patient> patients = patientDAO.findPatientsWithPrograms();
        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setRegistrationDate(patient.getRegistrationDate());
        if (patient.getPrograms() != null) {
            dto.setProgramIds(patient.getPrograms().stream()
                    .map(TheropyPrograms::getId).toList());
        }
        return dto;
    }
}
