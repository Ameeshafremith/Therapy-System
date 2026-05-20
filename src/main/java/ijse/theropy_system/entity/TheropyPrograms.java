package ijse.theropy_system.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "therapy_program")
public class TheropyPrograms {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "duration_weeks")
    private int durationWeeks;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @ManyToMany(mappedBy = "programs", fetch = FetchType.LAZY)
    private List<Therapist> therapists = new ArrayList<>();

    @ManyToMany(mappedBy = "programs", fetch = FetchType.LAZY)
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TherapySessions> sessions = new ArrayList<>();

    public TheropyPrograms() {
    }

    public TheropyPrograms(String id, String name, String description,
                           int durationWeeks, BigDecimal fee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationWeeks = durationWeeks;
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public List<Therapist> getTherapists() {
        return therapists;
    }

    public void setTherapists(List<Therapist> therapists) {
        this.therapists = therapists;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public List<TherapySessions> getSessions() {
        return sessions;
    }

    public void setSessions(List<TherapySessions> sessions) {
        this.sessions = sessions;
    }

    @Override
    public String toString() {
        return "TheropyPrograms{id='" + id + "', name='" + name + "', fee=" + fee + "'}";
    }
}
