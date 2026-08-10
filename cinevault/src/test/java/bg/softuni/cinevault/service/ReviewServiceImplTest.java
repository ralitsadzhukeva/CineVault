package bg.softuni.cinevault.service;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.exception.AccessDeniedException;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
import bg.softuni.cinevault.exception.review.DuplicateReviewException;
import bg.softuni.cinevault.exception.review.ReviewNotFoundException;
import bg.softuni.cinevault.exception.user.UserNotFoundException;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.ReviewRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.service.impl.ReviewServiceImpl;
import bg.softuni.cinevault.service.recommendation.RecommendationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private RecommendationService recommendationService;


    private Review review;
    private Movie movie;
    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .role(Role.USER)
                .build();

        movie = Movie.builder()
                .id(UUID.randomUUID())
                .title("Interstellar")
                .genre(Genre.SCIFI)
                .build();

        review = Review.builder()
                .id(UUID.randomUUID())
                .rating(9)
                .comment("Almost perfect")
                .user(user)
                .movie(movie)
                .build();
    }

    @Test
    @Transactional
    void addReview_shouldCreateReviewAndGenerateRecommendations() {

        UUID movieId = movie.getId();
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));

        when(reviewRepository.existsByMovieAndUser(movie, user))
                .thenReturn(false);

        reviewService.addReview(
                movieId,
                userId,
                10,
                "great movie"
        );

        verify(reviewRepository)
                .save(any(Review.class));

        verify(recommendationService)
                .generateRecommendations(userId);
    }

    @Test
    void addReview_shouldThrowException_whenMovieDoesNotExist() {
        UUID movieId = movie.getId();
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> reviewService.addReview(
                        movieId,
                        userId,
                        10,
                        "great movie"));

        verify(userRepository).findById(userId);
        verify(movieRepository).findById(movieId);
    }

    @Test
    void addReview_shouldThrowException_whenUserDoesNotExist() {
        UUID movieId = movie.getId();
        UUID userId = user.getId();
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
       assertThrows(UserNotFoundException.class, () -> reviewService.addReview(
               movieId,
               userId,
               10,
               "great movie"
               ));
       verify(userRepository).findById(userId);
    }

    @Test
    void addReview_shouldThrowException_whenUserAlreadyReviewedMovie() {
        UUID movieId = movie.getId();
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));
        when(reviewRepository.existsByMovieAndUser(movie, user))
                .thenReturn(true);
        assertThrows(DuplicateReviewException.class, () -> reviewService.addReview(
                movieId,
                userId,
                10,
                "great movie"
        ));
        verify(userRepository).findById(userId);
        verify(movieRepository).findById(movieId);
        verify(reviewRepository).existsByMovieAndUser(movie, user);
    }

    @Test
    void getMovieReviews_shouldReturnReviews(){
        when(movieRepository.findById(movie.getId()))
                .thenReturn(Optional.of(movie));

        when(reviewRepository.findByMovie(movie))
                .thenReturn(List.of(review));

        var result = reviewService.getMovieReviews(movie.getId());

        assertNotNull(result);
        verify(movieRepository).findById(movie.getId());
        verify(reviewRepository).findByMovie(movie);
    }
    @Test
    void getMovieReviews_shouldThrowException_whenMovieDoesNotExist() {
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> reviewService.getMovieReviews(movieId)
        );

        verify(movieRepository).findById(movieId);
        verify(reviewRepository, never()).findByMovie(any(Movie.class));
    }

    @Test
    void getUserReviews_shouldReturnReviews(){
        when(reviewRepository.findByUserId(user.getId()))
                .thenReturn(List.of(review));
        var result = reviewService.getUserReviews(user.getId());
        assertNotNull(result);
        verify(reviewRepository).findByUserId(user.getId());
    }
    @Test
    void deleteReviewsByMovieId_shouldDeleteReviews() {
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));

        reviewService.deleteReviewsByMovieId(movieId);

        verify(movieRepository).findById(movieId);
        verify(reviewRepository).deleteByMovieId(movieId);
    }

    @Test
    void deleteReviewsByMovieId_shouldThrowException_whenMovieDoesNotExist() {
        UUID movieId = movie.getId();
        when(movieRepository.findById(movieId))
                .thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> reviewService.deleteReviewsByMovieId(movieId));
        verify(movieRepository).findById(movieId);
    }
    @Test
    void getAverageRating_shouldReturnAverageRating() {
        UUID movieId = movie.getId();

        when(reviewRepository.getAverageRating(movieId))
                .thenReturn(8.5);

        double result = reviewService.getAverageRating(movieId);

        assertEquals(8.5, result);

        verify(reviewRepository).getAverageRating(movieId);
    }
    @Test
    @Transactional
    void deleteReview_shouldDeleteReview() {
        UUID reviewId = review.getId();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        reviewService.deleteReview(reviewId, user);

        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void deleteReview_shouldThrowException_whenReviewDoesNotExist() {
        UUID reviewId = review.getId();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(reviewId, user));
        verify(reviewRepository).findById(reviewId);
    }

    @Test
    @Transactional
    void deleteReview_shouldThrowException_whenUserDoesNotHavePermission() {
        UUID reviewId = review.getId();
        User differentUser = User.builder()
                .id(UUID.randomUUID())
                .username("differentUser")
                .role(Role.USER)
                .build();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));
        assertThrows(AccessDeniedException.class, () -> reviewService.deleteReview(reviewId, differentUser));

        verify(reviewRepository).findById(reviewId);
    }

    @Test
    @Transactional
    void editReview_shouldEditReview() {
        UUID reviewId = review.getId();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        reviewService.editReview(
                reviewId,
                user,
                10,
                "Amazing movie!"
        );

        assertEquals(10, review.getRating());
        assertEquals("Amazing movie!", review.getComment());

        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).save(review);
    }

    @Test
    void editReview_shouldThrowException_whenReviewDoesNotExist() {
        UUID reviewId = review.getId();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());
        assertThrows(ReviewNotFoundException.class, () -> reviewService.editReview(
                reviewId,
                user,
                10,
                "Amazing movie!"
        ));
        verify(reviewRepository).findById(reviewId);
    }

    @Test
    @Transactional
    void editReview_shouldThrowException_whenUserDoesNotHavePermission() {
        UUID reviewId = review.getId();
        User differentUser = User.builder()
                .id(UUID.randomUUID())
                .username("differentUser")
                .role(Role.USER)
                .build();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));
        assertThrows(AccessDeniedException.class, () -> reviewService.editReview(
                reviewId,
                differentUser,
                 10,
                "Amazing movie!"));
        verify(reviewRepository).findById(reviewId);
    }
    @Test
    void findById_shouldReturnReview() {
        UUID reviewId = review.getId();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));
        var result = reviewService.findById(reviewId);
        assertNotNull(result);
    }
    @Test
    void findById_shouldThrowException_whenReviewDoesNotExist() {
        UUID reviewId = UUID.randomUUID();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());
        assertThrows(ReviewNotFoundException.class, () -> reviewService.findById(reviewId));
    }
    @Test
    void findByIdForEdit_shouldReturnReview_whenUserOwnsReview() {
        UUID reviewId = review.getId();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        Review result = reviewService.findByIdForEdit(reviewId, user);

        assertNotNull(result);
        assertEquals(review, result);

        verify(reviewRepository).findById(reviewId);
    }

    @Test
    void findByIdForEdit_shouldThrowException_whenReviewDoesNotExist() {
        UUID reviewId = UUID.randomUUID();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());

        assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.findByIdForEdit(reviewId, user)
        );

        verify(reviewRepository).findById(reviewId);
    }

    @Test
    void findByIdForEdit_shouldThrowException_whenUserNotPermitted() {
        UUID reviewId = review.getId();

        User differentUser = User.builder()
                .id(UUID.randomUUID())
                .username("differentUser")
                .role(Role.USER)
                .build();

        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        assertThrows(
                AccessDeniedException.class,
                () -> reviewService.findByIdForEdit(reviewId, differentUser)
        );

        verify(reviewRepository).findById(reviewId);
    }
}
