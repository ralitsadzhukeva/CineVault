package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.dto.movie.MovieAddDto;
import bg.softuni.cinevault.dto.movie.MovieEditDto;
import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
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
                .orElseThrow(()->new MovieNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteMovie(UUID id) {
        watchlistService.deleteAllByMovie(id);
        reviewService.deleteReviewsByMovieId(id);
        movieRepository.deleteById(id);
    }

    @Override
    public MovieEditDto getMovieForEdit(UUID id) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        MovieEditDto movieEditDto = new MovieEditDto();

        movieEditDto.setTitle(movie.getTitle());
        movieEditDto.setDirector(movie.getDirector());
        movieEditDto.setGenre(movie.getGenre());
        movieEditDto.setReleaseYear(movie.getReleaseYear());
        movieEditDto.setDescription(movie.getDescription());
        movieEditDto.setPosterUrl(movie.getPosterUrl());

        return movieEditDto;
    }

    @Override
    public void updateMovie(UUID id, MovieEditDto movieEditDto) {
        Movie movie = movieRepository.findById(id).orElseThrow();

        movie.setTitle(movieEditDto.getTitle());
        movie.setDirector(movieEditDto.getDirector());
        movie.setGenre(movieEditDto.getGenre());
        movie.setReleaseYear(movieEditDto.getReleaseYear());
        movie.setDescription(movieEditDto.getDescription());
        movie.setPosterUrl(movieEditDto.getPosterUrl());

        movieRepository.save(movie);
    }

    @Override
    public List<Movie> searchMovies(String keyword) {
        if (keyword == null && keyword.isBlank()) {
            return movieRepository.findAll();
        }
        return movieRepository.findByTitleContainingIgnoreCase(keyword.trim());
    }
}
