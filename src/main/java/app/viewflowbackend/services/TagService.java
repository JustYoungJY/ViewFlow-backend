package app.viewflowbackend.services;


import app.viewflowbackend.DTO.tag.TagCreateRequest;
import app.viewflowbackend.DTO.tag.TagResponseDTO;
import app.viewflowbackend.DTO.tag.TagUpdateRequest;
import app.viewflowbackend.exceptions.PermissionDeniedException;
import app.viewflowbackend.exceptions.CompilationNotFoundException;
import app.viewflowbackend.exceptions.TagAlreadyExistsException;
import app.viewflowbackend.exceptions.TagNotFoundException;
import app.viewflowbackend.id.CompilationTagPK;
import app.viewflowbackend.models.basic.Compilation;
import app.viewflowbackend.models.basic.Tag;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.models.binders.CompilationTag;
import app.viewflowbackend.repositories.CompilationRepository;
import app.viewflowbackend.repositories.CompilationTagRepository;
import app.viewflowbackend.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final CompilationRepository compilationRepository;
    private final CompilationTagRepository compilationTagRepository;


    @Autowired
    public TagService(TagRepository tagRepository, CompilationRepository compilationRepository,
                      CompilationTagRepository compilationTagRepository) {
        this.tagRepository = tagRepository;
        this.compilationRepository = compilationRepository;
        this.compilationTagRepository = compilationTagRepository;
    }


    @Transactional
    public void createTag(TagCreateRequest tagCreateRequest) {
        String name = tagCreateRequest.getName();
        if (tagRepository.existsByName(name)) {
            throw new TagAlreadyExistsException(name);
        }
        Tag tag = Tag
                .builder()
                .name(name)
                .build();

        tagRepository.save(tag);
    }


    public TagResponseDTO getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        return TagResponseDTO
                .builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }


    public Page<TagResponseDTO> getTags(String query, Pageable pageable) {
        Page<Tag> page;
        if (query != null && !query.isEmpty()) {
            page = tagRepository.findByNameContainingIgnoreCase(query, pageable);
        } else {
            page = tagRepository.findAll(pageable);
        }

        return page.map(tag -> TagResponseDTO
                .builder()
                .id(tag.getId())
                .name(tag.getName())
                .build()
        );
    }


    @Transactional
    public void updateTag(Long tagId, TagUpdateRequest tagUpdateRequest) {
        String newName = tagUpdateRequest.getName();
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        if (tagRepository.existsByName(newName) && !tag.getName().equals(newName)) {
            throw new TagAlreadyExistsException(newName);
        }
        tag.setName(newName);
        tagRepository.save(tag);
    }


    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id);
        }
        tagRepository.deleteById(id);
    }


    @Transactional
    public void addTagToCompilation(Viewer viewer, Long compilationId, Long tagId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        if (!viewer.getId().equals(compilation.getViewer().getId())) {
            throw new PermissionDeniedException("Not owner");
        }

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        CompilationTagPK pk = new CompilationTagPK(tagId, compilationId);
        if (compilationTagRepository.existsById(pk)) {
            throw new TagAlreadyExistsException(tag.getName());
        }

        CompilationTag compilationTag = CompilationTag
                .builder()
                .id(pk)
                .tag(tag)
                .compilation(compilation)
                .build();

        compilationTagRepository.save(compilationTag);
    }


    @Transactional
    public void removeTagFromCompilation(Viewer viewer, Long compilationId, Long tagId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        if (!viewer.getId().equals(compilation.getViewer().getId())) {
            throw new PermissionDeniedException("Not owner");
        }

        CompilationTagPK pk = new CompilationTagPK(tagId, compilationId);
        compilationTagRepository.deleteById(pk);
    }

}
