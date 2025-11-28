package app.viewflowbackend.exceptions.notFound;

public class BadgeNotFoundException extends RuntimeException {
    public BadgeNotFoundException(Long badgeId) {
        super("Бейдж с id: " + badgeId + " не найден");
    }
}
