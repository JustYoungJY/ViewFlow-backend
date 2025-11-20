package app.viewflowbackend.exceptions;


public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Invalid refresh token");
    }
}
