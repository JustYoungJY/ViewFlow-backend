package app.viewflowbackend.specifications;

import app.viewflowbackend.models.basic.Compilation;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CompilationSpecifications {

    public static Specification<Compilation> isPublic() {
        return (root, query, cb) -> cb.isTrue(root.get("isPublic"));
    }

    public static Specification<Compilation> filterByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Specification.where(null);
        }

        return (root, query, cb) -> {
            Join tagsJoin = root.join("tags", JoinType.INNER);
            Join tagEntityJoin = tagsJoin.join("tag", JoinType.INNER);


            Predicate tagInListPredicate = cb.lower(tagEntityJoin.get("name")).in(tags.stream()
                    .map(String::toLowerCase)
                    .toList());

            query.groupBy(root.get("id"));
            query.having(cb.equal(cb.count(root.get("id")), tags.size()));

            return tagInListPredicate;
        };
    }

    public static Specification<Compilation> filterByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return Specification.where(null);
        }
        String likePattern = "%" + title.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), likePattern);
    }
}
