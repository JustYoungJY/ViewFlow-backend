package app.viewflowbackend.exceptions.notFound;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("Пользователь с id: " + userId + " не найден");
    }
}
