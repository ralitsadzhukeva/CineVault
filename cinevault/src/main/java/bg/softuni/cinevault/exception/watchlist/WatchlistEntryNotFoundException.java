package bg.softuni.cinevault.exception.watchlist;

import bg.softuni.cinevault.exception.ApplicationException;

public class WatchlistEntryNotFoundException extends ApplicationException {

    public WatchlistEntryNotFoundException(String id) {
        super("Watchlist entry with id:"+ id +"was not found.",
                "404",
                "Watchlist not found");    }
}
