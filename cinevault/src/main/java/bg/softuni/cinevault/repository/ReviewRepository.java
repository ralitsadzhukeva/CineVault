package bg.softuni.cinevault.repository;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.entities.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByMovie(Movie movie);
    boolean existsByMovieAndUser(Movie movie, User user);
    List<Review> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
    void deleteByMovieId(UUID movieId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Double getAverageRating(@Param("movieId") UUID movieId);
}
