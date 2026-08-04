package bg.softuni.cinevault.service;

import bg.softuni.cinevault.dto.movie.MovieAddDto;
import bg.softuni.cinevault.dto.movie.MovieEditDto;
import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.exception.movie.MovieNotFoundException;
import bg.softuni.cinevault.repository.MovieRepository;
import bg.softuni.cinevault.service.impl.MovieServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewService reviewService;

    @Mock
    private WatchlistService watchlistService;

    @InjectMocks
    private MovieServiceImpl movieService;


    private Movie movie;

    @BeforeEach
    void setUp() {

        movie = Movie.builder()
                .id(UUID.randomUUID())
                .title("Notting Hill")
                .genre(Genre.ROMANCE)
                .build();
    }

    @Test
    void findById_shouldReturnMovie() {
        UUID movieId =  movie.getId();

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.of(movie));

        Movie result = movieService.findById(movieId);

        assertNotNull(result);
        assertEquals(movieId, result.getId());
        assertEquals(movie.getTitle(), result.getTitle());

        verify(movieRepository).findById(movieId);
    }

    @Test
    void findById_shouldThrowException_whenMovieDoesNotExist() {
        UUID movieId =  UUID.randomUUID();

        when(movieRepository.findById(movieId))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> movieService.findById(movieId)
        );

        verify(movieRepository).findById(movieId);
    }

    @Test
    void add_shouldCreateAndSaveMovie() {

        MovieAddDto movieAddDto = MovieAddDto.builder()
                .title("Interstellar")
                .director("Cristopher Nolan")
                .genre(Genre.SCIFI)
                .releaseYear(2014)
                .description("Sci fi movie.")
                .posterUrl("poster.jpg")
                .build();

        when(movieRepository.save(any(Movie.class)))
                .thenReturn(movie);

        Movie result = movieService.add(movieAddDto);

        assertNotNull(result);

        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void findAll_shouldReturnAllMovies(){
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        var result = movieService.findAll();

        assertEquals(1, result.size());
        assertEquals(movie.getTitle(), result.get(0).getTitle());

        verify(movieRepository).findAll();
    }

    @Test
    void searchMovies_shouldFindMoviesByKeyword(){
        when(movieRepository.findByTitleContainingIgnoreCase("hill"))
                .thenReturn(List.of(movie));

        var result = movieService.searchMovies("hill");

        assertEquals(1, result.size());

        verify(movieRepository).findByTitleContainingIgnoreCase("hill");
    }

    @Test
    void searchMovies_shouldReturnAllMovies_whenKeywordIsBlank(){
        when(movieRepository.findAll())
                .thenReturn(List.of(movie));

        var result = movieService.searchMovies(" ");

        assertEquals(1, result.size());

        verify(movieRepository).findAll();
    }

    @Test
    void searchMovies_shouldReturnAllMovies_whenKeywordIsNull() {

        when(movieRepository.findAll())
                .thenReturn(java.util.List.of(movie));

        var result = movieService.searchMovies(null);

        assertEquals(1, result.size());

        verify(movieRepository).findAll();
    }

    @Test
    void getMovieForEdit_shouldReturnMovieForEditDto() {
        when(movieRepository.findById(movie.getId()))
                .thenReturn(Optional.of(movie));

        movie.setDirector("Cristopher Nolan");
        movie.setGenre(Genre.SCIFI);
        movie.setReleaseYear(2014);
        movie.setDescription("Sci fi movie.");
        movie.setPosterUrl("poster.jpg");

        when(movieRepository.findById(movie.getId()))
                .thenReturn(Optional.of(movie));

        MovieEditDto result = movieService.getMovieForEdit(movie.getId());

        assertNotNull(result);
        assertEquals(movie.getTitle(), result.getTitle());
        assertEquals(movie.getDirector(), result.getDirector());
        assertEquals(movie.getGenre(), result.getGenre());
        assertEquals(movie.getReleaseYear(), result.getReleaseYear());
        assertEquals(movie.getDescription(), result.getDescription());
        assertEquals(movie.getPosterUrl(), result.getPosterUrl());

        verify(movieRepository).findById(movie.getId());
    }

    @Test
    void getMovieForEdit_shouldThrowException_whenMovieDoesNotExist() {
        UUID movieId = UUID.randomUUID();
        when(movieRepository.findById(movieId))
                .thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieForEdit(movieId));
        verify(movieRepository).findById(movieId);
    }

    @Test
    void updateMovie_shouldUpdateMovie() {
        UUID movieId = movie.getId();

        MovieEditDto dto = new MovieEditDto();

        dto.setTitle("Interstellar");
        dto.setDirector("Cristopher Nolan");
        dto.setGenre(Genre.SCIFI);
        dto.setReleaseYear(2014);
        dto.setDescription("Sci-fi movie.");
        dto.setPosterUrl("new-poster.jpg");

        when(movieRepository.findById(movieId))
            .thenReturn(Optional.of(movie));

        movieService.updateMovie(movieId, dto);

        assertEquals("Interstellar", movie.getTitle());
        assertEquals("Cristopher Nolan", movie.getDirector());
        assertEquals(Genre.SCIFI, movie.getGenre());
        assertEquals(2014, movie.getReleaseYear());
        assertEquals("Sci-fi movie.", movie.getDescription());
        assertEquals("new-poster.jpg", movie.getPosterUrl());

        verify(movieRepository).save(movie);
    }

    @Test
    void updateMovie_shouldThrowException_whenMovieDoesNotExist() {
        UUID movieId = UUID.randomUUID();
        MovieEditDto dto = new MovieEditDto();
        when(movieRepository.findById(movieId))
            .thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(movieId, dto));
        verify(movieRepository).findById(movieId);
    }

    @Test
    void deleteMovie_shouldDeleteMovie() {
        UUID movieId = movie.getId();

        when(movieRepository.findById(movieId))
            .thenReturn(Optional.of(movie));

        movieService.deleteMovie(movieId);

        verify(watchlistService).deleteAllByMovie(movieId);
        verify(reviewService).deleteReviewsByMovieId(movieId);
        verify(movieRepository).delete(movie);
    }
    @Test
    void deleteMovie_shouldThrowException_whenMovieDoesNotExist() {
        UUID movieId = UUID.randomUUID();
        when(movieRepository.findById(movieId))
            .thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(movieId));
        verify(movieRepository).findById(movieId);
    }
}
