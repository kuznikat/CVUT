package dao;

import entities.Person;
import jakarta.persistence.*;
import jakarta.transaction.Transaction;


public class PersonDao extends BaseDao<Person> {

    public PersonDao() {
        super(Person.class);
    }

    public PersonDao(EntityManager em) {
        super(Person.class, em);
    }

    public void insertPerson(Person person){
        persist(person);
    }

    public Person findBySurname( String surname) {
        try{
            em = getEntityManager();
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE p.surname = :surname", Person.class);
            query.setParameter("surname", surname);
            return query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }finally {
            if(em != null){
                em.close();
            }
        }

    }


    public Person updatePerson(Person person) {
            return update(person);
    }


    public Person findByRodneCislo(String rodneCislo) {
        try {
            em = getEntityManager();
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE p.rodneCislo = :rodneCislo", Person.class);
            query.setParameter("rodneCislo", rodneCislo);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    public void deletePerson(Person person) {
        remove(person);
    }
}




