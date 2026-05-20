package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.dto.ProgramDTO;

import java.util.List;

public interface ProgramBO extends SuperBO {
    boolean saveProgram(ProgramDTO dto) throws Exception;
    boolean updateProgram(ProgramDTO dto) throws Exception;
    boolean deleteProgram(String id) throws Exception;
    ProgramDTO getProgramById(String id) throws Exception;
    List<ProgramDTO> getAllPrograms() throws Exception;
}
