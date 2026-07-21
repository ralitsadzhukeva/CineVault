package bg.softuni.cinevault.exception.review;

import bg.softuni.cinevault.exception.ApplicationException;

import java.util.UUID;

public class ReviewNotFoundException extends ApplicationException {
    public ReviewNotFoundException(UUID id) {
        super("Review with id: "+id+" was not found.",
                "404",
                "Review not found");
    }
}
