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

class GenreDAOTest {


    static List<Genre> buildGenres() {
        return List.of(
                Genre.builder().id(1).genre("Action").build(),
                Genre.builder().id(2).genre("Comedy").build(),
                Genre.builder().id(3).genre("Drama").build()
        );
    }

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
        // Arrange + Act
        List<Genre> genres = GenrePopulator.populateGenres(dao);

        // Assert
        assertFalse(genres.isEmpty());
        assertEquals("Action", genres.get(0).getGenre());
    }

    @Test
    void createAllGenre() {
        // Arrange
        List<Genre> toCreate = buildGenres();

        // Act
        List<Genre> created = dao.createAll(toCreate);

        // Assert (return-værdi)
        assertEquals(3, created.size());
        assertTrue(created.stream().anyMatch(g -> g.getGenre().equals("Action")));
        assertTrue(created.stream().anyMatch(g -> g.getGenre().equals("Comedy")));
        assertTrue(created.stream().anyMatch(g -> g.getGenre().equals("Drama")));

        // Assert (mod DB)
        List<Genre> all = dao.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void getGenreById() {
        // Arrange
        GenrePopulator.populateGenres(dao);

        // Act
        Optional<Genre> genre = dao.getById(1);

        // Assert
        assertTrue(genre.isPresent());
        assertEquals("Action", genre.get().getGenre());
    }

    @Test
    void getAllGenres() {
        // Arrange
        GenrePopulator.populateGenres(dao);

        // Act
        List<Genre> allGenres = dao.getAll();

        // Assert
        assertEquals(3, allGenres.size());
        assertTrue(allGenres.stream().anyMatch(g -> g.getGenre().equals("Action")));
        assertTrue(allGenres.stream().anyMatch(g -> g.getGenre().equals("Comedy")));
        assertTrue(allGenres.stream().anyMatch(g -> g.getGenre().equals("Drama")));
    }

    @Test
    void updateGenre() {
    // Arrange
    List<Genre> genres = GenrePopulator.populateGenres(dao);
    Genre g = genres.get(0);

    // Act
    Genre updated = dao.update(
            Genre.builder()
                    .id(g.getId())
                    .genre("Adventure")
                    .build()
    );

    // Assert
    assertEquals("Adventure", updated.getGenre());
}

    @Test
    void deleteGenre() {
        // Arrange
        List<Genre> genres = GenrePopulator.populateGenres(dao);
        Genre g = genres.get(0);

        // Act
        boolean deleted = dao.delete(g.getId());

        // Assert
        assertTrue(deleted);
        assertTrue(dao.getById(g.getId()).isEmpty());
    }

}