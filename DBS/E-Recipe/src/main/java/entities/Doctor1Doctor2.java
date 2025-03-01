package entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "doctor1_doctor2")
public class Doctor1Doctor2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "license1", nullable = false, length = 50)
    private String license1;

    @Column(name = "license2", nullable = false, length = 50)
    private String license2;

    @Column(name = "datum", nullable = false)
    private LocalDate datum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getLicense1() {
        return license1;
    }

    public void setLicense1(String license1) {
        this.license1 = license1;
    }

    public String getLicense2() {
        return license2;
    }

    public void setLicense2(String license2) {
        this.license2 = license2;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

}