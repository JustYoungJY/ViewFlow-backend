package app.viewflowbackend.exceptions.alreadyExists;

public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException(String tagName) {
        super("Тэг " + tagName + " уже существует");
    }
}
