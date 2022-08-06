package ru.nhp.core.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.nhp.core.entities.Product;

public class ProductSpecifications {
    public static Specification<Product> priceGreaterOrEqualsThan(Integer price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> priceLessThanOrEqualsThan(Integer price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> titleLike(String titlePart) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), String.format("%%%s%%", titlePart));
    }

    public static Specification<Product> findByCategory(String categoryTitle) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("categories").get("title"), categoryTitle);
    }

    public static Specification<Product> findByCategoryId(Long categoryId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("categories").get("id"), categoryId);
    }
}
