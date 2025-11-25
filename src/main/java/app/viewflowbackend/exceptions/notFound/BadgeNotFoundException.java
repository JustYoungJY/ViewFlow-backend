package app.viewflowbackend.exceptions.notFound;

public class BadgeNotFoundException extends RuntimeException {
    public BadgeNotFoundException(Long badgeId) {
        super("badge with id " + badgeId + " not found");
    }
}
