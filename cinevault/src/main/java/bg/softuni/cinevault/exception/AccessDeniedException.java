package bg.softuni.cinevault.exception;

public class AccessDeniedException extends ApplicationException {

    public AccessDeniedException() {
        super("You do not have permission to perform this action.",
                "403",
                "Access Denied"
        );
    }
}