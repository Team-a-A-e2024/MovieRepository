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
public class Person {
    @Id
    private Integer id;
    private String name;

    //relations
    @OneToMany (mappedBy = "person", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<MovieCast> casts = new HashSet<>();

    //bi-directional functions

    //cast

    public void addCast (MovieCast cast){
        casts.add(cast);
        if(cast.getPerson() != null){
            cast.getPerson().removeCast(cast);
        }
        cast.setPerson(this);
    }

    public void removeCast (MovieCast cast){
        if(casts.remove(cast)){
            cast.setPerson(null);
        }
    }
}
