package app.daos;

import app.entities.Movie;
import app.exceptions.DatabaseException;
import app.persistence.IDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
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

    public List<Movie> createAll(List<Movie> movieList) {
        List<Movie> movies = new ArrayList<>();
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                for (Movie movie : movieList) {
                    try {
                        em.persist(movie);
                        movies.add(movie);
                    } catch (DatabaseException e) {
                    }
                }
                em.getTransaction().commit();
            } catch (DatabaseException e) {
                em.getTransaction().rollback();
            }
            return movies;
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
    public List<Movie> searchByTitle(String title) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:title)", Movie.class)
                    .setParameter("title", "%" + title + "%")
                    .getResultList();
        }
    }
    public Double getAverageRating() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                    "SELECT AVG(m.rating) FROM Movie m", Double.class).getSingleResult();
        }
    }
    public List<Movie> getTop10HighestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Movie m ORDER BY m.rating DESC", Movie.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }
    public List<Movie> getTop10LowestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Movie m ORDER BY m.rating ASC", Movie.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }

}