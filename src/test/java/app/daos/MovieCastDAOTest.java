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

import static app.populators.MovieCastPopulator.buildCasts;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
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

    @Test
    void createMovieCast() {
        // Arrange
        MovieCast expected = MovieCast.builder()
                .character("Neo")
                .build();

        // Act
        MovieCast actual = dao.create(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void createAllMovieCast() {
        // Arrange
        List<MovieCast> expected = buildCasts();

        // Act
        List<MovieCast> actual = dao.createAll(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getMovieCastById() {
        // Arrange
        MovieCast inserted = MovieCastPopulator.populateMovieCasts(dao).get(0);
        MovieCast expected = MovieCast.builder()
                .id(inserted.getId())
                .character(inserted.getCharacter())
                .build();

        // Act
        MovieCast actual = dao.getById(inserted.getId()).get();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllMovieCasts() {
        // Arrange
        List<MovieCast> expected = MovieCastPopulator.populateMovieCasts(dao);

        // Act
        List<MovieCast> actual = dao.getAll();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void updateMovieCast() {
        // Arrange
        MovieCast mc = MovieCastPopulator.populateMovieCasts(dao).get(0);
        MovieCast expected = MovieCast.builder()
                .id(mc.getId())
                .character("Thomas Anderson")
                .build();

        // Act
        MovieCast actual = dao.update(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void deleteMovieCast() {
        // Arrange
        MovieCast mc = MovieCastPopulator.populateMovieCasts(dao).get(0);
        boolean expected = true;

        // Act
        boolean actual = dao.delete(mc.getId());

        // Assert
        assertEquals(expected, actual);
        assertEquals(Optional.empty(), dao.getById(mc.getId()));
    }
}