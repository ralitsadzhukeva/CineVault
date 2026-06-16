package bg.softuni.cinevault.service;


import bg.softuni.cinevault.entities.Watchlist;

import java.util.List;
import java.util.UUID;

public interface WatchlistService {
    void addMovie(UUID movieId, UUID userId);
    List<Watchlist> getUserWatchlist(UUID userId);
    void removeMovie(UUID watchlistId);
    void markAsWatched(UUID watchlistId);
}
