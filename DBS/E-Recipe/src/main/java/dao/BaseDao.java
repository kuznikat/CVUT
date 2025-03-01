package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

import java.sql.SQLOutput;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class BaseDao<T> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ApplicationPU");

    protected EntityManager em;

    protected final Class<T> type;


    protected BaseDao(Class<T> type) {
        this.type = type;
    }

    protected BaseDao(Class<T> type, EntityManager em) {
        this.type = type;
        this.em = em;
    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public T find(long id){
        Objects.requireNonNull(id);
        try{
            em = emf.createEntityManager();
            return em.find(type,id);
        }finally {
            if(em != null){
                em.close();
            }
        }
    }

    public List<T> findAll(){
        try{
            em = emf.createEntityManager();
            List<T> results = em.createQuery("SELECT e FROM" + type.getSimpleName() + " e", type).getResultList();
            System.out.println("Found " + results.size() + "entities of type " + type.getSimpleName());
            return results;
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        } finally {
            if(em != null){
                em.close();
            }
        }
    }

    public void persist(T entity){
        Objects.requireNonNull(entity);
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }catch (RuntimeException e){
            throw new PersistenceException(e);
        } finally {
            if(em != null){
                em.close();
            }
        }
    }

    public void persist(Collection<T> entities){
        Objects.requireNonNull(entities);
        if(entities.isEmpty()){
            return;
        }
        try {
            entities.forEach(this::persist);
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    public T update(T entity){
        Objects.requireNonNull(entity);
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();
            T tmp = em.merge(entity);
            em.getTransaction().commit();
            return tmp;
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        } finally {
            if(em != null){
                em.close();
            }
        }
    }

    public void remove(T entity){
        Objects.requireNonNull(entity);
        try{
            em = emf.createEntityManager();
            final T toRemove = em.merge(entity);
            if(toRemove != null){
                em.getTransaction().begin();
                em.remove(toRemove);
                em.getTransaction().commit();
            }
        }catch (RuntimeException e){
            throw new PersistenceException(e);
        }finally {
            if(em != null){
                em.close();
            }
        }
    }



}
