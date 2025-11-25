package app.viewflowbackend.exceptions.alreadyExists;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException(Long id) {
        super("Compilation with id " + id + " already liked");
    }
}
