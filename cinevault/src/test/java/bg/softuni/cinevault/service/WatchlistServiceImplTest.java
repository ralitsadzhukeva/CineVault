package bg.softuni.cinevault.service;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.entities.Watchlist;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
import bg.softuni.cinevault.exception.user.UserNotFoundException;
import bg.softuni.cinevault.exception.watchlist.WatchlistEntryNotFoundException;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.repository.WatchlistRepository;
import bg.softuni.cinevault.service.impl.WatchlistServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceImplTest {
    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private WatchlistServiceImpl watchlistService;

    private Watchlist watchlist;
    private Movie movie;
    private User user;

    @BeforeEach
    void setUp(){

        movie = Movie.builder()
                .id(UUID.randomUUID())
                .title("Notting Hill")
                .genre(Genre.ROMANCE)
                .build();

        user = User.builder()
                .id(UUID.randomUUID())
                .username("lanita")
                .firstName("Lana")
                .lastName("Del Rey")
                .email("lanita@gmail.com")
                .password("123456")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        watchlist = Watchlist.builder()
                .id(UUID.randomUUID())
                .watched(false)
                .addedOn(LocalDate.now())
                .user(user)
                .movie(movie)
                .build();
    }

    @Test
    @Transactional
    void addMovie_shouldAddMovieToWatchlist(){
        UUID movieId = movie.getId();
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));

        when(watchlistRepository.existsByUserAndMovie(user,movie))
                .thenReturn(false);

        watchlistService.addMovie(movieId,userId);

        verify(userRepository).findById(userId);
        verify(movieRepository).findById(movieId);
        verify(watchlistRepository)
                .existsByUserAndMovie(user,movie);

        verify(watchlistRepository)
                .save(any(Watchlist.class));
    }

    @Test
    void addToWatchlist_shouldThrowException_whenUserDoesNotExist() {

        UUID movieId = movie.getId();
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> watchlistService.addMovie(movieId, userId)
        );

        verify(userRepository)
                .findById(userId);

        verify(movieRepository, never())
                .findById(movieId);

        verify(watchlistRepository, never())
                .save(any(Watchlist.class));
    }

    @Test
    void addToWatchlist_shouldThrowException_whenMovieDoesNotExist() {

        UUID movieId = movie.getId();
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> watchlistService.addMovie(movieId, userId)
        );

        verify(userRepository)
                .findById(userId);

        verify(movieRepository)
                .findById(movieId);

        verify(watchlistRepository, never())
                .save(any(Watchlist.class));
    }

    @Test
    @Transactional
    void addToWatchlist_shouldNotAddMovie_whenMovieIsAlreadyInWatchlist() {

        UUID movieId = movie.getId();
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));

        when(watchlistRepository.existsByUserAndMovie(user, movie))
                .thenReturn(true);

        watchlistService.addMovie(movieId, userId);

        verify(userRepository)
                .findById(userId);

        verify(movieRepository)
                .findById(movieId);

        verify(watchlistRepository)
                .existsByUserAndMovie(user, movie);

        verify(watchlistRepository, never())
                .save(any(Watchlist.class));
    }
    @Test
    @Transactional
    void getUserWatchlist_shouldReturnUserWatchlist(){
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        watchlistService.getUserWatchlist(userId);

        verify(userRepository).findById(userId);
    }

    @Test
    void getUserWatchlist_shouldReturnNull_whenUserDoesNotExist(){
        UUID userId = user.getId();
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->watchlistService.getUserWatchlist(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    @Transactional
    void removeMovieFromWatchlist_shouldRemoveMovieFromWatchlist(){
        UUID watchlistId = watchlist.getId();
        UUID userId = user.getId();

        when(watchlistRepository.findById(watchlistId))
                .thenReturn(Optional.of(watchlist));

        watchlistService.removeMovie(watchlistId,userId);

        verify(watchlistRepository).findById(watchlistId);
        verify(watchlistRepository).delete(watchlist);
    }

    @Test
    void removeMovieFromWatchlist_shouldThrowAnException_whenWatchlistDoesNotExist(){
        UUID watchlistId = watchlist.getId();
        UUID userId = user.getId();

        when(watchlistRepository.findById(watchlistId))
                .thenReturn(Optional.empty());

        assertThrows(WatchlistEntryNotFoundException.class,()->watchlistService.removeMovie(watchlistId,userId));

        verify(watchlistRepository).findById(watchlistId);
        verify(watchlistRepository,never()).delete(any(Watchlist.class));
    }

    @Test
    @Transactional
    void markAsWatched_shouldMarkMovieAsWatched(){
        UUID watchlistId = watchlist.getId();
        UUID userId = user.getId();

        when(watchlistRepository.findById(watchlistId))
                .thenReturn(Optional.of(watchlist));

        watchlistService.markAsWatched(watchlistId,userId);

        assertTrue(watchlist.getWatched());

        verify(watchlistRepository).findById(watchlistId);
        verify(watchlistRepository).save(watchlist);
    }

    @Test
    void markAsWatched_shouldThrowAnException_whenWatchlistDoesNotExist(){
        UUID watchlistId = watchlist.getId();
        UUID userId = user.getId();

        when(watchlistRepository.findById(watchlistId))
                .thenReturn(Optional.empty());

        assertThrows(WatchlistEntryNotFoundException.class,()->watchlistService.markAsWatched(watchlistId,userId));

        verify(watchlistRepository).findById(watchlistId);

        verify(watchlistRepository,never()).save(any(Watchlist.class));
    }

    @Test
    @Transactional
    void deleteAllByMovie_shouldDeleteAllWatchlistEntries(){
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));

        watchlistService.deleteAllByMovie(movieId);

        verify(movieRepository).findById(movieId);
        verify(watchlistRepository).deleteByMovie(movie);
    }

    @Test
    void deleteAllByMovie_shouldThrowAnException_whenMovieDoesNotExist(){
        UUID movieId = movie.getId();
        when(movieRepository.findById(movieId))
                .thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class,()->watchlistService.deleteAllByMovie(movieId));
        verify(movieRepository).findById(movieId);
        verify(watchlistRepository,never()).deleteByMovie(any(Movie.class));
    }
}
