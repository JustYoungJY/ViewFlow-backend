package app.viewflowbackend.exceptions.notFound;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(Long compilationId) {
        super("Подборка с id: " + compilationId + " не найдена");
    }
}
