package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.dto.movie.MovieAddDto;
import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.service.MovieService;
import bg.softuni.cinevault.service.ReviewService;
import bg.softuni.cinevault.service.WatchlistService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ReviewService reviewService;
    private final WatchlistService watchlistService;

    public MovieServiceImpl(MovieRepository movieRepository, ReviewService reviewService, WatchlistService watchlistService) {
        this.movieRepository = movieRepository;
        this.reviewService = reviewService;
        this.watchlistService = watchlistService;
    }

    @Override
    public Movie add(MovieAddDto movieAddDto) {
        Movie movie =Movie.builder()
                .title(movieAddDto.getTitle())
                .director(movieAddDto.getDirector())
                .genre(movieAddDto.getGenre())
                .releaseYear(movieAddDto.getReleaseYear())
                .description(movieAddDto.getDescription())
                .posterUrl(movieAddDto.getPosterUrl())
                .build();
        
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(UUID id) {
        return movieRepository
                .findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Movie not found"));
    }

    @Override
    @Transactional
    public void deleteMovie(UUID id) {
        watchlistService.deleteAllByMovie(id);
        reviewService.deleteReviewsByMovieId(id);
        movieRepository.deleteById(id);
    }
}
