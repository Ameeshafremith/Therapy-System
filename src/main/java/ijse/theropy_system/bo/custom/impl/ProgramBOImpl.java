package ijse.theropy_system.bo.custom.impl;

import ijse.theropy_system.bo.custom.ProgramBO;
import ijse.theropy_system.dao.custom.DAOFactory;
import ijse.theropy_system.dao.custom.ProgramDAO;
import ijse.theropy_system.dto.ProgramDTO;
import ijse.theropy_system.entity.TheropyPrograms;
import ijse.theropy_system.exception.RegistrationException;
import ijse.theropy_system.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class ProgramBOImpl implements ProgramBO {

    private final ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAOFactory(DAOFactory.DAOType.PROGRAM);

    @Override
    public boolean saveProgram(ProgramDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getId(), "Program ID");
        ValidationUtil.validateRequired(dto.getName(), "Program Name");
        ValidationUtil.validatePositiveNumber(dto.getFee(), "Program Fee");

        TheropyPrograms existing = programDAO.findById(dto.getId());
        if (existing != null) {
            throw new RegistrationException("Program ID already exists");
        }

        TheropyPrograms program = new TheropyPrograms(dto.getId(), dto.getName(),
                dto.getDescription(), dto.getDurationWeeks(), dto.getFee());
        return programDAO.save(program);
    }

    @Override
    public boolean updateProgram(ProgramDTO dto) throws Exception {
        ValidationUtil.validateRequired(dto.getId(), "Program ID");
        ValidationUtil.validateRequired(dto.getName(), "Program Name");
        ValidationUtil.validatePositiveNumber(dto.getFee(), "Program Fee");

        TheropyPrograms program = programDAO.findById(dto.getId());
        if (program == null) {
            throw new RegistrationException("Program not found");
        }

        program.setName(dto.getName());
        program.setDescription(dto.getDescription());
        program.setDurationWeeks(dto.getDurationWeeks());
        program.setFee(dto.getFee());

        return programDAO.update(program);
    }

    @Override
    public boolean deleteProgram(String id) throws Exception {
        return programDAO.delete(id);
    }

    @Override
    public ProgramDTO getProgramById(String id) throws Exception {
        TheropyPrograms program = programDAO.findById(id);
        return program != null ? convertToDTO(program) : null;
    }

    @Override
    public List<ProgramDTO> getAllPrograms() throws Exception {
        List<TheropyPrograms> programs = programDAO.findAll();
        List<ProgramDTO> dtos = new ArrayList<>();
        for (TheropyPrograms p : programs) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }

    private ProgramDTO convertToDTO(TheropyPrograms program) {
        ProgramDTO dto = new ProgramDTO();
        dto.setId(program.getId());
        dto.setName(program.getName());
        dto.setDescription(program.getDescription());
        dto.setDurationWeeks(program.getDurationWeeks());
        dto.setFee(program.getFee());
        return dto;
    }
}
