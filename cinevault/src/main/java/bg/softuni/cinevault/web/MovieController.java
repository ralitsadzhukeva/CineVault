package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.movie.MovieAddDto;
import bg.softuni.cinevault.enums.Genre;
import bg.softuni.cinevault.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
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
}