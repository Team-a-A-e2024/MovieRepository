package app.daos;


import app.entities.Movie;
import app.exceptions.DatabaseException;
import app.persistence.IDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


import java.util.List;
import java.util.Optional;


public class MovieDAO implements IDao<Movie, Integer> {


    private final EntityManagerFactory emf;


    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public Movie create(Movie m) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                em.persist(m);
                em.getTransaction().commit();
                return m;
            } catch (DatabaseException e) {
                em.getTransaction().rollback();
            }
            return null;
        }
    }


    @Override
    public Optional<Movie> getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Movie.class, id));
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
    public Movie update(Movie m) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Movie merged = em.merge(m);
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
                Movie m = em.find(Movie.class, id);
                if (m != null) {
                    em.remove(m);
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