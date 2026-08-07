package bg.softuni.cinevault.service;

import bg.softuni.cinevault.dto.recommendation.*;
import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.ReviewRepository;
import bg.softuni.cinevault.service.recommendation.RecommendationServiceImpl;
import bg.softuni.cinevault.service.recommendation.client.RecommendationClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceImplTest {

    @Mock
    private RecommendationClient recommendationClient;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    private Movie movie;
    private Review review;

    @BeforeEach
    void setUp() {

        movie = Movie.builder()
                .id(UUID.randomUUID())
                .title("Notting Hill")
                .genre(Genre.ROMANCE)
                .build();

        review = Review.builder()
                .rating(10)
                .movie(movie)
                .build();
    }

    @Test
    void generateRecommendations_shouldBuildRequestAndCallFeignClient() {

        UUID userId = UUID.randomUUID();


        when(reviewRepository.findByUserId(userId))
                .thenReturn(List.of(review));


        when(movieRepository.findAll())
                .thenReturn(List.of(movie));


        when(reviewRepository.getAverageRating(movie.getId()))
                .thenReturn(9.5);


        recommendationService.generateRecommendations(userId);


        verify(reviewRepository)
                .findByUserId(userId);

        verify(movieRepository)
                .findAll();

        verify(reviewRepository)
                .getAverageRating(movie.getId());


        ArgumentCaptor<RecommendationRequestDto> captor =
                ArgumentCaptor.forClass(RecommendationRequestDto.class);

        verify(recommendationClient)
                .generateRecommendations(captor.capture());

        RecommendationRequestDto request = captor.getValue();


        assertEquals(userId, request.getUserId());


        assertEquals(1, request.getWatchedMovies().size());

        MoviePreferenceDto watchedMovie =
                request.getWatchedMovies().get(0);

        assertEquals(
                movie.getId(),
                watchedMovie.getMovieId()
        );

        assertEquals(
                Genre.ROMANCE,
                watchedMovie.getGenre()
        );

        assertEquals(
                10,
                watchedMovie.getRating()
        );


        assertEquals(1, request.getAllMovies().size());

        MovieDto allMovie =
                request.getAllMovies().get(0);

        assertEquals(
                movie.getId(),
                allMovie.getMovieId()
        );

        assertEquals(
                Genre.ROMANCE,
                allMovie.getGenre()
        );

        assertEquals(
                9.5,
                allMovie.getAverageRating()
        );
    }
    @Test
    void getRecommendations_shouldMapRecommendationToViewDto() {

        UUID userId = UUID.randomUUID();

        UUID movieId = UUID.randomUUID();

        Movie recommendedMovie = Movie.builder()
                .id(movieId)
                .title("Interstellar")
                .genre(Genre.SCIFI)
                .posterUrl("poster.jpg")
                .build();

        RecommendationDto recommendation = RecommendationDto.builder()
                .movieId(movieId)
                .reason("Because you enjoy Sci-Fi movies.")
                .score(95)
                .build();

        when(recommendationClient.getRecommendations(userId))
                .thenReturn(List.of(recommendation));

        when(movieRepository.findById(movieId))
                .thenReturn(java.util.Optional.of(recommendedMovie));

        when(reviewRepository.getAverageRating(movieId))
                .thenReturn(9.2);

        List<RecommendationViewDto> result =
                recommendationService.getRecommendations(userId);

        assertEquals(1, result.size());

        RecommendationViewDto dto = result.get(0);

        assertEquals(movieId, dto.getMovieId());
        assertEquals("Interstellar", dto.getTitle());
        assertEquals(Genre.SCIFI, dto.getGenre());
        assertEquals("poster.jpg", dto.getPosterUrl());
        assertEquals(9.2, dto.getAverageRating());
        assertEquals("Because you enjoy Sci-Fi movies.", dto.getReason());
        assertEquals(95, dto.getScore());
    }
    @Test
    void getRecommendations_shouldThrowException_whenMovieDoesNotExist() {

        UUID userId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();

        RecommendationDto recommendation = RecommendationDto.builder()
                .movieId(movieId)
                .reason("Recommended for you")
                .score(90)
                .build();

        when(recommendationClient.getRecommendations(userId))
                .thenReturn(List.of(recommendation));

        when(movieRepository.findById(movieId))
                .thenReturn(java.util.Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                MovieNotFoundException.class,
                () -> recommendationService.getRecommendations(userId)
        );
    }
    @Test
    void deleteRecommendations_shouldCallRecommendationClient() {

        UUID userId = UUID.randomUUID();

        recommendationService.deleteRecommendations(userId);

        verify(recommendationClient)
                .deleteRecommendations(userId);
    }
    @Test
    void getRecommendations_shouldReturnEmptyList_whenNoRecommendationsExist() {

        UUID userId = UUID.randomUUID();

        when(recommendationClient.getRecommendations(userId))
                .thenReturn(List.of());

        List<RecommendationViewDto> result =
                recommendationService.getRecommendations(userId);

        assertEquals(0, result.size());

        verify(recommendationClient)
                .getRecommendations(userId);
    }
    @Test
    void generateRecommendations_shouldHandleUserWithNoReviews() {

        UUID userId = UUID.randomUUID();

        when(reviewRepository.findByUserId(userId))
                .thenReturn(List.of());

        when(movieRepository.findAll())
                .thenReturn(List.of(movie));

        when(reviewRepository.getAverageRating(movie.getId()))
                .thenReturn(8.0);

        recommendationService.generateRecommendations(userId);

        verify(recommendationClient)
                .generateRecommendations(any(RecommendationRequestDto.class));
    }
    @Test
    void getRecommendations_shouldReturnEmptyList_whenClientReturnsNull() {

        UUID userId = UUID.randomUUID();

        when(recommendationClient.getRecommendations(userId))
                .thenReturn(null);

        List<RecommendationViewDto> result =
                recommendationService.getRecommendations(userId);

        assertEquals(0, result.size());

        verify(recommendationClient)
                .getRecommendations(userId);
    }
    @Test
    void getRecommendations_shouldMapMultipleRecommendations() {

        UUID userId = UUID.randomUUID();

        UUID movieId1 = UUID.randomUUID();
        UUID movieId2 = UUID.randomUUID();

        Movie movie1 = Movie.builder()
                .id(movieId1)
                .title("Interstellar")
                .genre(Genre.SCIFI)
                .posterUrl("interstellar.jpg")
                .build();

        Movie movie2 = Movie.builder()
                .id(movieId2)
                .title("The Dark Knight")
                .genre(Genre.ACTION)
                .posterUrl("dark-knight.jpg")
                .build();

        RecommendationDto recommendation1 = RecommendationDto.builder()
                .movieId(movieId1)
                .reason("Because you enjoy Sci-Fi movies.")
                .score(95)
                .build();

        RecommendationDto recommendation2 = RecommendationDto.builder()
                .movieId(movieId2)
                .reason("Because you enjoy Action movies.")
                .score(88)
                .build();

        when(recommendationClient.getRecommendations(userId))
                .thenReturn(List.of(recommendation1, recommendation2));

        when(movieRepository.findById(movieId1))
                .thenReturn(java.util.Optional.of(movie1));

        when(movieRepository.findById(movieId2))
                .thenReturn(java.util.Optional.of(movie2));

        when(reviewRepository.getAverageRating(movieId1))
                .thenReturn(9.2);

        when(reviewRepository.getAverageRating(movieId2))
                .thenReturn(8.8);

        List<RecommendationViewDto> result =
                recommendationService.getRecommendations(userId);

        assertEquals(2, result.size());

        assertEquals("Interstellar", result.get(0).getTitle());
        assertEquals(95, result.get(0).getScore());

        assertEquals("The Dark Knight", result.get(1).getTitle());
        assertEquals(88, result.get(1).getScore());
    }
}