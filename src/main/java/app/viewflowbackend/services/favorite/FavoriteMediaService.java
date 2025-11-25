package app.viewflowbackend.services.favorite;

import app.viewflowbackend.DTO.favoriteMedia.FavoriteMediaAddRequestDTO;
import app.viewflowbackend.DTO.favoriteMedia.FavoriteMediaResponseDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.exceptions.FavoriteNotFoundException;
import app.viewflowbackend.id.FavoriteMediaPK;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.models.binders.FavoriteMedia;
import app.viewflowbackend.repositories.FavoriteMediaRepository;
import app.viewflowbackend.services.api.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class FavoriteMediaService {

    private final FavoriteMediaRepository favoriteMediaRepository;
    private final TmdbService tmdbService;

    @Autowired
    public FavoriteMediaService(FavoriteMediaRepository favoriteMediaRepository, TmdbService tmdbService) {
        this.favoriteMediaRepository = favoriteMediaRepository;
        this.tmdbService = tmdbService;
    }


    @Transactional
    public void toggleFavoriteMedia(Viewer viewer, FavoriteMediaAddRequestDTO dto) {
        FavoriteMediaPK pk = new FavoriteMediaPK(viewer.getId(), dto.getMediaId(), dto.getMediaType());

        if (favoriteMediaRepository.existsById(pk)) {
            favoriteMediaRepository.deleteById(pk);
        } else {
            tmdbService.checkMediaExists(dto.getMediaId(), dto.getMediaType());
            FavoriteMedia favoriteMedia = FavoriteMedia
                    .builder()
                    .id(pk)
                    .viewer(viewer)
                    .addedAt(LocalDateTime.now())
                    .build();

            favoriteMediaRepository.save(favoriteMedia);
        }
    }


    public FavoriteMediaResponseDTO getFavoriteMedia(Viewer viewer, Long mediaId, MediaType mediaType) {
        FavoriteMediaPK pk = new FavoriteMediaPK(viewer.getId(), mediaId, mediaType);
        FavoriteMedia favoriteMedia = favoriteMediaRepository.findById(pk)
                .orElseThrow(FavoriteNotFoundException::new);

        return FavoriteMediaResponseDTO
                .builder()
                .mediaId(favoriteMedia.getId().getMediaId())
                .mediaType(favoriteMedia.getId().getMediaType())
                .addedAt(LocalDateTime.now())
                .build();
    }


    public Page<FavoriteMediaResponseDTO> getFavoritesMedia(Viewer viewer, Pageable pageable) {
        Page<FavoriteMedia> list = favoriteMediaRepository.findByViewerId(viewer.getId(), pageable);

        return list.map(favorite -> FavoriteMediaResponseDTO
                .builder()
                .mediaId(favorite.getId().getMediaId())
                .mediaType(favorite.getId().getMediaType())
                .addedAt(favorite.getAddedAt())
                .build()
        );
    }

}
