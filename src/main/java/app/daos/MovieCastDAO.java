package app.daos;

import app.entities.MovieCast;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
public class MovieCastDAO implements IDAO<MovieCast, Integer> {


    private final EntityManagerFactory emf;


    public MovieCastDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public MovieCast create(MovieCast mc) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(mc);
            em.getTransaction().commit();
            return mc;
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
    public MovieCast getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(MovieCast.class, id);
        }
    }


    @Override
    public MovieCast update(MovieCast mc) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            MovieCast merged = em.merge(mc);
            em.getTransaction().commit();
            return merged;
        }
    }


    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            MovieCast mc = em.find(MovieCast.class, id);
            if (mc != null) {
                em.remove(mc);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        }
    }
}