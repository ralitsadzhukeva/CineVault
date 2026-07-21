package bg.softuni.cinevault.exception.movie;

import bg.softuni.cinevault.exception.ApplicationException;

import java.util.UUID;

public class MovieNotFoundException extends ApplicationException {

    public MovieNotFoundException(UUID id) {
        super("Movie with id: "+id+" was not found.",
                "404",
                "Movie not found");
    }
}
