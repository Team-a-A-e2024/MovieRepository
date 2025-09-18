package app.populators;

import app.daos.MovieCastDAO;
import app.entities.MovieCast;
import app.entities.Movie;
import app.entities.Person;

import java.util.List;

public class MovieCastPopulator {

    public static List<MovieCast> populateMovieCasts(MovieCastDAO castDAO) {

        MovieCast c1 = MovieCast.builder()
                .Character("Neo")
                .build();
        MovieCast c2 = MovieCast.builder()
                .Character("Narrator")
                .build();
        MovieCast c3 = MovieCast.builder()
                .Character("Cobb")
                .build();

        castDAO.create(c1);
        castDAO.create(c2);
        castDAO.create(c3);

        return List.of(c1, c2, c3);
    }
}