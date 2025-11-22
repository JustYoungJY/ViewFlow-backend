package app.viewflowbackend.specifications;

import app.viewflowbackend.models.basic.Compilation;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CompilationSpecifications {

    public static Specification<Compilation> isPublic() {
        return (root, query, cb) -> cb.isTrue(root.get("isPublic"));
    }

    public static Specification<Compilation> filterBy(String filter) {
        if (filter == null || filter.isEmpty()) {
            return Specification.where(null);
        }

        String likePattern = "%" + filter.toLowerCase() + "%";

        return (root, query, cb) -> {

            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), likePattern);

            Join tagsJoin = root.join("tags", JoinType.LEFT);

            Join tagEntityJoin = tagsJoin.join("tag", JoinType.LEFT);

            Predicate tagPredicate = cb.like(cb.lower(tagEntityJoin.get("name")), likePattern);

            Predicate combinedPredicate = cb.or(titlePredicate, tagPredicate);

            query.distinct(true);

            return combinedPredicate;
        };
    }
}
