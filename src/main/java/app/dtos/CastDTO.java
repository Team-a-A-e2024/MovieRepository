package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CastDTO {
    @JsonProperty("id")
    private int id;
    @JsonProperty("job")
    private String job;
    @JsonProperty("department")
    private String department;
    @JsonProperty("character")
    private String character;
    @JsonProperty("name")
    private String name;
}
