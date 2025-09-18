package app.populators;

import app.entities.Movie;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class MoviePopulatorTest {

    public static Movie matrix;
    public static Movie inception;
    public static Movie room;
    public static Movie frozen;

    public static void populate(EntityManager em) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Movie").executeUpdate();

        matrix = Movie.builder()
                .title("The Matrix")
                .imdbID("tt0133093")
                .rating(8.7)
                .releaseDate(LocalDate.of(1999, 3, 31))
                .overview("Sci-fi classic")
                .build();

        inception = Movie.builder()
                .title("Inception")
                .imdbID("tt1375666")
                .rating(8.5)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .overview("Dream within a dream")
                .build();

        room = Movie.builder()
                .title("The Room")
                .imdbID("tt0368226")
                .rating(3.7)
                .releaseDate(LocalDate.of(2003, 6, 27))
                .overview("So bad it's good")
                .build();

        frozen = Movie.builder()
                .title("Frozen")
                .imdbID("tt2294629")
                .rating(7.5)
                .releaseDate(LocalDate.of(2013, 11, 27))
                .overview("Disney hit")
                .build();

        em.persist(matrix);
        em.persist(inception);
        em.persist(room);
        em.persist(frozen);

        em.getTransaction().commit();
    }
}
