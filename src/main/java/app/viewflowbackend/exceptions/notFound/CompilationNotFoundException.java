package app.viewflowbackend.exceptions.notFound;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(Long compilationId) {
        super("Compilation with id " + compilationId + " not found");
    }
}
