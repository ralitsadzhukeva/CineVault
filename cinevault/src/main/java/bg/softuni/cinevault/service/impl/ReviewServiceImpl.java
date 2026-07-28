package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.exception.AccessDeniedException;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
import bg.softuni.cinevault.exception.review.DuplicateReviewException;
import bg.softuni.cinevault.exception.review.ReviewNotFoundException;
import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.ReviewRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.service.ReviewService;
import bg.softuni.cinevault.service.recommendation.RecommendationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final RecommendationService recommendationService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository, RecommendationService recommendationService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.recommendationService = recommendationService;
    }
    @Override
    public void addReview(UUID movieId, UUID userId, Integer rating, String comment) {
        User user = userRepository.findById(userId).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        if (reviewRepository.existsByMovieAndUser(movie, user)) {

            log.warn("User {} attempted to add duplicate review for movie {}.",
                    user.getUsername(),
                    movie.getTitle());

            throw new DuplicateReviewException();
        }
        Review review = Review.builder()
                .user(user)
                .movie(movie)
                .rating(rating)
                .comment(comment)
                .build();

        log.info("User {} added review for movie {}.", user.getUsername(), movie.getTitle());

        reviewRepository.save(review);
        recommendationService.generateRecommendations(user.getId());
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
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new MovieNotFoundException(movieId));

        reviewRepository.deleteByMovieId(movieId);

        log.info("Deleted all reviews for movie {}.", movie.getTitle());

        reviewRepository.deleteByMovieId(movieId);
    }

    @Override
    public double getAverageRating(UUID movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new MovieNotFoundException(movieId));

        List<Review> reviews = reviewRepository.findByMovie(movie);

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }

    @Override
    public void deleteReview(UUID id, User currentUser) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new ReviewNotFoundException(id));


        if (!review.getUser().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            log.warn("User {} attempted to modify review {} without permission.",
                    currentUser.getUsername(),
                    id);

            throw new AccessDeniedException();
        }

        log.info("User {} deleted review {} for movie {}.",
                currentUser.getUsername(),
                id,
                review.getMovie().getTitle());

        reviewRepository.deleteById(id);
    }

    @Override
    public void editReview(UUID id, User currentUser, Integer rating, String comment){
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new ReviewNotFoundException(id));

        if (!review.getUser().getId().equals(currentUser.getId())
        && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException();
        }

        review.setRating(rating);
        review.setComment(comment);

        log.info("User {} edited review {} for movie {}.",
                currentUser.getUsername(),
                id,
                review.getMovie().getTitle());

        reviewRepository.save(review);
    }

    @Override
    public Review findById(UUID id) {
        return reviewRepository.findById(id).orElseThrow();
    }
}
