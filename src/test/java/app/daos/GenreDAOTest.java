package app.daos;

import app.config.HibernateConfig;
import app.entities.Genre;
import app.populators.GenrePopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")

class GenreDAOTest {

    private static EntityManagerFactory emf;
    private static GenreDAO dao;

    @BeforeEach
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new GenreDAO(emf);
    }

    @AfterEach
    void teardown() {
        if (emf != null && emf.isOpen()) emf.close();
    }


    @Test
    void createGenre() {
        // Arrange
        Genre expected = Genre.builder()
                .id(100)
                .genre("Action")
                .build();

        // Act
        Genre actual = dao.create(expected);

        // Assert
        assertEquals(expected, actual);

        // Assert – hentet fra DB matcher
        Genre fromDb = dao.getById(100).orElseThrow();
        assertEquals(expected, fromDb);
    }

    @Test
    void createAllGenres() {
        // Arrange
        Genre expected1 = Genre.builder().id(201).genre("Action").build();
        Genre expected2 = Genre.builder().id(202).genre("Comedy").build();
        Genre expected3 = Genre.builder().id(203).genre("Drama").build();
        List<Genre> expected = List.of(expected1, expected2, expected3);

        // Act
        List<Genre> actual = dao.createAll(expected);

        // Assert
        assertEquals(expected, actual);
        assertEquals(dao.getAll(), actual);
    }

    @Test
    void getGenreById() {
        // Arrange
        GenrePopulator.populateGenres(dao);
        Genre expected = Genre.builder()
                .id(1)
                .genre("Action")
                .build();

        // Act
        Genre actual = dao.getById(1).get();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllGenres() {
        // Arrange
        GenrePopulator.populateGenres(dao);
        List<Genre> expected = List.of(
                Genre.builder().id(1).genre("Action").build(),
                Genre.builder().id(2).genre("Comedy").build(),
                Genre.builder().id(3).genre("Drama").build()
        );

        // Act
        List<Genre> actual = dao.getAll();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void updateGenre() {
        // Arrange
        Genre g = GenrePopulator.populateGenres(dao).get(0);
        Genre expected = Genre.builder()
                .id(g.getId())
                .genre("Adventure")
                .build();

        // Act
        Genre actual = dao.update(expected);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void deleteGenre() {
        // Arrange
        Genre g = GenrePopulator.populateGenres(dao).get(0);
        boolean expected = true;

        // Act
        boolean actual = dao.delete(g.getId());

        // Assert
        assertEquals(expected, actual);
        assertTrue(dao.getById(g.getId()).isEmpty());
    }
}