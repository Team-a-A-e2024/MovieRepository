package app.utils;

import app.dtos.CastDTO;
import app.dtos.CreditsDTO;
import app.entities.Movie;
import app.entities.MovieCast;
import app.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class MovieCastMapper {

    public static List<MovieCast> CastDTOtoMovieCastEntity(List<CastDTO> castDTOs, Movie movie) {
        List<MovieCast> movieCasts = new ArrayList<>();

        for (CastDTO cast : castDTOs) {
            Person person = Person.builder()
                    .id(cast.getId())
                    .name(cast.getName())
                    .build();

            movieCasts.add(MovieCast.builder()
                    .job(cast.getJob())
                    .department(cast.getDepartment())
                    .character(cast.getCharacter())
                    .movie(movie)
                    .person(person)
                    .build()
            );
        }

        return movieCasts;
    }
    public static List<MovieCast> CreditsDTOToMovieCastEntity(CreditsDTO creditsDTO, List<Movie> movies){
        Movie resultMovie = null;
        for(Movie movie : movies){
            if(movie.getId()==creditsDTO.getId()){
                resultMovie = movie;
                break;
            }
        }
        return CastDTOtoMovieCastEntity(creditsDTO.getCast(),resultMovie);
    }
}