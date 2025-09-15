package app.daos;

import app.entities.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
public class GenreDAO implements IDAO<Genre, Integer> {


    private final EntityManagerFactory emf;


    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public Genre create(Genre g) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(g);
            em.getTransaction().commit();
            return g;
        }
    }


    @Override
    public List<Genre> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT g FROM Genre g", Genre.class)
                    .getResultList();
        }
    }


    @Override
    public Genre getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Genre.class, id);
        }
    }


    @Override
    public Genre update(Genre g) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Genre merged = em.merge(g);
            em.getTransaction().commit();
            return merged;
        }
    }


    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Genre g = em.find(Genre.class, id);
            if (g != null) {
                em.remove(g);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        }
    }
}