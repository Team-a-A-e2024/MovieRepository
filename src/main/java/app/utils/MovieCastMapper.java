package app.utils;

import app.dtos.CastDTO;
import app.dtos.CreditsDTO;
import app.dtos.CrewDTO;
import app.entities.Movie;
import app.entities.MovieCast;
import app.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class MovieCastMapper {

    public static List<MovieCast> castDTOtoMovieCastEntity(List<CastDTO> castDTOs, Movie movie) {
        List<MovieCast> movieCasts = new ArrayList<>();

        for (CastDTO cast : castDTOs) {
            Person person = Person.builder()
                    .id(cast.getId())
                    .name(cast.getName())
                    .build();

            movieCasts.add(MovieCast.builder()
                    .character(cast.getCharacter())
                    .movie(movie)
                    .person(person)
                    .build()
            );
        }

        return movieCasts;
    }

    public static List<MovieCast> mapCrewDTOsToMovieCast(List<CrewDTO> crewDTOs, Movie movie) {
        List<MovieCast> movieCasts = new ArrayList<>();

        for (CrewDTO crew : crewDTOs) {
            Person person = Person.builder()
                    .id(crew.getId())
                    .name(crew.getName())
                    .build();

            movieCasts.add(MovieCast.builder()
                    .job(crew.getJob())
                    .department(crew.getDepartment())
                    .movie(movie)
                    .person(person)
                    .build());
        }

        return movieCasts;
    }

    public static List<MovieCast> creditsDTOToMovieCastEntity(CreditsDTO creditsDTO, List<Movie> movies){
        List<MovieCast> movieCasts = new ArrayList<>();

        for(Movie movie : movies){
            if(movie.getId()==creditsDTO.getId()){
                movieCasts.addAll(castDTOtoMovieCastEntity(creditsDTO.getCast(),movie));
                movieCasts.addAll(mapCrewDTOsToMovieCast(creditsDTO.getCrew(), movie));
                break;
            }
        }

        return movieCasts;
    }

    public static List<MovieCast> creditsDTOListToMovieCastList(List<CreditsDTO> creditsDTOs, List<Movie> movies) {
        List<MovieCast> movieCasts = new ArrayList<>();

        creditsDTOs.forEach(credits -> {
            movieCasts.addAll(creditsDTOToMovieCastEntity(credits, movies));
        });

        return movieCasts;
    }
}