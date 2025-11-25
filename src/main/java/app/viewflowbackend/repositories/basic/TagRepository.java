package app.viewflowbackend.repositories.basic;

import app.viewflowbackend.models.basic.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String tagName);

    Page<Tag> findByNameContainingIgnoreCase(String query, Pageable pageable);
}
