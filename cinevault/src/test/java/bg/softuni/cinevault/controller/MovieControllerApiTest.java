package bg.softuni.cinevault.controller;

import bg.softuni.cinevault.config.SecurityConfig;
import bg.softuni.cinevault.dto.movie.MovieEditDto;
import bg.softuni.cinevault.entities.Movie;
import bg.softuni.cinevault.service.MovieService;
import bg.softuni.cinevault.service.ReviewService;
import bg.softuni.cinevault.web.MovieController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@Import(SecurityConfig.class)
class MovieControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @MockitoBean
    private ReviewService reviewService;


    @Test
    @WithMockUser(roles = "USER")
    void movies_shouldReturnMoviesPage() throws Exception {

        when(movieService.searchMovies(""))
                .thenReturn(List.of());

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"));
    }


    @Test
    @WithMockUser(roles = "USER")
    void movies_shouldSearchMovies() throws Exception {

        when(movieService.searchMovies("interstellar"))
                .thenReturn(List.of());

        mockMvc.perform(get("/movies")
                        .param("search", "interstellar"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void movieDetails_shouldReturnMovieDetailsPage() throws Exception {

        UUID movieId = UUID.randomUUID();

        Movie mockMovie = new Movie();
        mockMovie.setTitle("Test Movie Title");

        when(movieService.findById(movieId))
                .thenReturn(mockMovie);

        when(reviewService.getMovieReviews(movieId))
                .thenReturn(List.of());

        when(reviewService.getAverageRating(movieId))
                .thenReturn(0.0);

        mockMvc.perform(get("/movies/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("reviews"))
                .andExpect(model().attributeExists("averageRating"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAddMoviePage_shouldReturnMovieAddPage() throws Exception {

        mockMvc.perform(get("/movies/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-add"))
                .andExpect(model().attributeExists("movieAddDto"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void manageMovies_shouldReturnManageMoviesPage() throws Exception {

        when(movieService.findAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/movies/manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-movies"))
                .andExpect(model().attributeExists("movies"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteMovie_shouldDeleteMovieAndRedirect() throws Exception {

        UUID movieId = UUID.randomUUID();

        doNothing().when(movieService)
                .deleteMovie(movieId);

        mockMvc.perform(post("/movies/delete/{id}", movieId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/manage"));

        verify(movieService)
                .deleteMovie(movieId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editMovie_shouldReturnEditMoviePage() throws Exception {

        UUID movieId = UUID.randomUUID();

        MovieEditDto movieEditDto = new MovieEditDto();

        when(movieService.getMovieForEdit(movieId))
                .thenReturn(movieEditDto);

        mockMvc.perform(get("/movies/edit/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-edit"))
                .andExpect(model().attributeExists("movieEditDto"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("movieId", movieId));
    }

    @Test
    @WithMockUser(roles = "USER")
    void editMovie_shouldBeForbiddenForUser() throws Exception {
        UUID movieId = UUID.randomUUID();
        when(movieService.getMovieForEdit(movieId))
                .thenReturn(new MovieEditDto());

        mockMvc.perform(get("/movies/edit/{id}", movieId))
                .andExpect(status().isForbidden());
    }
}

