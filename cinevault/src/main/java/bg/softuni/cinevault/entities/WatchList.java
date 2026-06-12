package bg.softuni.cinevault.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "watch_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private Boolean watched;
    @Column(nullable=false)
    private LocalDate addedOn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;
}
