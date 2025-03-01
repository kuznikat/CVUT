package entities;

import jakarta.persistence.*;


@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "person_id")
public class Doctor{
    @Id
    @Column(name = "doctor_id", nullable = false)
    private Integer id;

    @Column(name = "license", nullable = false, length = 50)
    private String license;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "specialization", nullable = false, length = 50)
    private String specialization;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}