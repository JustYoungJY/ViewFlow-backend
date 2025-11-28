package app.viewflowbackend.exceptions.notFound;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super("Комментарий с id: " + commentId + " не найден");
    }
}
