package dao;

import entities.Doctor;
import entities.Doctor1Doctor2;
import entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class Doctor1Doctor2Dao extends BaseDao<Doctor1Doctor2>{

    public Doctor1Doctor2Dao() {super(Doctor1Doctor2.class);}

    public Doctor1Doctor2Dao(EntityManager em) {super(Doctor1Doctor2.class, em);}

    public Doctor1Doctor2 findByDoctorId(int doctorId) {
        try {
            em = getEntityManager();
            TypedQuery<Doctor1Doctor2> query = em.createQuery(
                    "SELECT d FROM Doctor1Doctor2 d WHERE d.id = :id", Doctor1Doctor2.class);
            query.setParameter("id", doctorId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void performTransaction(){
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Insert first Person if it doesn't exist
            Person person1 = em.find(Person.class, 39); // Replace with appropriate person_id
            if (person1 == null) {
                person1 = new Person();
                person1.setId(39); // Replace with appropriate person_id
                person1.setName("Rurri");
                person1.setSurname("Morru");
                em.persist(person1);
            }

            // Insert first Doctor
            Doctor doctor1 = new Doctor();
            doctor1.setId(20); // Make sure the ID is unique and correctly set
            doctor1.setLicense("020F11R1");
            doctor1.setPerson(person1);
            doctor1.setSpecialization("Pathologist");
            em.persist(doctor1);

            // Insert second Person if it doesn't exist
            Person person2 = em.find(Person.class, 41); // Replace with appropriate person_id
            if (person2 == null) {
                person2 = new Person();
                person2.setId(41); // Replace with appropriate person_id
                person2.setName("Yuzzi");
                person2.setSurname("Kruzzi");
                em.persist(person2);
            }

            // Insert second Doctor
            Doctor doctor2 = new Doctor();
            doctor2.setId(21); // Make sure the ID is unique and correctly set
            doctor2.setLicense("021F11R1");
            doctor2.setPerson(person2);
            doctor2.setSpecialization("Cardiovascular");
            em.persist(doctor2);

            // Update Doctor1Doctor2
            em.createQuery("UPDATE Doctor1Doctor2 d SET d.license1 = :license1, d.license2 = :license2 WHERE d.id = :id")
                    .setParameter("license1", "020F11R1")
                    .setParameter("license2", "021F11R1")
                    .setParameter("id", 5)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
