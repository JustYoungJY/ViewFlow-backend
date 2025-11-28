package app.viewflowbackend.exceptions.notFound;

import app.viewflowbackend.enums.MediaType;

public class MediaNotFoundException extends RuntimeException {
    public MediaNotFoundException(Long id, MediaType type) {
        super("Медиа с id: " + id + " и типом " + type + " не найдено");
    }
}
