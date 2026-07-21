package bg.softuni.cinevault.exception.user;

import bg.softuni.cinevault.exception.ApplicationException;

import java.util.UUID;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(UUID id) {
        super("User with id: "+id+" was not found.",
                "404",
                "User not found");    }
}
