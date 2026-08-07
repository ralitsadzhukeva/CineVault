package bg.softuni.cinevault.integration;

import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.ReviewRepository;
import bg.softuni.cinevault.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryIntegrationTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindReviewByMovie() {

        Movie movie = Movie.builder()
                .title("Notting Hill")
                .director("Roger Michell")
                .releaseYear(1999)
                .description("Romantic comedy")
                .posterUrl("poster.jpg")
                .genre(Genre.ROMANCE)
                .build();

        movie = movieRepository.save(movie);

        User user = User.builder()
                .username("reviewer_" + UUID.randomUUID())
                .firstName("Lana")
                .lastName("Del Rey")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Review review = Review.builder()
                .rating(10)
                .comment("Amazing movie!")
                .movie(movie)
                .user(user)
                .build();

        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findByMovie(movie);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(10, reviews.get(0).getRating());
        assertEquals("Amazing movie!", reviews.get(0).getComment());
    }

    @Test
    void shouldCheckIfReviewExistsForMovieAndUser() {

        Movie movie = Movie.builder()
                .title("Pretty Woman")
                .director("Garry Marshall")
                .releaseYear(1990)
                .description("Romantic comedy")
                .posterUrl("poster.jpg")
                .genre(Genre.ROMANCE)
                .build();

        movie = movieRepository.save(movie);

        User user = User.builder()
                .username("user_" + UUID.randomUUID())
                .firstName("Test")
                .lastName("User")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Review review = Review.builder()
                .rating(9)
                .comment("Great!")
                .movie(movie)
                .user(user)
                .build();

        reviewRepository.save(review);

        boolean exists = reviewRepository.existsByMovieAndUser(movie, user);

        assertTrue(exists);
    }

    @Test
    void shouldFindReviewsByUserId() {

        Movie movie = Movie.builder()
                .title("La La Land")
                .director("Damien Chazelle")
                .releaseYear(2016)
                .description("Musical romance")
                .posterUrl("poster.jpg")
                .genre(Genre.ROMANCE)
                .build();

        movie = movieRepository.save(movie);

        User user = User.builder()
                .username("reviewer_" + UUID.randomUUID())
                .firstName("Test")
                .lastName("User")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        Review review = Review.builder()
                .rating(8)
                .comment("Very good!")
                .movie(movie)
                .user(user)
                .build();

        reviewRepository.save(review);

        List<Review> reviews =
                reviewRepository.findByUserId(user.getId());

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(8, reviews.get(0).getRating());
    }


    @Test
    void shouldCalculateAverageRating() {

        Movie movie = Movie.builder()
                .title("The Notebook")
                .director("Nick Cassavetes")
                .releaseYear(2004)
                .description("Romantic drama")
                .posterUrl("poster.jpg")
                .genre(Genre.ROMANCE)
                .build();

        movie = movieRepository.save(movie);

        User user1 = User.builder()
                .username("user1_" + UUID.randomUUID())
                .firstName("User")
                .lastName("One")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user1 = userRepository.save(user1);

        User user2 = User.builder()
                .username("user2_" + UUID.randomUUID())
                .firstName("User")
                .lastName("Two")
                .email(UUID.randomUUID() + "@gmail.com")
                .password("password")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        user2 = userRepository.save(user2);

        reviewRepository.save(
                Review.builder()
                        .rating(10)
                        .comment("Perfect")
                        .movie(movie)
                        .user(user1)
                        .build()
        );

        reviewRepository.save(
                Review.builder()
                        .rating(8)
                        .comment("Very good")
                        .movie(movie)
                        .user(user2)
                        .build()
        );

        Double average =
                reviewRepository.getAverageRating(movie.getId());

        assertNotNull(average);
        assertEquals(9.0, average);
    }
}