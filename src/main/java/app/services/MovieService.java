package app.services;

import app.dtos.MovieDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MovieService {
    private final FetchTools fetchTools;

    public MovieService(FetchTools fetchTools) {
        this.fetchTools = fetchTools;
    }

    public List<MovieDTO.Movie> getRecentDanishMoviesInfo()  {
        List<MovieDTO.Movie> movies = new ArrayList<>();
        MovieDTO movieDTO = fetchTools.getFromApi(discoverMovieUri(1), MovieDTO.class);
        int totalPages = movieDTO.getTotalPages();

        int cores = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = Math.min(totalPages, cores);
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        List<Callable<MovieDTO>> tasks = new ArrayList<>();

        for (int i = 1; i <= totalPages; i++) {
            int finalI = i;
            tasks.add(() -> fetchTools.getFromApi(discoverMovieUri(finalI), MovieDTO.class));
        }

        try {
            List<Future<MovieDTO>> futures = executorService.invokeAll(tasks);

            for (Future<MovieDTO> future : futures) {
                MovieDTO response = future.get();
                movies.addAll(response.getResults());
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

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
