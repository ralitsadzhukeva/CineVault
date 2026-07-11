package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.movie.MovieAddDto;
import bg.softuni.cinevault.dto.movie.MovieEditDto;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.service.MovieService;
import bg.softuni.cinevault.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView getAddMoviePage(HttpSession session) {


        ModelAndView mav = new ModelAndView("movie-add");
        mav.addObject("movieAddDto", new MovieAddDto());
        mav.addObject("genres", Genre.values());

        return mav;
    }

    @PostMapping("/movies/add")
    public ModelAndView addMovie(@Valid @ModelAttribute("movieAddDto") MovieAddDto movieAddDto, BindingResult bindingResult,HttpSession session) {
        if (bindingResult.hasErrors()) {

            ModelAndView mav = new ModelAndView("movie-add");

            mav.addObject("genres", Genre.values());

            return mav;
        }

        movieService.add(movieAddDto);

        return new ModelAndView("redirect:/movies");
    }
    @GetMapping("/movies")
    public ModelAndView movies(@RequestParam(required = false,defaultValue = "") String search) {

        ModelAndView mav = new ModelAndView("movies");

        mav.addObject("movies", movieService.searchMovies(search));
        mav.addObject("search", search);

        return mav;
    }
    @GetMapping("/movies/{id}")
    public ModelAndView movieDetails(@PathVariable UUID id) {

        ModelAndView mav = new ModelAndView("movie-details");

        mav.addObject("movie", movieService.findById(id));
        mav.addObject( "reviews",reviewService.getMovieReviews(id));
        mav.addObject("averageRating", reviewService.getAverageRating(id));
        return mav;
    }

    @GetMapping("/movies/manage")
    public ModelAndView manageMovies(HttpSession session) {
        ModelAndView mav = new ModelAndView("manage-movies");

        mav.addObject("movies", movieService.findAll());

        return mav;
    }
    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable UUID id, HttpSession session) {


        movieService.deleteMovie(id);

        return "redirect:/movies/manage";
    }
    @GetMapping("/movies/edit/{id}")
    public ModelAndView editMovie(@PathVariable UUID id, HttpSession session) {

        ModelAndView mav = new ModelAndView("movie-edit");

        mav.addObject("movieEditDto", movieService.getMovieForEdit(id));
        mav.addObject("genres", Genre.values());
        mav.addObject("movieId", id);

        return mav;
    }
    @PostMapping("/movies/edit/{id}")
    public ModelAndView editMovie(@PathVariable UUID id, @Valid MovieEditDto movieEditDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("movie-edit");

            mav.addObject("genres", Genre.values());
            mav.addObject("movieId", id);

            return mav;
        }

        movieService.updateMovie(id, movieEditDto);
        return new ModelAndView("redirect:/movies/manage");
    }

}