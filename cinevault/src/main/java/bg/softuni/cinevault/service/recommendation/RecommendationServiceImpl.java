package bg.softuni.cinevault.service.recommendation;

import bg.softuni.cinevault.dto.recommendation.MovieDto;
import bg.softuni.cinevault.dto.recommendation.MoviePreferenceDto;
import bg.softuni.cinevault.dto.recommendation.RecommendationRequestDto;
import bg.softuni.cinevault.service.MovieService;
import bg.softuni.cinevault.service.ReviewService;
import bg.softuni.cinevault.service.recommendation.client.RecommendationClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService{
    private final RecommendationClient recommendationClient;
    private final MovieService movieService;
    private final ReviewService reviewService;

    public RecommendationServiceImpl(RecommendationClient recommendationClient,
                                     MovieService movieService, ReviewService reviewService) {
        this.recommendationClient = recommendationClient;
        this.movieService = movieService;
        this.reviewService = reviewService;
    }

    @Override
    public void generateRecommendations(UUID userId) {
        List<MoviePreferenceDto> watchedMovies = reviewService.getUserReviews(userId)
                .stream()
                .map(review -> MoviePreferenceDto.builder()
                        .movieId(review.getMovie().getId())
                        .genre(review.getMovie().getGenre())
                        .rating(review.getRating())
                        .build())
                .toList();

        List<MovieDto> allMovies = movieService.findAll()
                .stream()
                .map(movie -> MovieDto.builder()
                        .movieId(movie.getId())
                        .genre(movie.getGenre())
                        .averageRating(reviewService.getAverageRating(movie.getId()))
                        .build())
                .toList();

        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .userId(userId)
                .watchedMovies(watchedMovies)
                .allMovies(allMovies)
                .build();

        recommendationClient.generateRecommendations(request);
    }
}
