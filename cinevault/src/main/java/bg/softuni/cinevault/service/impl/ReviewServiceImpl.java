package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.ReviewRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }
    @Override
    public void addReview(UUID movieId, UUID userId, Integer rating, String comment) {
        User user = userRepository.findById(userId).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        if (reviewRepository.existsByMovieAndUser(movie, user)){
            return;
        }
        Review review = Review.builder()
                .user(user)
                .movie(movie)
                .rating(rating)
                .comment(comment)
                .build();
        reviewRepository.save(review);
    }

    @Override
    public List<Review> getMovieReviews(UUID movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        return reviewRepository.findByMovie(movie);
    }

    @Override
    public List<Review> getUserReviews(UUID id) {
        return reviewRepository.findByUserId(id);
    }

    @Override
    @Transactional
    public void deleteReviewsByMovieId(UUID movieId) {
        reviewRepository.deleteByMovieId(movieId);
    }
}
