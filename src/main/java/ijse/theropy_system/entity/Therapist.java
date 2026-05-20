package ijse.theropy_system.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "therapist")
public class Therapist {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false, length = 20)
    private String availability = "Available";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "therapist_program",
            joinColumns = @JoinColumn(name = "therapist_id"),
            inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private List<TheropyPrograms> programs = new ArrayList<>();

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TherapySessions> sessions = new ArrayList<>();

    public Therapist() {
    }

    public Therapist(String id, String name, String specialization, String email,
                     String phone, LocalDate hireDate, String availability) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
        this.availability = availability;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public List<TheropyPrograms> getPrograms() {
        return programs;
    }

    public void setPrograms(List<TheropyPrograms> programs) {
        this.programs = programs;
    }

    public List<TherapySessions> getSessions() {
        return sessions;
    }

    public void setSessions(List<TherapySessions> sessions) {
        this.sessions = sessions;
    }

    @Override
    public String toString() {
        return "Therapist{id='" + id + "', name='" + name + "', specialization='" + specialization + "'}";
    }
}