package app.viewflowbackend.services;

import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
import app.viewflowbackend.DTO.watchedMedia.WatchedMediaAddRequestDTO;
import app.viewflowbackend.DTO.watchedMedia.WatchedMediaResponseDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.exceptions.notFound.MediaNotFoundException;
import app.viewflowbackend.id.WatchedMediaPK;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.models.binders.WatchedMedia;
import app.viewflowbackend.repositories.binders.WatchedMediaRepository;
import app.viewflowbackend.services.api.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class WatchedMediaService {

    private final WatchedMediaRepository watchedMediaRepository;
    private final TmdbService tmdbService;


    @Autowired
    public WatchedMediaService(WatchedMediaRepository watchedMediaRepository, TmdbService tmdbService) {
        this.watchedMediaRepository = watchedMediaRepository;
        this.tmdbService = tmdbService;
    }


    @Transactional
    public void addOrUpdateWatched(Viewer viewer, WatchedMediaAddRequestDTO dto) {
        WatchedMediaPK pk = new WatchedMediaPK(viewer.getId(), dto.getMediaId(), dto.getMediaType());

        WatchedMedia watchedMedia = watchedMediaRepository.findById(pk).orElse(WatchedMedia
                .builder()
                .id(pk)
                .viewer(viewer)
                .watchedAt(LocalDateTime.now())
                .build()
        );

        watchedMedia.setProgress(dto.getProgress());
        watchedMedia.setLastSeason(dto.getLastSeason());
        watchedMedia.setLastEpisode(dto.getLastEpisode());

        if (watchedMedia.getProgress() < 0 || watchedMedia.getProgress() > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        if (dto.getMediaType() == MediaType.MOVIE && (dto.getLastSeason() != null || dto.getLastEpisode() != null)) {
            throw new IllegalArgumentException("Season/Episode only for TV");
        }

        watchedMediaRepository.save(watchedMedia);
    }


    public Page<WatchedMediaResponseDTO> getWatchedMedia(Viewer viewer, MediaType mediaType, Pageable pageable) {
        Page<WatchedMedia> page;
        if (mediaType != null) {
            page = watchedMediaRepository.findByViewerIdAndId_MediaType(viewer.getId(), mediaType, pageable);
        } else {
            page = watchedMediaRepository.findByViewerId(viewer.getId(), pageable);
        }

        return page.map(this::mapToDTO);
    }


    @Transactional
    public void removeWatched(Viewer viewer, Long mediaId, MediaType mediaType) {
        WatchedMediaPK pk = new WatchedMediaPK(viewer.getId(), mediaId, mediaType);
        if (!watchedMediaRepository.existsById(pk)) {
            throw new MediaNotFoundException(mediaId, mediaType);
        }
        watchedMediaRepository.deleteById(pk);
    }


    private WatchedMediaResponseDTO mapToDTO(WatchedMedia watchedMedia) {
        MediaDetailsDTO mediaDetails = tmdbService
                .getMediaDetails(watchedMedia.getId().getMediaId(), watchedMedia.getId().getMediaType());
        return WatchedMediaResponseDTO
                .builder()
                .mediaId(watchedMedia.getId().getMediaId())
                .mediaType(watchedMedia.getId().getMediaType())
                .watchedAt(watchedMedia.getWatchedAt())
                .progress(watchedMedia.getProgress())
                .lastSeason(watchedMedia.getLastSeason())
                .lastEpisode(watchedMedia.getLastEpisode())
                .mediaDetails(mediaDetails)
                .build();
    }


}
