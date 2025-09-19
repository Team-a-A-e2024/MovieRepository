package app.services;

import app.dtos.CreditsDTO;
import app.dtos.MovieDTO;

import java.util.ArrayList;
import java.util.List;

public class CreditsService {
    private final FetchTools fetchTools;

    public CreditsService(FetchTools fetchTools) {
        this.fetchTools = fetchTools;
    }

    public CreditsDTO getCreditsInfoByMovieId(int movieId) {
        return fetchTools.getFromApi(creditsUri(movieId), CreditsDTO.class);
    }

    public List<CreditsDTO> getAllCreditsInfo(List<MovieDTO.Movie> movies) {
        List<String> endpoints = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            endpoints.add(creditsUri(movies.get(i).getId()));
        }

        return fetchTools.getFromApiList(endpoints, CreditsDTO.class);
    }

    private String creditsUri(int movieId) {
        return "https://api.themoviedb.org/3/movie/" + movieId + "/credits" +
                "?language=en-US" +
                "&api_key=" + System.getenv("API_KEY");
    }
}
