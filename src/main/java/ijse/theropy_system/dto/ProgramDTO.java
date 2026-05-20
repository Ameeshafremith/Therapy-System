package ijse.theropy_system.dto;

import java.math.BigDecimal;

public class ProgramDTO {
    private String id;
    private String name;
    private String description;
    private int durationWeeks;
    private BigDecimal fee;

    public ProgramDTO() {}

    public ProgramDTO(String id, String name, String description, int durationWeeks, BigDecimal fee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationWeeks = durationWeeks;
        this.fee = fee;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(int durationWeeks) { this.durationWeeks = durationWeeks; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
}
