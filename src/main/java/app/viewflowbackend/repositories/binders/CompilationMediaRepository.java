package app.viewflowbackend.repositories.binders;

import app.viewflowbackend.id.CompilationMediaPK;
import app.viewflowbackend.models.binders.CompilationMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationMediaRepository extends JpaRepository<CompilationMedia, CompilationMediaPK> {
    List<CompilationMedia> findByCompilationId(Long compilation_id);

    void deleteById(CompilationMediaPK compilationMediaPK);

    @Modifying
    @Query("UPDATE CompilationMedia cm SET cm.orderIndex = cm.orderIndex + 1 " +
            "WHERE cm.compilation.id = :compilationId " +
            "AND cm.orderIndex >= :fromIndex AND cm.orderIndex < :toIndex")
    void shiftIndicesDown(@Param("compilationId") Long compilationId,
                          @Param("fromIndex") Integer fromIndex,
                          @Param("toIndex") Integer toIndex);


    @Modifying
    @Query("UPDATE CompilationMedia cm SET cm.orderIndex = cm.orderIndex - 1 " +
            "WHERE cm.compilation.id = :compilationId " +
            "AND cm.orderIndex > :fromIndex AND cm.orderIndex <= :toIndex")
    void shiftIndicesUp(@Param("compilationId") Long compilationId,
                        @Param("fromIndex") Integer fromIndex,
                        @Param("toIndex") Integer toIndex);
}
