package app.services;

import app.dtos.MovieDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private final FetchTools fetchTools;

    public MovieService(FetchTools fetchTools) {
        this.fetchTools = fetchTools;
    }

    public List<MovieDTO.Movie> getRecentDanishMoviesInfo()  {
        List<MovieDTO.Movie> movies = new ArrayList<>();
        List<String> endpoints = new ArrayList<>();

        MovieDTO movieDTO = fetchTools.getFromApi(discoverMovieUri(1), MovieDTO.class);
        int totalPages = movieDTO.getTotalPages();

        for (int i = 1; i <= totalPages; i++) {
            endpoints.add(discoverMovieUri(i));
        }

        List<MovieDTO> movieDTOs = fetchTools.getFromApiList(endpoints, MovieDTO.class);
        movieDTOs.forEach(x -> movies.addAll(x.getResults()));

        return movies;
    }

    private String discoverMovieUri(int page) {
        LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);
        return "https://api.themoviedb.org/3/discover/movie" +
                "?include_adult=true" +
                "&include_video=false" +
                "&page=" + page +
                "&primary_release_date.gte=" + fiveYearsAgo +
                "&sort_by=popularity.desc" +
                "&with_original_language=da" +
                "&api_key=" + System.getenv("API_KEY");
    }
}
