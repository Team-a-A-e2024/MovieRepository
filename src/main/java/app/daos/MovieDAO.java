package app.daos;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
public class MovieDAO implements IDAO<Movie, Integer> {


    private final EntityManagerFactory emf;


    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public Movie create(Movie m) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(m);
            em.getTransaction().commit();
            return m;
        }
    }


    @Override
    public List<Movie> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m", Movie.class)
                    .getResultList();
        }
    }


    @Override
    public Movie getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Movie.class, id);
        }
    }


    @Override
    public Movie update(Movie m) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie merged = em.merge(m);
            em.getTransaction().commit();
            return merged;
        }
    }


    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie m = em.find(Movie.class, id);
            if (m != null) {
                em.remove(m);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        }
    }
}