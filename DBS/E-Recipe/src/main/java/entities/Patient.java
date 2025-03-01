package entities;

import jakarta.persistence.*;


@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "person_id")
public class Patient{
    @Id
    @Column(name = "patient_id", nullable = false)
    private Integer id;

    @Column(name = "mr_number", nullable = false, length = 50)
    private String mrNumber;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMrNumber() {
        return mrNumber;
    }

    public void setMrNumber(String mrNumber) {
        this.mrNumber = mrNumber;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}