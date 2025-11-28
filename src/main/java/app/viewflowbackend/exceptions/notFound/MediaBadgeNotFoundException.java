package app.viewflowbackend.exceptions.notFound;

import app.viewflowbackend.enums.MediaType;

public class MediaBadgeNotFoundException extends RuntimeException {
    public MediaBadgeNotFoundException(Long mediaId, MediaType mediaType) {
        super("Бейдж для медиа " + mediaId + " и " + mediaType + " не найден");
    }
}
