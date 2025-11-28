package app.viewflowbackend.exceptions.auth;


public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Неверный логин или пароль");
    }
}
