package app.viewflowbackend.exceptions;

public class BadgeNotFoundException extends RuntimeException {
    public BadgeNotFoundException(Long badgeId) {
        super("badge with id " + badgeId + " not found");
    }
}
