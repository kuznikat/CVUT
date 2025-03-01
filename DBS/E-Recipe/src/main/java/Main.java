
import dao.Doctor1Doctor2Dao;
import dao.PersonDao;
import entities.Doctor1Doctor2;
import entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


import java.sql.SQLOutput;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ApplicationPU");
        EntityManager em = emf.createEntityManager();

        //Create an instance of PersonDao with EntityManager
        PersonDao personDao = new PersonDao(em);

        //Create a new Person
        Person newPerson = new Person();

//        newPerson.setRodneCislo("123456777/4");
//        newPerson.setName("");
//        newPerson.setSurname("");
//        newPerson.setBirthday(LocalDate.of(2005,1,2));


//        INSERT the new Person using PersonDao
//        personDao.insertPerson(newPerson);
//        System.out.println("\nInsert new Person: " + newPerson.getName() + " " + newPerson.getSurname());


        //UPDATE surname
//        Person foundPerson = personDao.findBySurname("");
//        foundPerson.setSurname("");
//        personDao.updatePerson(foundPerson);
//        System.out.println("\nUpdated Persons surname is: " + foundPerson.getSurname());


        //DELETE person
//        Person findPerson = personDao.findByRodneCislo("");
//        personDao.deletePerson(findPerson);
//        System.out.println("\nDeleted Person: " + findPerson.getName() + " " + findPerson.getSurname());

        //TRANSACTION
//        Doctor1Doctor2Dao doctor1Doctor2Dao = new Doctor1Doctor2Dao(em);

//        try {
//            // Perform the transaction
//            doctor1Doctor2Dao.performTransaction();
//            System.out.println("Transaction completed successfully.");
//            Doctor1Doctor2 doctor1Doctor2 = doctor1Doctor2Dao.findByDoctorId(5);
//            System.out.println("\nChanges in doctors consultation after transaction: doctor with licence " + doctor1Doctor2.getLicense1() + "will consult doctor with licence " + doctor1Doctor2.getLicense2());
//        } finally {
//            if (em != null && em.isOpen()) {
//                em.close();
//            }
//            emf.close();
//        }

    }
}


