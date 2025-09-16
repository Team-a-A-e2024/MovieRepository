package app.daos;

import app.entities.Movie;
import app.entities.MovieCast;
import app.exceptions.DatabaseException;
import app.persistence.IDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieCastDAO implements IDao<MovieCast, Integer> {

    private final EntityManagerFactory emf;

    public MovieCastDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public MovieCast create(MovieCast mc) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                em.persist(mc);
                em.getTransaction().commit();
                return mc;
            } catch (DatabaseException e) {
                em.getTransaction().rollback();
            }
            return null;
        }
    }

    public List<MovieCast> createAll(List<MovieCast> movieCastList) {
        List<MovieCast> movieCasts = new ArrayList<>();
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                for (MovieCast movieCast : movieCastList) {
                    try {
                        em.persist(movieCast);
                        movieCasts.add(movieCast);
                    } catch (DatabaseException e) {
                    }
                }
                em.getTransaction().commit();
            } catch (DatabaseException e) {
                em.getTransaction().rollback();
            }
            return movieCasts;
        }
    }

    @Override
    public Optional<MovieCast> getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(MovieCast.class, id));
        }
    }

    @Override
    public List<MovieCast> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT mc FROM MovieCast mc", MovieCast.class)
                    .getResultList();
        }
    }

    @Override
    public MovieCast update(MovieCast mc) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                MovieCast merged = em.merge(mc);
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
                MovieCast mc = em.find(MovieCast.class, id);
                if (mc != null) {
                    em.remove(mc);
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