package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.movie.MovieAddDto;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.service.MovieService;
import bg.softuni.cinevault.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class MovieController {
    private final MovieService movieService;
    private final ReviewService reviewService;

    public MovieController(MovieService movieService, ReviewService reviewService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
    }

    @GetMapping("/movies/add")
    public ModelAndView getAddMoviePage() {

        ModelAndView mav = new ModelAndView("movie-add");
        mav.addObject("movieAddDto", new MovieAddDto());
        mav.addObject("genres", Genre.values());

        return mav;
    }

    @PostMapping("/movies/add")
    public ModelAndView addMovie(
            @Valid @ModelAttribute("movieAddDto") MovieAddDto movieAddDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("movie-add");
        }

        movieService.add(movieAddDto);

        return new ModelAndView("redirect:/movies");
    }
    @GetMapping("/movies")
    public ModelAndView movies() {
        ModelAndView mav = new ModelAndView("movies");

        mav.addObject("movies", movieService.findAll());

        return mav;
    }
    @GetMapping("/movies/{id}")
    public ModelAndView reviews(@PathVariable UUID id) {

        ModelAndView mav = new ModelAndView("movies");

        mav.addObject( "movie", movieService.findById(id));

        mav.addObject("reviews", reviewService.getMovieReviews(id));
        return mav;
    }
    @GetMapping("/movies/manage")
    public ModelAndView manageMovies() {

        ModelAndView mav = new ModelAndView("manage-movies");

        mav.addObject("movies", movieService.findAll());

        return mav;
    }
    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable UUID id, HttpSession session) {

        if (session.getAttribute("user_id") == null) {
            return "redirect:/login";
        }

        if (session.getAttribute("user_role") != Role.ADMIN) {
            return "redirect:/movies";
        }

        movieService.deleteMovie(id);

        return "redirect:/movies";
    }
}