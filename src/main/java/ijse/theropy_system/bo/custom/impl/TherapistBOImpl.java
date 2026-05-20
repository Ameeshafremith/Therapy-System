package ijse.theropy_system.bo.custom.impl;

import ijse.theropy_system.bo.custom.TherapistBO;
import ijse.theropy_system.dao.custom.DAOFactory;
import ijse.theropy_system.dao.custom.ProgramDAO;
import ijse.theropy_system.dao.custom.TherapistDAO;
import ijse.theropy_system.dto.TherapistDTO;
import ijse.theropy_system.entity.TheropyPrograms;
import ijse.theropy_system.entity.Therapist;
import ijse.theropy_system.exception.RegistrationException;
import ijse.theropy_system.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class TherapistBOImpl implements TherapistBO {

    private final TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.THERAPIST);
    private final ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PROGRAM);

    @Override
    public boolean saveTherapist(TherapistDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getId(), "Therapist ID");
        ValidationUtil.validateName(dto.getName());
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.validatePhone(dto.getPhone());
        ValidationUtil.validateRequired(dto.getSpecialization(), "Specialization");

        Therapist existing = therapistDAO.findById(dto.getId());
        if (existing != null) {
            throw new RegistrationException("Therapist ID already exists");
        }

        Therapist therapist = new Therapist(dto.getId(), dto.getName(), dto.getSpecialization(),
                dto.getEmail(), dto.getPhone(), dto.getHireDate(), dto.getAvailability());

        if (dto.getProgramIds() != null) {
            for (String programId : dto.getProgramIds()) {
                TheropyPrograms program = programDAO.findById(programId);
                if (program != null) {
                    therapist.getPrograms().add(program);
                }
            }
        }

        return therapistDAO.save(therapist);
    }

    @Override
    public boolean updateTherapist(TherapistDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getId(), "Therapist ID");
        ValidationUtil.validateName(dto.getName());
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.validatePhone(dto.getPhone());

        Therapist therapist = therapistDAO.findById(dto.getId());
        if (therapist == null) {
            throw new RegistrationException("Therapist not found");
        }

        therapist.setName(dto.getName());
        therapist.setSpecialization(dto.getSpecialization());
        therapist.setEmail(dto.getEmail());
        therapist.setPhone(dto.getPhone());
        therapist.setHireDate(dto.getHireDate());
        therapist.setAvailability(dto.getAvailability());

        if (dto.getProgramIds() != null) {
            therapist.getPrograms().clear();
            for (String programId : dto.getProgramIds()) {
                TheropyPrograms program = programDAO.findById(programId);
                if (program != null) {
                    therapist.getPrograms().add(program);
                }
            }
        }

        return therapistDAO.update(therapist);
    }

    @Override
    public boolean deleteTherapist(String id) throws Exception {
        return therapistDAO.delete(id);
    }

    @Override
    public TherapistDTO getTherapistById(String id) throws Exception {
        Therapist therapist = therapistDAO.findById(id);
        return therapist != null ? convertToDTO(therapist) : null;
    }

    @Override
    public List<TherapistDTO> getAllTherapists() throws Exception {
        List<Therapist> therapists = therapistDAO.findAll();
        List<TherapistDTO> dtos = new ArrayList<>();
        for (Therapist t : therapists) {
            dtos.add(convertToDTO(t));
        }
        return dtos;
    }

    @Override
    public List<TherapistDTO> getAvailableTherapists() throws Exception {
        List<Therapist> therapists = therapistDAO.findByAvailability("Available");
        List<TherapistDTO> dtos = new ArrayList<>();
        for (Therapist t : therapists) {
            dtos.add(convertToDTO(t));
        }
        return dtos;
    }

    @Override
    public boolean assignProgramToTherapist(String therapistId, String programId) throws Exception {
        Therapist therapist = therapistDAO.findById(therapistId);
        if (therapist == null) {
            throw new RegistrationException("Therapist not found");
        }
        TheropyPrograms program = programDAO.findById(programId);
        if (program == null) {
            throw new RegistrationException("Program not found");
        }
        therapist.getPrograms().add(program);
        return therapistDAO.update(therapist);
    }

    private TherapistDTO convertToDTO(Therapist therapist) {
        TherapistDTO dto = new TherapistDTO();
        dto.setId(therapist.getId());
        dto.setName(therapist.getName());
        dto.setSpecialization(therapist.getSpecialization());
        dto.setEmail(therapist.getEmail());
        dto.setPhone(therapist.getPhone());
        dto.setHireDate(therapist.getHireDate());
        dto.setAvailability(therapist.getAvailability());
        if (therapist.getPrograms() != null) {
            dto.setProgramIds(therapist.getPrograms().stream()
                    .map(TheropyPrograms::getId).toList());
        }
        return dto;
    }
}
