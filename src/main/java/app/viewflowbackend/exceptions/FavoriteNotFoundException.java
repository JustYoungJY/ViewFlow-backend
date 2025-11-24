package app.viewflowbackend.exceptions;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException() {
        super("Favorite not found");
    }
}
