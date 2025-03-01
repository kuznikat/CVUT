package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "telephone")
public class Telephone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telephone_id", nullable = false)
    private Integer id;

    @Column(name = "telephone_number", nullable = false, length = 50)
    private String telephoneNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

}