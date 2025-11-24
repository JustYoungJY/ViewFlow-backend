package app.viewflowbackend.services;

import app.viewflowbackend.DTO.auxiliary.CompilationMediaDTO;
import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
import app.viewflowbackend.DTO.compilation.CompilationCreateRequestDTO;
import app.viewflowbackend.DTO.compilation.CompilationListItemDTO;
import app.viewflowbackend.DTO.compilation.CompilationResponseDTO;
import app.viewflowbackend.DTO.compilation.CompilationUpdateRequestDTO;
import app.viewflowbackend.DTO.compilationMedia.CompilationMediaAddRequestDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.exceptions.AccessDeniedException;
import app.viewflowbackend.exceptions.AlreadyLikedException;
import app.viewflowbackend.exceptions.CompilationNotFoundException;
import app.viewflowbackend.id.CompilationLikePK;
import app.viewflowbackend.id.CompilationMediaPK;
import app.viewflowbackend.models.basic.Compilation;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.models.binders.CompilationLike;
import app.viewflowbackend.models.binders.CompilationMedia;
import app.viewflowbackend.repositories.CompilationLikeRepository;
import app.viewflowbackend.repositories.CompilationMediaRepository;
import app.viewflowbackend.repositories.CompilationRepository;
import app.viewflowbackend.services.api.TmdbService;
import app.viewflowbackend.specifications.CompilationSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationLikeRepository compilationLikeRepository;
    private final CompilationMediaRepository compilationMediaRepository;
    private final TmdbService tmdbService;
    private final ModelMapper modelMapper;

    @Autowired
    public CompilationService(CompilationRepository compilationRepository, ModelMapper modelMapper,
                              CompilationLikeRepository compilationLikeRepository,
                              CompilationMediaRepository compilationMediaRepository,
                              TmdbService tmdbService) {
        this.compilationRepository = compilationRepository;
        this.compilationLikeRepository = compilationLikeRepository;
        this.compilationMediaRepository = compilationMediaRepository;
        this.tmdbService = tmdbService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void createCompilation(Viewer viewer, CompilationCreateRequestDTO request) {
        Compilation compilation = modelMapper.map(request, Compilation.class);
        compilation.setViewer(viewer);
        compilation.setCreatedAt(LocalDateTime.now());
        compilation.setUpdatedAt(LocalDateTime.now());
        compilation.setMediaCount(request.getMediaList().size());
        compilation.setLikesCount(0);

        //TODO: add field

        compilationRepository.save(compilation);

        for (CompilationMediaAddRequestDTO mediaDto : request.getMediaList()) {
            CompilationMedia cm = CompilationMedia.builder()
                    .id(new CompilationMediaPK(compilation.getId(), mediaDto.getMediaId(), mediaDto.getMediaType()))
                    .compilation(compilation)
                    .orderIndex(mediaDto.getOrderIndex())
                    .authorDescription(mediaDto.getAuthorDescription())
                    .addedAt(LocalDateTime.now())
                    .build();
            compilationMediaRepository.save(cm);
        }

    }

    public CompilationResponseDTO getCompilation(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));


        List<CompilationMediaDTO> media = compilationMediaRepository.findByCompilationId(compilationId).stream()
                .map(cm -> {
                    MediaDetailsDTO details = tmdbService.getMediaDetails(cm.getId().getMediaId(), cm.getId().getMediaType());
                    return CompilationMediaDTO.builder()
                            .mediaId(cm.getId().getMediaId())
                            .mediaType(cm.getId().getMediaType())
                            .orderIndex(cm.getOrderIndex())
                            .authorDescription(cm.getAuthorDescription())
                            .addedAt(cm.getAddedAt())
                            .mediaDetails(details)
                            .build();
                })
                .toList();


        CompilationResponseDTO response = modelMapper.map(compilation, CompilationResponseDTO.class);
        response.setMedia(media);
        return response;
    }


    public Page<CompilationListItemDTO> getCompilations(Pageable pageable, String filter) {
        Specification<Compilation> spec = Specification.where(CompilationSpecifications.isPublic());
        if (filter != null) {
            spec = spec.and(CompilationSpecifications.filterBy(filter));
        }
        Page<Compilation> page = compilationRepository.findAll(spec, pageable);
        return page.map(this::mapToListItem);
    }


    @Transactional
    public void updateCompilation(Viewer viewer, Long id, CompilationUpdateRequestDTO dto) throws AccessDeniedException {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException(id));
        if (!viewer.getId().equals(compilation.getViewer().getId())) {
            throw new AccessDeniedException("Not owner");
        }

        Optional.ofNullable(dto.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(dto.getDescription()).ifPresent(compilation::setDescription);
        Optional.ofNullable(dto.getIsPublic()).ifPresent(compilation::setIsPublic);
        compilation.setUpdatedAt(LocalDateTime.now());

        compilationRepository.save(compilation);
    }


    @Transactional
    public void deleteCompilation(Viewer viewer, Long compilationId) throws AccessDeniedException {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));

        if (!viewer.getId().equals(compilation.getViewer().getId())) {
            throw new AccessDeniedException("Not owner");
        }

        compilationRepository.delete(compilation);
    }


    @Transactional
    public void addMediaToCompilation(Viewer viewer, Long compilationId, CompilationMediaAddRequestDTO dto) throws AccessDeniedException {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        if (!viewer.getId().equals(compilation.getViewer().getId())) {
            throw new AccessDeniedException("Not owner");
        }

        CompilationMediaPK pk = new CompilationMediaPK(compilationId, dto.getMediaId(), dto.getMediaType());
        CompilationMedia cm = CompilationMedia
                .builder()
                .id(pk)
                .compilation(compilation)
                .orderIndex(dto.getOrderIndex())
                .authorDescription(dto.getAuthorDescription())
                .addedAt(LocalDateTime.now())
                .build();

        compilation.setMediaCount(compilation.getMediaCount() + 1);
        compilationRepository.save(compilation);
        compilationMediaRepository.save(cm);
    }


    @Transactional
    public void removeMediaFromCompilation(Viewer viewer, Long compilationId, Long mediaId, MediaType mediaType) throws AccessDeniedException {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));

        if (!viewer.getId().equals(compilation.getViewer().getId())) {
            throw new app.viewflowbackend.exceptions.AccessDeniedException("Not owner");
        }

        CompilationMediaPK pk = new CompilationMediaPK(compilationId, mediaId, mediaType);
        compilationMediaRepository.deleteById(pk);
        compilation.setMediaCount(compilation.getMediaCount() - 1);
        compilationRepository.save(compilation);
    }


    @Transactional
    public void likeCompilation(Viewer viewer, Long compilationId) throws AccessDeniedException {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));

        CompilationLikePK pk = new CompilationLikePK(compilationId, viewer.getId());
        if (compilationLikeRepository.existsById(pk)) {
            throw new AlreadyLikedException(compilationId);
        }

        CompilationLike like = CompilationLike
                .builder()
                .id(pk)
                .compilation(compilation)
                .viewer(viewer)
                .createdAt(LocalDateTime.now())
                .build();

        compilationLikeRepository.save(like);
        compilation.setLikesCount(compilation.getLikesCount() + 1);
        compilationRepository.save(compilation);
    }


    @Transactional
    public void unlikeCompilation(Viewer viewer, Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));

        CompilationLikePK pk = new CompilationLikePK(compilationId, viewer.getId());
        compilationLikeRepository.deleteById(pk);
        compilation.setLikesCount(compilation.getLikesCount() - 1);
        compilationRepository.save(compilation);
    }


    private CompilationListItemDTO mapToListItem(Compilation compilation) {
        return CompilationListItemDTO.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .imageUrl(compilation.getImageUrl())
                .mediaCount(compilation.getMediaCount())
                .likesCount(compilation.getLikesCount())
                .viewerUsername(compilation.getViewer().getUsername())
                .build();
    }


}
