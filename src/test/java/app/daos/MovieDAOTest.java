package app.daos;

import app.config.HibernateConfig;
import app.entities.Movie;
import app.populators.MoviePopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieDAOTest {

    private EntityManagerFactory emf;
    private MovieDAO dao;

    @BeforeEach
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new MovieDAO(emf);
    }

    @AfterEach
    void teardown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    // Bruges kun til createAll-testen, så vi ikke kalder populatoren to gange
    private List<Movie> buildMovies() {
        return List.of(
                Movie.builder().title("The Matrix").build(),
                Movie.builder().title("Fight Club").build(),
                Movie.builder().title("Inception").build()
        );
    }

    @Test
    void createMovie() {
        // Arrange + Act (brug populator til at indsætte standardfilm)
        List<Movie> movies = MoviePopulator.populateMovies(dao);

        // Assert (simpelt tjek på første element)
        assertFalse(movies.isEmpty());
        assertNotNull(movies.get(0).getId());
        assertNotNull(movies.get(0).getTitle());
    }

    @Test
    void createAllMovies() {
        // Arrange
        List<Movie> toCreate = buildMovies();

        // Act
        List<Movie> created = dao.createAll(toCreate);

        // Assert (returværdi + DB)
        assertEquals(3, created.size());
        assertTrue(created.stream().anyMatch(m -> "The Matrix".equals(m.getTitle())));
        assertTrue(created.stream().anyMatch(m -> "Fight Club".equals(m.getTitle())));
        assertTrue(created.stream().anyMatch(m -> "Inception".equals(m.getTitle())));
        assertEquals(3, dao.getAll().size());
    }

    @Test
    void getMovieById() {
        // Arrange
        List<Movie> seeded = MoviePopulator.populateMovies(dao);
        Movie first = seeded.get(0);

        // Act
        Optional<Movie> found = dao.getById(first.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(first.getTitle(), found.get().getTitle());
    }

    @Test
    void getAllMovies() {
        // Arrange
        List<Movie> seeded = MoviePopulator.populateMovies(dao);

        // Act
        List<Movie> all = dao.getAll();

        // Assert
        assertEquals(seeded.size(), all.size());
        for (Movie m : seeded) {
            assertTrue(all.stream().anyMatch(x -> x.getTitle().equals(m.getTitle())));
        }
    }

    @Test
    void updateMovie() {
        // Arrange
        Movie original = MoviePopulator.populateMovies(dao).get(0);

        // Act
        Movie updated = dao.update(
                Movie.builder()
                        .id(original.getId())
                        .title(original.getTitle() + " (Updated)")
                        .build()
        );

        // Assert
        assertNotNull(updated);
        assertEquals(original.getId(), updated.getId());
        assertEquals(original.getTitle() + " (Updated)", updated.getTitle());
    }

    @Test
    void deleteMovie() {
        // Arrange
        Movie m = MoviePopulator.populateMovies(dao).get(0);

        // Act
        boolean deleted = dao.delete(m.getId());

        // Assert
        assertTrue(deleted);
        assertTrue(dao.getById(m.getId()).isEmpty());
    }
}