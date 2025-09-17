package app.dtos;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MovieDTO {
    @JsonProperty("page")
    private int page;
    @JsonProperty("results")
    private List<Movie> results;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_results")
    private int totalResults;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Movie {
        @JsonProperty("id")
        private int id;
        @JsonProperty("vote_average")
        private double rating;
        @JsonProperty("release_date")
        private LocalDate releaseDate;
        @JsonProperty("original_title")
        private String title;
        @JsonProperty("overview")
        private String overview;
        @JsonProperty("genre_ids")
        private List<Integer> genres;
    }
}
