package app.viewflowbackend.exceptions;

import app.viewflowbackend.enums.MediaType;

public class MediaNotFoundException extends RuntimeException {
    public MediaNotFoundException(Long id, MediaType type) {
        super("Media with id " + id + " and type " + type + " not found");
    }
}
