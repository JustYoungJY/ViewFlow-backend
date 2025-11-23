package app.viewflowbackend.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super("Comment with id " + commentId + " not found");
    }
}
