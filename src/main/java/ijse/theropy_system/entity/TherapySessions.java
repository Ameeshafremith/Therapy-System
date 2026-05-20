package ijse.theropy_system.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "therapy_session")
public class TherapySessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status;

    @Column
    private String notes;

    @Column(name = "room")
    private String room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private TheropyPrograms program;

    public enum SessionStatus {
        SCHEDULED, COMPLETED, CANCELLED
    }

    public TherapySessions() {
    }

    public TherapySessions(LocalDate date, LocalTime time, SessionStatus status,
                           String notes, String room, Therapist therapist, Patient patient,
                           TheropyPrograms program) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.notes = notes;
        this.room = room;
        this.therapist = therapist;
        this.patient = patient;
        this.program = program;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Therapist getTherapist() {
        return therapist;
    }

    public void setTherapist(Therapist therapist) {
        this.therapist = therapist;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public TheropyPrograms getProgram() {
        return program;
    }

    public void setProgram(TheropyPrograms program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return "TherapySessions{id=" + id + ", date=" + date + ", time=" + time +
                ", status=" + status + "}";
    }
}
