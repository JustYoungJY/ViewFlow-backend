package app.viewflowbackend.exceptions.notFound;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long tagId) {
        super("Тэг с id: " + tagId + " не найден");
    }
}
