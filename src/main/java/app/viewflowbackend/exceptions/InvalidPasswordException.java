package app.viewflowbackend.exceptions;


public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Invalid password");
    }
}
