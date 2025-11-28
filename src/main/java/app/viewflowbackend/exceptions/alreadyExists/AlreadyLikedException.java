package app.viewflowbackend.exceptions.alreadyExists;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException(Long id) {
        super("Подборка с id: " + id + " уже лайкнута");
    }
}
