package bg.softuni.cinevault.repository;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.entities.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface WatchlistRepository extends JpaRepository <Watchlist, UUID>{
    List<Watchlist> findByUser(User user);
    boolean existsByUserAndMovie(User user, Movie movie);
    void deleteByUserId(UUID userId);
    void deleteByMovie(Movie movie);
}
