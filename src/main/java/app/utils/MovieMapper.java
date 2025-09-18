package app.utils;

import app.dtos.MovieDTO;
import app.entities.Genre;
import app.entities.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieMapper {

    public static List<Movie> mapMovieDTOtoMovieEntity(MovieDTO movieDTO, List<Genre> genres){
        List<Movie> movies = new ArrayList<>();

        for(MovieDTO.Movie movie : movieDTO.getResults()){
            Set<Genre> resultGenres = new HashSet<>();
            for(Integer id : movie.getGenres()){
                for(Genre genre : genres){
                    if(genre.getId().equals(id)){
                        resultGenres.add(genre);
                        break;
                    }
                }
            }

            movies.add(Movie.builder().
                    id(movie.getId()).
                    rating(movie.getRating()).
                    releaseDate(movie.getReleaseDate()).
                    title(movie.getTitle()).
                    overview(movie.getOverview()).
                    genres(resultGenres).
                    build()
            );
        }
        return movies;
    }
}
