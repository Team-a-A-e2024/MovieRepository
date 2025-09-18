package app.services;

import app.config.HibernateConfig;
import app.daos.MovieDAO;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieDAOTest {

    private static EntityManagerFactory emf;
    private MovieDAO movieDAO;

        @BeforeAll
        static void setupOnce() {
            emf = HibernateConfig.getEntityManagerFactoryForTest();
            assertNotNull(emf, "EntityManagerFactory should not be null");
        }


    @BeforeEach
    void setup() {
        movieDAO = new MovieDAO(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Movie").executeUpdate();

        em.persist(Movie.builder().title("The Matrix").rating(8.7).releaseDate(LocalDate.of(1999,3,31)).build());
        em.persist(Movie.builder().title("Inception").rating(8.5).releaseDate(LocalDate.of(2010,7,16)).build());
        em.persist(Movie.builder().title("The Room").rating(3.7).releaseDate(LocalDate.of(2003,6,27)).build());
        em.persist(Movie.builder().title("Frozen").rating(7.5).releaseDate(LocalDate.of(2013,11,27)).build());

        em.getTransaction().commit();
        em.close();
    }

    @AfterAll
    static void tearDownOnce() {
        emf.close();
    }
    @Test
    void searchByTitle() {

        List<Movie> results = movieDAO.searchByTitle("matrix");

        assertNotNull(results);

        assertEquals(1, results.size());
        assertEquals("The Matrix", results.get(0).getTitle());
    }

    @Test
    void testGetAverageRating() {
        Double avg = movieDAO.getAverageRating();

        double expected = (8.7 + 8.5 + 3.7 + 7.5) / 4;

        assertNotNull(avg);
        assertEquals(expected, avg, 0.001);
    }

    @Test
    void testTop10HighestRated() {
        List<Movie> top = movieDAO.getTop10HighestRated();

        assertEquals("The Matrix", top.get(0).getTitle());
        assertEquals("Inception", top.get(1).getTitle());
    }

    @Test
    void testTop10LowestRated() {
        List<Movie> low = movieDAO.getTop10LowestRated();

        assertEquals("The Room", low.get(0).getTitle());
        assertEquals("Frozen", low.get(1).getTitle());
    }
}
