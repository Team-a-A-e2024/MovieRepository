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

        Movie first = results.get(0);
        Movie second = results.get(1);
        Movie third = results.get(2);
        Movie fourth = results.get(3);

        assertEquals(MoviePopulatorTest.matrix, first);
        assertEquals(MoviePopulatorTest.inception, second);
        assertEquals(MoviePopulatorTest.frozen, third);
        assertEquals(MoviePopulatorTest.room, fourth);
    }

    @Test
    @Tag("integration")
    void testLowestRatedMovies() {
        List<Movie> results = movieDAO.getTop10LowestRated();

        assertEquals(4, results.size());

        Movie first = results.get(0);
        Movie second = results.get(1);
        Movie third = results.get(2);
        Movie fourth = results.get(3);

        assertEquals(MoviePopulatorTest.room, first);
        assertEquals(MoviePopulatorTest.frozen, second);
        assertEquals(MoviePopulatorTest.inception, third);
        assertEquals(MoviePopulatorTest.matrix, fourth);
    }
}