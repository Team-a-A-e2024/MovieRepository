package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenreDTO {
    List<Genres> genres;

    @Data
    @ToString
    public static class Genres {
        @JsonProperty("id")
        private int id;
        @JsonProperty("name")
        private String name;
    }
}
