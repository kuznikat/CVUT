package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "patient_telephone")
public class PatientTelephone {
    @EmbeddedId
    private PatientTelephoneId id;

    @MapsId("telephoneId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "telephone_id", nullable = false)
    private Telephone telephone;

    @MapsId("patientId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    public PatientTelephoneId getId() {
        return id;
    }

    public void setId(PatientTelephoneId id) {
        this.id = id;
    }

    public Telephone getTelephone() {
        return telephone;
    }

    public void setTelephone(Telephone telephone) {
        this.telephone = telephone;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}