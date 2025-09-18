package app.populators;

import app.daos.MovieDAO;
import app.entities.Movie;

import java.time.LocalDate;
import java.util.List;

public class MoviePopulator {

    public static List<Movie> populateMovies(MovieDAO movieDAO) {
        Movie m1 = Movie.builder()
                .id(100)
                .title("The Matrix")
                .releaseDate(LocalDate.of(2000,1,1))
                .build();
        Movie m2 = Movie.builder()
                .id(101)
                .title("Fight Club")
                .releaseDate(LocalDate.of(2010,1,1))
                .build();
        Movie m3 = Movie.builder()
                .id(102)
                .title("Inception")
                .releaseDate(LocalDate.of(2020,1,1))
                .build();

        movieDAO.create(m1);
        movieDAO.create(m2);
        movieDAO.create(m3);

        return List.of(m1, m2, m3);
    }
}