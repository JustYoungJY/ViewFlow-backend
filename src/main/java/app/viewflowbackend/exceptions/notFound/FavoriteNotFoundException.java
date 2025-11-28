package app.viewflowbackend.exceptions.notFound;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException() {
        super("Избранное не найдено");
    }
}
