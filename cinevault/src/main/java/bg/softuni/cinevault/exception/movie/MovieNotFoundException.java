package bg.softuni.cinevault.exception.movie;

import bg.softuni.cinevault.exception.ApplicationException;

public class MovieNotFoundException extends ApplicationException {

    public MovieNotFoundException() {
        super("Movie was not found.",
                "404",
                "Movie not found");
    }
}
