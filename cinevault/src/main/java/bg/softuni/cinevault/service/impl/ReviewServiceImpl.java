package bg.softuni.cinevault.service.impl;

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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
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
            throw new DuplicateReviewException();
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

    @Override
    public double getAverageRating(UUID movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        List<Review> reviews = reviewRepository.findByMovie(movie);

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }

    @Override
    public void deleteReview(UUID id, User currentUser) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);


        if (!review.getUser().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You are not allowed to delete this review");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public void editReview(UUID id, User currentUser, Integer rating, String comment){
        Review review = reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);

        if (!review.getUser().getId().equals(currentUser.getId())
        && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You are not allowed to edit this review");
        }

        review.setRating(rating);
        review.setComment(comment);
        reviewRepository.save(review);
    }

    @Override
    public Review findById(UUID id) {
        return reviewRepository.findById(id).orElseThrow();
    }
}
