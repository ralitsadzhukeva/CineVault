package bg.softuni.cinevault.exception.review;

import bg.softuni.cinevault.exception.ApplicationException;

public class DuplicateReviewException extends ApplicationException {
    public DuplicateReviewException() {
        super("You have already reviewed this movie.",
                "409",
                "Duplicate review");
    }
}
