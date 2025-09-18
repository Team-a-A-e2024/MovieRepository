package app.daos;

import app.config.HibernateConfig;
import app.entities.Movie;
import app.populators.MoviePopulatorTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieDAOTest {

    private static EntityManagerFactory emf;
    private MovieDAO movieDAO;

    @BeforeAll
    void setupOnce() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    @AfterAll
    void tearDownOnce() {
        if (emf != null) emf.close();
    }

    @BeforeEach
    void setup() {
        movieDAO = new MovieDAO(emf);
        EntityManager em = emf.createEntityManager();
        MoviePopulatorTest.populate(em);
        em.close();
    }

    @Test
    @Tag("integration")
    void searchByTitle() {
        List<Movie> results = movieDAO.searchByTitle("The Matrix");

        assertNotNull(results);
        assertEquals(1, results.size());

        Movie movie = results.get(0);
        assertEquals(MoviePopulatorTest.matrix, movie);
    }

    @Test
    @Tag("integration")
    void testGetAverageRating() {
        double avg = movieDAO.getAverageRating();
        double expected = (MoviePopulatorTest.matrix.getRating()
                + MoviePopulatorTest.inception.getRating()
                + MoviePopulatorTest.room.getRating()
                + MoviePopulatorTest.frozen.getRating()) / 4.0;

        assertEquals(expected, avg, 0.001);
    }

    @Test
    @Tag("integration")
    void testTopRatedMovies() {
        List<Movie> results = movieDAO.getTop10HighestRated();

        assertEquals(4, results.size());

        Movie m1 = results.get(0);
        Movie m2 = results.get(1);
        Movie m3 = results.get(2);
        Movie m4 = results.get(3);

        assertEquals(MoviePopulatorTest.matrix, m1);
        assertEquals(MoviePopulatorTest.inception, m2);
        assertEquals(MoviePopulatorTest.frozen, m3);
        assertEquals(MoviePopulatorTest.room, m4);
    }

    @Test
    @Tag("integration")
    void testLowestRatedMovies() {
        List<Movie> results = movieDAO.getTop10LowestRated();

        assertEquals(4, results.size());

        Movie m1 = results.get(0);
        Movie m2 = results.get(1);
        Movie m3 = results.get(2);
        Movie m4 = results.get(3);

        assertEquals(MoviePopulatorTest.room, m1);
        assertEquals(MoviePopulatorTest.frozen, m2);
        assertEquals(MoviePopulatorTest.inception, m3);
        assertEquals(MoviePopulatorTest.matrix, m4);
    }
}