package bg.softuni.cinevault.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable=false)
    private String title;
    @Column(nullable=false)
    private String director;
    @Column(nullable=false)
    private String genre;
    @Column(name="release_year", nullable=false)
    private Integer releaseYear;
    @Column
    private String description;
    @Column(name="poster_url")
    private String posterUrl;
}
