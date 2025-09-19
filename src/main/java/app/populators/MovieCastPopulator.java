package app.populators;

import app.daos.MovieCastDAO;
import app.entities.MovieCast;
import app.entities.Movie;
import app.entities.Person;

import java.util.List;

public class MovieCastPopulator {

    public static List<MovieCast> populateMovieCasts(MovieCastDAO castDAO) {

        MovieCast c1 = MovieCast.builder()
                .character("Neo")
                .build();
        MovieCast c2 = MovieCast.builder()
                .character("Narrator")
                .build();
        MovieCast c3 = MovieCast.builder()
                .character("Cobb")
                .build();

        castDAO.create(c1);
        castDAO.create(c2);
        castDAO.create(c3);

        return List.of(c1, c2, c3);
    }

    public static List<MovieCast> buildCasts() {
        return List.of(
                MovieCast.builder().character("Neo").build(),
                MovieCast.builder().character("Narrator").build(),
                MovieCast.builder().character("Cobb").build()
        );
    }

}