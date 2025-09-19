package app.daos;


import app.entities.Person;
import app.exceptions.DatabaseException;
import app.persistence.IDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public class PersonDAO implements IDao<Person, Integer> {

    private final EntityManagerFactory emf;

    public PersonDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Person create(Person p) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Person existing = em.find(Person.class, p.getId());
                if (existing == null){
                    em.persist(p);
                    existing = p;
                }
                em.getTransaction().commit();
                return existing;
            } catch (DatabaseException e) {
                em.getTransaction().rollback();
            }
            return null;
        }
    }

    @Override
    public Optional<Person> getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Person.class, id));
        }
    }

    @Override
    public List<Person> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Person p", Person.class)
                    .getResultList();
        }
    }

    @Override
    public Person update(Person p) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Person merged = em.merge(p);
                em.getTransaction().commit();
                return merged;
            } catch (DatabaseException e) {
                em.getTransaction().rollback();
            }
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Person p = em.find(Person.class, id);
                if (p != null) {
                    em.remove(p);
                    em.getTransaction().commit();
                    return true;
                }
            } catch (DatabaseException e) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }
}