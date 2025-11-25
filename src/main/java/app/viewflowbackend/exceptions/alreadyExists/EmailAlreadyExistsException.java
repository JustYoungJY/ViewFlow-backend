package app.viewflowbackend.exceptions.alreadyExists;


public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email " + email + " already exists");
    }
}
