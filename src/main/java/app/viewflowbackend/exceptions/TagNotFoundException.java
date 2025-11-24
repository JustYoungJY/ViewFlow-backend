package app.viewflowbackend.exceptions;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long tagId) {
        super("Tag with id " + tagId + " not found");
    }
}
