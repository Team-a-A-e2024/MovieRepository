package app.persistence;

import app.dtos.CastDTO;
import app.entities.Movie;
import app.entities.MovieCast;
import app.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class MovieCastMapper {

    public static List<MovieCast> CastDTOtoMovieCastEntityMapper(List<CastDTO> castDTOs, Movie movie, List<Person> persons) {
        List<MovieCast> movieCasts = new ArrayList<>();

        for (CastDTO cast : castDTOs) {
            Person person = null;
            for (Person p : persons) {
                if (p.getId().equals(cast.getId())) {
                    person = p;
                    break;
                }
            }
            if (person != null) {
                movieCasts.add(MovieCast.builder()
                        .job(cast.getJob())
                        .department(cast.getDepartment())
                        .character(cast.getCharacter())
                        .movie(movie)
                        .person(person)
                        .build()
                );
            }
        }
        return movieCasts;
    }
}