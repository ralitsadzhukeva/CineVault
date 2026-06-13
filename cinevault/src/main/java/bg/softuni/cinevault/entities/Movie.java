package bg.softuni.cinevault.entities;

import bg.softuni.cinevault.enums.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @Column(name="release_year", nullable=false)
    @Min(1888)
    @Max(2026)
    private Integer releaseYear;
    @Column
    private String description;
    @Column(name="poster_url")
    private String posterUrl;

}
