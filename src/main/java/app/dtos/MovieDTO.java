package app.dtos;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class MovieDTO {
    @JsonProperty("id")
    private int id;
    @JsonProperty("imdb_id")
    private String imdbId;
    @JsonProperty("vote_average")
    private double rating;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    @JsonProperty("title")
    private String title;
    @JsonProperty("overview")
    private String overview;

    private List<GenreDTO> genres;
    private List<CastDTO> cast;
    private List<CreditsDTO> credits;
}
