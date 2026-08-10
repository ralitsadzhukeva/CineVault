package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.entities.Watchlist;
import bg.softuni.cinevault.exception.AccessDeniedException;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
import bg.softuni.cinevault.exception.user.UserNotFoundException;
import bg.softuni.cinevault.exception.watchlist.WatchlistEntryNotFoundException;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.repository.WatchlistRepository;
import bg.softuni.cinevault.service.WatchlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class WatchlistServiceImpl implements WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public void addMovie(UUID movieId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException(movieId));
        if (watchlistRepository.existsByUserAndMovie(user, movie)) {
            return;
        }
        Watchlist watchlist = Watchlist.builder()
                .user(user)
                .movie(movie)
                .watched(false)
                .addedOn(LocalDate.now())
                .build();
        log.info("User {} added movie {} to watchlist.",
                user.getUsername(),
                movie.getTitle());
        watchlistRepository.save(watchlist);
    }

    @Override
    public List<Watchlist> getUserWatchlist(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));
        return watchlistRepository.findByUser(user);
    }

    @Override
    public void removeMovie(UUID watchlistId, UUID userId) {
        Watchlist entry = watchlistRepository.findById(watchlistId)
                .orElseThrow(()-> new WatchlistEntryNotFoundException(watchlistId.toString()));

        if (!entry.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        log.info("User {} removed movie {} from watchlist.",
                entry.getUser().getUsername(),
                entry.getMovie().getTitle());

        watchlistRepository.delete(entry);
    }

    @Override
    public void markAsWatched(UUID watchlistId, UUID userId) {
        Watchlist entry = watchlistRepository.findById(watchlistId)
                .orElseThrow(()-> new WatchlistEntryNotFoundException(watchlistId.toString()));

        if (!entry.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }

        entry.setWatched(true);

        log.info("User {} marked movie {} as watched.",
                entry.getUser().getUsername(),
                entry.getMovie().getTitle());

        watchlistRepository.save(entry);
    }

    @Override
    public void deleteAllByMovie(UUID movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new MovieNotFoundException(movieId));

        log.info("Deleted all watchlist entries for movie {}.", movie.getTitle());

        watchlistRepository.deleteByMovie(movie);
    }
}
