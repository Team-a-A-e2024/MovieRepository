package app.services;

import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.entities.Genre;

import java.util.Set;

public class GenreService {
    private final FetchTools fetchTools;

    public GenreService(FetchTools fetchTools) { this.fetchTools = fetchTools; }

    public GenreDTO getGenresInfo() {
        return fetchTools.getFromApi(genresUri(), GenreDTO.class);
    }

    private String genresUri() {
        return "https://api.themoviedb.org/3/genre/movie/list?language=en" +
                "&api_key=" + System.getenv("API_KEY");
    }

}
