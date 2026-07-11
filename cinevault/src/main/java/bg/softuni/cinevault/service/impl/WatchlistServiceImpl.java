package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.entities.Watchlist;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.repository.WatchlistRepository;
import bg.softuni.cinevault.service.WatchlistService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
                .orElseThrow();
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow();
        if (watchlistRepository.existsByUserAndMovie(user, movie)) {
            return;
        }
        Watchlist watchlist = Watchlist.builder()
                .user(user)
                .movie(movie)
                .watched(false)
                .addedOn(LocalDate.now())
                .build();
        watchlistRepository.save(watchlist);
    }

    @Override
    public List<Watchlist> getUserWatchlist(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        return watchlistRepository.findByUser(user);
    }

    @Override
    public void removeMovie(UUID watchlistId) {
        Watchlist entry = watchlistRepository.findById(watchlistId)
                .orElseThrow(()-> new RuntimeException("Watchlist entry not found: " + watchlistId) );

        watchlistRepository.delete(entry);
    }

    @Override
    public void markAsWatched(UUID watchlistId) {
        Watchlist entry = watchlistRepository.findById(watchlistId)
                .orElseThrow();

        entry.setWatched(true);

        watchlistRepository.save(entry);
    }

    @Override
    public void deleteAllByMovie(UUID movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow();

        watchlistRepository.deleteByMovie(movie);
    }
}
