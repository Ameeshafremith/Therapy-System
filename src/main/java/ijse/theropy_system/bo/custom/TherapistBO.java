package ijse.theropy_system.bo.custom;

import ijse.theropy_system.bo.SuperBO;
import ijse.theropy_system.dto.TherapistDTO;

import java.util.List;

public interface TherapistBO extends SuperBO {
    boolean saveTherapist(TherapistDTO dto) throws Exception;
    boolean updateTherapist(TherapistDTO dto) throws Exception;
    boolean deleteTherapist(String id) throws Exception;
    TherapistDTO getTherapistById(String id) throws Exception;
    List<TherapistDTO> getAllTherapists() throws Exception;
    List<TherapistDTO> getAvailableTherapists() throws Exception;
    boolean assignProgramToTherapist(String therapistId, String programId) throws Exception;
}
