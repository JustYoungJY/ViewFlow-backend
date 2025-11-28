package app.viewflowbackend.exceptions.api;

public class InvalidResponseFormatException extends RuntimeException {
    public InvalidResponseFormatException(String message) {
        super(message);
    }
}
