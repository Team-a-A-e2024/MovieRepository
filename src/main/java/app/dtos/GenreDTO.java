package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenreDTO {
    List<Genres> genres;


    public static class Genres {
        @JsonProperty("id")
        private int id;
        @JsonProperty("name")
        private String name;
    }
}
