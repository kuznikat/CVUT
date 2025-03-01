package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class PatientTelephoneId implements java.io.Serializable {
    private static final long serialVersionUID = 4143390802708102457L;
    @Column(name = "telephone_id", nullable = false)
    private Integer telephoneId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    public Integer getTelephoneId() {
        return telephoneId;
    }

    public void setTelephoneId(Integer telephoneId) {
        this.telephoneId = telephoneId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PatientTelephoneId entity = (PatientTelephoneId) o;
        return Objects.equals(this.patientId, entity.patientId) &&
                Objects.equals(this.telephoneId, entity.telephoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, telephoneId);
    }

}