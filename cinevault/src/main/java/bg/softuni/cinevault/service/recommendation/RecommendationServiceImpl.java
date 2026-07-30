package bg.softuni.cinevault.service.recommendation;

import bg.softuni.cinevault.dto.recommendation.*;
import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.repository.ReviewRepository;
import bg.softuni.cinevault.service.MovieService;
import bg.softuni.cinevault.service.ReviewService;
import bg.softuni.cinevault.service.recommendation.client.RecommendationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RecommendationServiceImpl implements RecommendationService{
    private final RecommendationClient recommendationClient;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public RecommendationServiceImpl(RecommendationClient recommendationClient,
                                     MovieRepository movieRepository, ReviewRepository reviewRepository) {
        this.recommendationClient = recommendationClient;
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;

    }
    @CacheEvict(value = "recommendations", key = "#userId", beforeInvocation = false)
    @Override
    public void generateRecommendations(UUID userId) {
        List<MoviePreferenceDto> watchedMovies = reviewRepository.findByUserId(userId)
                .stream()
                .map(review -> MoviePreferenceDto.builder()
                        .movieId(review.getMovie().getId())
                        .genre(review.getMovie().getGenre())
                        .rating(review.getRating())
                        .build())
                .toList();

        List<MovieDto> allMovies = movieRepository.findAll()
                .stream()
                .map(movie -> MovieDto.builder()
                        .movieId(movie.getId())
                        .genre(movie.getGenre())
                        .averageRating(reviewRepository.getAverageRating(movie.getId()))
                        .build())
                .toList();

        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .userId(userId)
                .watchedMovies(watchedMovies)
                .allMovies(allMovies)
                .build();

        recommendationClient.generateRecommendations(request);

        log.info("Recommendations generated successfully for user {}. Watched movies: {}, Available movies: {}",
                userId,
                watchedMovies.size(),
                allMovies.size());
    }

    @Cacheable(value = "recommendations", key = "#userId")
    @Override
    public List<RecommendationViewDto> getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations =
                recommendationClient.getRecommendations(userId);

        if (recommendations == null || recommendations.isEmpty()) {
            log.warn("No recommendations found for user {}.", userId);

            return List.of();
        }

        return recommendations.stream()
                .map(recommendationDto -> {

                    Movie movie = movieRepository.findById(recommendationDto.getMovieId())
                            .orElseThrow(()-> new MovieNotFoundException(recommendationDto.getMovieId()));

                    return RecommendationViewDto.builder()
                            .movieId(movie.getId())
                            .title(movie.getTitle())
                            .posterUrl(movie.getPosterUrl())
                            .genre(movie.getGenre())
                            .averageRating(reviewRepository.getAverageRating(movie.getId()))
                            .reason(recommendationDto.getReason())
                            .score(recommendationDto.getScore())
                            .build();

                })
                .toList();
    }
    @CacheEvict(value = "recommendations", key = "#userId")
    @Override
    public void deleteRecommendations(UUID userId) {
        recommendationClient.deleteRecommendations(userId);

        log.info("Deleted all recommendations for user {}.", userId);
    }
}
