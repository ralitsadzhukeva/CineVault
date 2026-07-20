package bg.softuni.cinevault.exception.user;

import bg.softuni.cinevault.exception.ApplicationException;

public class DuplicateUsernameException extends ApplicationException {
    public DuplicateUsernameException() {
        super("User with this username already exists.",
                "409",
                "Duplicate username");
    }
}
