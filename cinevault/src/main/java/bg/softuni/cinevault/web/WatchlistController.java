package bg.softuni.cinevault.web;

import bg.softuni.cinevault.service.WatchlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class WatchlistController {
    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }
    @GetMapping("/watchlist/remove/{movieId}")
    public String removeMovie(@PathVariable UUID movieId, HttpSession session) {

        if (session.getAttribute("user_id") == null) {
            return "redirect:/login";
        }
        watchlistService.removeMovie(movieId);
        return "redirect:/watchlist";
    }
    @GetMapping("/watchlist/watched/{id}")
    public String markAsWatched(@PathVariable UUID id, HttpSession session) {

        if (session.getAttribute("user_id") == null) {
            return "redirect:/login";
        }

        watchlistService.markAsWatched(id);

        return "redirect:/watchlist";
    }

    @GetMapping("/watchlist/add/{movieId}")
    public String addMovie(@PathVariable UUID movieId, HttpSession session) {
        if (session.getAttribute("user_id") == null) {
            return "redirect:/login";
        }

        UUID userId = (UUID) session.getAttribute("user_id");

        watchlistService.addMovie(movieId, userId);

        return "redirect:/movies";
    }
    @GetMapping("/watchlist")
    public ModelAndView watchlist(HttpSession session) {
        if (session.getAttribute("user_id") == null) {
            return new ModelAndView("redirect:/login");
        }
        UUID userId = (UUID) session.getAttribute("user_id");

        ModelAndView mav = new ModelAndView("watchlist");

        mav.addObject(
                "watchlist", watchlistService.getUserWatchlist(userId));

        return mav;
    }
}
