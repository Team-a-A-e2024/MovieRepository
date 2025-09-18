package app.daos;

import app.config.HibernateConfig;
import app.entities.Movie;
import app.entities.MovieCast;
import app.entities.Person;
import app.populators.MovieCastPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieCastDAOTest {

    private EntityManagerFactory emf;
    private MovieCastDAO dao;

    private MovieDAO movieDao;
    private PersonDAO personDao;
    private Movie movie;
    private Person person;

    @BeforeEach
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new MovieCastDAO(emf);

        movieDao = new MovieDAO(emf);
        personDao = new PersonDAO(emf);

        movie = movieDao.create(Movie.builder().title("The Matrix").build());
        person = personDao.create(Person.builder().id(1).name("Keanu Reeves").build());
    }

    @AfterEach
    void teardown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    // Bruges kun til createAll-testen, så vi ikke kalder populatoren to gange
    private List<MovieCast> buildCasts() {
        return List.of(
                MovieCast.builder().Character("Neo").build(),
                MovieCast.builder().Character("Narrator").build(),
                MovieCast.builder().Character("Cobb").build()
        );
    }

    @Test
    void createMovieCast() {
        // Arrange + Act
        List<MovieCast> movieCasts = MovieCastPopulator.populateMovieCasts(dao);

        // Assert
        assertFalse(movieCasts.isEmpty());
        assertEquals("Neo", movieCasts.get(0).getCharacter());
    }

    @Test
    void createAllMovieCast() {
        // Arrange (brug buildCasts her)
        List<MovieCast> toCreate = buildCasts();

        // Act
        List<MovieCast> created = dao.createAll(toCreate);

        // Assert
        assertEquals(3, created.size());
        assertTrue(created.stream().anyMatch(c -> "Neo".equals(c.getCharacter())));
        assertTrue(created.stream().anyMatch(c -> "Narrator".equals(c.getCharacter())));
        assertTrue(created.stream().anyMatch(c -> "Cobb".equals(c.getCharacter())));

        // mod DB
        assertEquals(3, dao.getAll().size());
    }

    @Test
    void getMovieCastById() {
        // Arrange
        MovieCast mc = MovieCastPopulator.populateMovieCasts(dao).get(0);

        // Act
        Optional<MovieCast> found = dao.getById(mc.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Neo", found.get().getCharacter());
    }

    @Test
    void getAllMovieCasts() {
        // Arrange
        MovieCastPopulator.populateMovieCasts(dao);

        // Act
        List<MovieCast> all = dao.getAll();

        // Assert
        assertTrue(all.size() >= 3);
        assertTrue(all.stream().anyMatch(c -> "Neo".equals(c.getCharacter())));
        assertTrue(all.stream().anyMatch(c -> "Narrator".equals(c.getCharacter())));
        assertTrue(all.stream().anyMatch(c -> "Cobb".equals(c.getCharacter())));
    }

    @Test
    void updateMovieCast() {
        // Arrange
        MovieCast mc = MovieCastPopulator.populateMovieCasts(dao).get(0);

        // Act
        MovieCast updated = dao.update(
                MovieCast.builder()
                        .id(mc.getId())
                        .Character("Thomas Anderson")
                        .build()
        );

        // Assert
        assertEquals("Thomas Anderson", updated.getCharacter());
    }

    @Test
    void deleteMovieCast() {
        // Arrange
        MovieCast mc = MovieCastPopulator.populateMovieCasts(dao).get(0);

        // Act
        boolean deleted = dao.delete(mc.getId());

        // Assert
        assertTrue(deleted);
        assertTrue(dao.getById(mc.getId()).isEmpty());
    }
}