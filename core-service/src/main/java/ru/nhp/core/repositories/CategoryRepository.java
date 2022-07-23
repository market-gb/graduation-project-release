package ru.nhp.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nhp.core.entities.Category;
import ru.nhp.core.entities.Product;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Product> {
    @Query("select count(c) from Category c where c.title = ?1")
    Long countByTitle(String title);
}
