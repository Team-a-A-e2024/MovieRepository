package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
public class Movie {
    @Id
    private Integer id;
    private String imdbID;
    private Double rating;
    private LocalDate releaseDate;
    private String title;
    @Column(length=1024)
    private String overview;

    //relations
    @OneToMany (mappedBy = "movie", fetch = FetchType.EAGER)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<MovieCast> casts = new HashSet<>();

    @ManyToMany (fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    //bi-directional functions

    //cast

    public void addCast (MovieCast cast){
        casts.add(cast);
        if(cast.getMovie() != null){
            cast.getMovie().removeCast(cast);
        }
        cast.setMovie(this);
    }

    public void removeCast (MovieCast cast){
        if(casts.remove(cast)){
            cast.setMovie(null);
        }
    }

    //genre

    public void addGenre (Genre genre){
        if(genres.add(genre)){
            genre.addmovie(this);
        }
    }

    public void removeGenre (Genre genre){
        if(genres.remove(genre)){
            genre.removeMovie(this);
        }
    }
}
