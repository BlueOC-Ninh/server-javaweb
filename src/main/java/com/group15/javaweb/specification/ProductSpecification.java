package com.group15.javaweb.specification;

import com.group15.javaweb.entity.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> filter(String name, String categoryId, Boolean deleted) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (categoryId != null && !categoryId.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if (deleted != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("deleted"), deleted));
            }

            return predicate;
        };
    }
}
