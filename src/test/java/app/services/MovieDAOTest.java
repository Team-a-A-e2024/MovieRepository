package app.services;

import app.config.HibernateConfig;
import app.daos.MovieDAO;
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
        List<Movie> results = movieDAO.searchByTitle("matrix");

        assertNotNull(results);
        assertEquals(1, results.size());

        Movie movie = results.get(0);
        assertEquals(MoviePopulatorTest.matrix.getImdbID(), movie.getImdbID());
        assertEquals(MoviePopulatorTest.matrix.getTitle(), movie.getTitle());
        assertEquals(MoviePopulatorTest.matrix.getOverview(), movie.getOverview());
        assertEquals(MoviePopulatorTest.matrix.getRating(), movie.getRating());
        assertEquals(MoviePopulatorTest.matrix.getReleaseDate(), movie.getReleaseDate());
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

        assertEquals(MoviePopulatorTest.matrix.getTitle(), results.get(0).getTitle());
        assertEquals(MoviePopulatorTest.inception.getTitle(), results.get(1).getTitle());
        assertEquals(MoviePopulatorTest.frozen.getTitle(), results.get(2).getTitle());
        assertEquals(MoviePopulatorTest.room.getTitle(), results.get(3).getTitle());
    }

    @Test
    @Tag("integration")
    void testLowestRatedMovies() {
        List<Movie> results = movieDAO.getTop10LowestRated();

        assertEquals(4, results.size());

        assertEquals(MoviePopulatorTest.room.getTitle(), results.get(0).getTitle());
        assertEquals(MoviePopulatorTest.frozen.getTitle(), results.get(1).getTitle());
        assertEquals(MoviePopulatorTest.inception.getTitle(), results.get(2).getTitle());
        assertEquals(MoviePopulatorTest.matrix.getTitle(), results.get(3).getTitle());
    }

}