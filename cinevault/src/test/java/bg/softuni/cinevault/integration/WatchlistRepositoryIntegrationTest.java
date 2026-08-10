package bg.softuni.cinevault.integration;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.entities.Watchlist;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.repository.WatchlistRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WatchlistRepositoryIntegrationTest {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    void shouldFindWatchlistByUser() {

        User user = User.builder()
                .username("watcher_" + UUID.randomUUID())
                .firstName("Test")
                .lastName("User")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Movie movie = Movie.builder()
                .title("Notting Hill")
                .director("Roger Michell")
                .releaseYear(1999)
                .description("Romantic comedy")
                .posterUrl("poster.jpg")
                .genre(Genre.ROMANCE)
                .build();

        movie = movieRepository.save(movie);

        Watchlist watchlist = Watchlist.builder()
                .user(user)
                .movie(movie)
                .watched(false)
                .addedOn(LocalDate.now())
                .build();

        watchlistRepository.save(watchlist);

        List<Watchlist> result =
                watchlistRepository.findByUser(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(movie.getId(), result.get(0).getMovie().getId());
        assertFalse(result.get(0).getWatched());
    }

    @Test
    @Transactional
    void shouldCheckIfMovieIsAlreadyInWatchlist() {

        User user = User.builder()
                .username("watcher_" + UUID.randomUUID())
                .firstName("Test")
                .lastName("User")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Movie movie = Movie.builder()
                .title("Titanic")
                .director("James Cameron")
                .releaseYear(1997)
                .description("Romance")
                .posterUrl("poster.jpg")
                .genre(Genre.ROMANCE)
                .build();

        movie = movieRepository.save(movie);

        watchlistRepository.save(Watchlist.builder()
                        .user(user)
                        .movie(movie)
                        .watched(false)
                        .addedOn(LocalDate.now())
                        .build()
        );

        boolean exists = watchlistRepository.existsByUserAndMovie(user, movie);

        assertTrue(exists);
    }

    @Test
    @Transactional
    void shouldDeleteWatchlistEntriesByMovie() {

        User user = User.builder()
                .username("watcher_" + UUID.randomUUID())
                .firstName("Test")
                .lastName("User")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Movie movie = Movie.builder()
                .title("Pride and Prejudice")
                .director("Joe Wright")
                .releaseYear(2005)
                .description("Romance")
                .posterUrl("poster.jpg")
                .genre(Genre.ROMANCE)
                .build();

        movie = movieRepository.save(movie);

        Watchlist watchlist = Watchlist.builder()
                .user(user)
                .movie(movie)
                .watched(false)
                .addedOn(LocalDate.now())
                .build();

        watchlistRepository.save(watchlist);

        assertTrue(watchlistRepository.existsByUserAndMovie(user, movie));

        watchlistRepository.deleteByMovie(movie);

        assertFalse(watchlistRepository.existsByUserAndMovie(user, movie));
    }
}