package app.services;

import app.dtos.CreditsDTO;

public class CreditsService {
    private final FetchTools fetchTools;

    public CreditsService(FetchTools fetchTools) {
        this.fetchTools = fetchTools;
    }

    public CreditsDTO getCreditsInfoByMovieId(int movieId) {
        return fetchTools.getFromApi(creditsUri(movieId), CreditsDTO.class);
    }

    private String creditsUri(int movieId) {
        return "https://api.themoviedb.org/3/movie/" + movieId + "/credits" +
                "?language=en-US" +
                "&api_key=" + System.getenv("API_KEY");
    }
}
