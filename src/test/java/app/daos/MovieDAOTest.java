package app.daos;

import app.config.HibernateConfig;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.MovieCast;
import app.entities.Person;
import app.populators.MoviePopulator;
import app.populators.MoviePopulatorTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
class MovieDAOTest {

    private static EntityManagerFactory emf;
    private MovieDAO dao;

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
        dao = new MovieDAO(emf);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("TRUNCATE TABLE moviecast, movie, person, genre RESTART IDENTITY CASCADE")
                .executeUpdate();
        em.getTransaction().commit();
        MoviePopulatorTest.populate(em);
        em.close();
    }

    @Test
    void createMovie() {
        // Arrange
        Movie expected = Movie.builder()
                .id(100)
                .title("The Matrix")
                .build();

        // Act
        Movie actual = dao.create(expected);

        // Assert
        assertEquals(expected, actual);

        // Assert – hentet fra DB matcher
        Movie fromDb = dao.getById(actual.getId()).orElseThrow();
        assertEquals(expected, fromDb);
    }

    @Test
    void createAllMovies() {
        // Arrange
        List<Movie> expected = List.of(Movie.builder()
                        .id(115)
                        .title("Vaiana")
                        .build(),

                Movie.builder()
                        .id(116)
                        .title("Paw Patrol")
                        .build());

        // Act
        List<Movie> actual = dao.createAll(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getMovieById() {
        // Arrange
        List<Movie> seeded = MoviePopulator.populateMovies(dao);
        Movie expected = seeded.get(0);

        // Act
        Movie actual = dao.getById(expected.getId()).get();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllMovies() {
        // Arrange
        List<Movie> expected = List.of(MoviePopulatorTest.matrix, MoviePopulatorTest.inception, MoviePopulatorTest.room, MoviePopulatorTest.frozen);

        // Act
        List<Movie> actual = dao.getAll();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void updateMovie() {
        // Arrange
        Movie original = MoviePopulator.populateMovies(dao).get(0);
        Movie expected = Movie.builder()
                .id(original.getId())
                .title(original.getTitle() + " (Updated)")
                .build();

        // Act
        Movie actual = dao.update(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void deleteMovie() {
        // Arrange
        Movie m = MoviePopulator.populateMovies(dao).get(0);
        boolean expected = true;

        // Act
        boolean actual = dao.delete(m.getId());

        // Assert
        assertEquals(expected, actual);
        assertEquals(Optional.empty(), dao.getById(m.getId()));
    }

    @Test
    void searchByTitle() {
        List<Movie> results = dao.searchByTitle("The Matrix");

        assertNotNull(results);
        assertEquals(1, results.size());

        Movie movie = results.get(0);
        assertEquals(MoviePopulatorTest.matrix, movie);
    }

    @Test
    void testGetAverageRating() {
        double avg = dao.getAverageRating();
        double expected = (MoviePopulatorTest.matrix.getRating()
                + MoviePopulatorTest.inception.getRating()
                + MoviePopulatorTest.room.getRating()
                + MoviePopulatorTest.frozen.getRating()) / 4.0;

        assertEquals(expected, avg, 0.001);
    }

    @Test
    void testTopRatedMovies() {
        List<Movie> results = dao.getTop10HighestRated();

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
    void testLowestRatedMovies() {
        List<Movie> results = dao.getTop10LowestRated();

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

    @Test
    void getAllByGenre() {
        //arrange
        Genre genre = Genre.builder().genre("test").id(1).build();
        List<Movie> expected = List.of(
                Movie.builder().id(2001).title("test1").build(),
                Movie.builder().id(2002).title("test2").build()
        );
        expected.forEach(movie -> movie.addGenre(genre));
        GenreDAO genreDAO = new GenreDAO(emf);
        genreDAO.create(genre);
        expected.forEach(dao::create);

        //act
        List<Movie> result = dao.getAllByGenre(Genre.builder().genre("test").id(1).build());

        //assert
        assertEquals(expected,result);
    }

    @Test
    void getAllByPerson() {
        //arrange
        Person person = Person.builder().id(2000).build();
        List<Movie> movies = List.of(
                Movie.builder().id(2001).title("test1").build(),
                Movie.builder().id(2002).title("test2").build()
        );
        //even if they are in the same movie in multiple roles the result will only have the movie appear once ;)
        List<MovieCast> cast = List.of(
                MovieCast.builder().movie(movies.get(0)).person(person).build(),
                MovieCast.builder().movie(movies.get(1)).person(person).build(),
                MovieCast.builder().movie(movies.get(1)).person(person).build()
        );
        PersonDAO personDAO = new PersonDAO(emf);
        MovieCastDAO movieCastDAO = new MovieCastDAO(emf);

        personDAO.create(person);
        movies.forEach(dao::create);
        cast.forEach(movieCastDAO::create);

        List<Movie> expected = new ArrayList<>(movies);

        //act
        List<Movie> actual = dao.getAllByPerson(person);

        //assert
        assertEquals(expected,actual);
    }
}