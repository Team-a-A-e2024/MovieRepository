package app.populators;

import app.daos.GenreDAO;
import app.entities.Genre;

import java.util.List;

public class GenrePopulator {

    public static List<Genre> populateGenres(GenreDAO genreDAO) {
        Genre g1 = Genre.builder()
                .id(1)
                .genre("Action")
                .build();
        Genre g2 = Genre.builder()
                .id(2)
                .genre("Comedy")
                .build();
        Genre g3 = Genre.builder()
                .id(3)
                .genre("Drama")
                .build();

        genreDAO.create(g1);
        genreDAO.create(g2);
        genreDAO.create(g3);

        return List.of(g1, g2, g3);
    }

    public static List<Genre> buildGenres() {
        return List.of(
                Genre.builder().id(1).genre("Action").build(),
                Genre.builder().id(2).genre("Comedy").build(),
                Genre.builder().id(3).genre("Drama").build()
        );
    }
}