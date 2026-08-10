package bg.softuni.cinevault.web;

import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.service.WatchlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/watchlist/remove/{watchlistId}")
    public String removeMovie(@PathVariable UUID watchlistId,
                              @AuthenticationPrincipal User user) {

        watchlistService.removeMovie(watchlistId,user.getId());

        return "redirect:/watchlist";
    }

    @PostMapping("/watchlist/watched/{id}")
    public String markAsWatched(@PathVariable UUID id,
                                @AuthenticationPrincipal User user) {

        watchlistService.markAsWatched(id,user.getId());

        return "redirect:/watchlist";
    }

    @PostMapping("/watchlist/add/{movieId}")
    public String addMovie(@PathVariable UUID movieId,
                           @AuthenticationPrincipal User user) {

        watchlistService.addMovie(movieId, user.getId());

        return "redirect:/movies";
    }
    @GetMapping("/watchlist")
    public ModelAndView watchlist(@AuthenticationPrincipal User user) {


        ModelAndView mav = new ModelAndView("watchlist");

        mav.addObject(
                "watchlist", watchlistService.getUserWatchlist(user.getId()));

        return mav;
    }
}
