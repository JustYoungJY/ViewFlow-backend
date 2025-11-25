package app.viewflowbackend.services;


import app.viewflowbackend.DTO.badge.BadgeCreateRequestDTO;
import app.viewflowbackend.DTO.badge.BadgeResponseDTO;
import app.viewflowbackend.DTO.badge.BadgeUpdateRequestDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.enums.Role;
import app.viewflowbackend.exceptions.BadgeNotFoundException;
import app.viewflowbackend.exceptions.MediaBadgeNotFoundException;
import app.viewflowbackend.exceptions.PermissionDeniedException;
import app.viewflowbackend.id.MediaBadgePK;
import app.viewflowbackend.models.basic.Badge;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.models.binders.MediaBadge;
import app.viewflowbackend.repositories.BadgeRepository;
import app.viewflowbackend.repositories.MediaBadgeRepository;
import app.viewflowbackend.services.api.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final MediaBadgeRepository mediaBadgeRepository;
    private final TmdbService tmdbService;


    @Autowired
    public BadgeService(BadgeRepository badgeRepository, MediaBadgeRepository mediaBadgeRepository,
                        TmdbService tmdbService) {
        this.badgeRepository = badgeRepository;
        this.mediaBadgeRepository = mediaBadgeRepository;
        this.tmdbService = tmdbService;
    }

    @Transactional
    public void createBadge(Viewer viewer, BadgeCreateRequestDTO dto) {
        Badge badge = Badge
                .builder()
                .name(dto.getName())
                .color(dto.getColor())
                .build();

        badgeRepository.save(badge);
    }


    public BadgeResponseDTO getMediaBadge(Long mediaId, MediaType mediaType) {
        MediaBadgePK pk = new MediaBadgePK(mediaId, mediaType);
        MediaBadge mediaBadge = mediaBadgeRepository.findById(pk)
                .orElseThrow(() -> new MediaBadgeNotFoundException(mediaId, mediaType));
        Badge badge = mediaBadge.getBadge();

        return BadgeResponseDTO
                .builder()
                .name(badge.getName())
                .color(badge.getColor())
                .build();
    }

    public List<BadgeResponseDTO> getAllBadges() {
        List<Badge> badges = badgeRepository.findAll();
        return badges.stream().map(badge -> BadgeResponseDTO
                .builder()
                .name(badge.getName())
                .color(badge.getColor())
                .build()
        ).toList();
    }


    @Transactional
    public void updateBadge(Viewer viewer, Long badgeId, BadgeUpdateRequestDTO dto) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new BadgeNotFoundException(badgeId));

        badge.setName(dto.getName() == null ? badge.getName() : dto.getName());
        badge.setColor(dto.getColor() == null ? badge.getColor() : dto.getColor());
        badgeRepository.save(badge);
    }

    @Transactional
    public void deleteBadge(Viewer viewer, Long badgeId) {
        badgeRepository.deleteById(badgeId);
    }


    @Transactional
    public void addBadgeToMedia(Viewer viewer, Long badgeId ,Long mediaId, MediaType mediaType) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new BadgeNotFoundException(badgeId));
        tmdbService.checkMediaExists(mediaId, mediaType);

        MediaBadgePK pk = new MediaBadgePK(mediaId, mediaType);
        MediaBadge mediaBadge = new MediaBadge(pk, badge);
        mediaBadgeRepository.save(mediaBadge);
    }


    @Transactional
    public void removeBadgeFromMedia(Viewer viewer, Long mediaId, MediaType mediaType) {
        tmdbService.checkMediaExists(mediaId, mediaType);

        MediaBadgePK pk = new MediaBadgePK(mediaId, mediaType);
        mediaBadgeRepository.deleteById(pk);
    }

}
