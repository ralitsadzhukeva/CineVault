package bg.softuni.cinevault.exception.review;

import bg.softuni.cinevault.exception.ApplicationException;

public class ReviewNotFoundException extends ApplicationException {
    public ReviewNotFoundException() {
        super("Review was not found.",
                "404",
                "Review not found");
    }
}
