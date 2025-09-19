package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
public class MovieCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String job;
    private String department;
    private String character;

    //relation
    @ManyToOne
    @Setter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Movie movie;

    @ManyToOne
    //@JoinColumn(referencedColumnName = "externalid")
    @Setter
    private Person person;
}
