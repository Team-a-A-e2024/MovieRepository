package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity

public class Genre {
    @Id
    private Integer id;
    private String genre;

    //relation
    @ManyToMany (mappedBy = "genres")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Movie> movies = new HashSet<>();

    //bi-directional functions

    //movie

    public void addmovie (Movie movie){
        if(movies.add(movie)){
            movie.addGenre(this);
        }
    }

    public void removeMovie(Movie movie){
        if(movies.remove(movie)){
            movie.removeGenre(this);
        }
    }
}