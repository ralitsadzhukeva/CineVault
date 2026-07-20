package bg.softuni.cinevault.exception.user;

import bg.softuni.cinevault.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super("User was not found.",
                "404",
                "User not found");    }
}
