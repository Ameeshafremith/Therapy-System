package ijse.theropy_system.dto;

import java.time.LocalDate;
import java.util.List;

public class TherapistDTO {
    private String id;
    private String name;
    private String specialization;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private String availability;
    private List<String> programIds;

    public TherapistDTO() {}

    public TherapistDTO(String id, String name, String specialization, String email,
                        String phone, LocalDate hireDate, String availability) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.availability = availability;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public List<String> getProgramIds() { return programIds; }
    public void setProgramIds(List<String> programIds) { this.programIds = programIds; }
}
