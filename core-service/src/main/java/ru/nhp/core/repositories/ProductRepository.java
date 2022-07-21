package ru.nhp.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nhp.core.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("select count(p) from Product p where p.title = ?1")
    Long countByTitle(String title);

    @Query("select p from Product p join Category c where c.id = ?1")
    Page<Product> findAllByCategoryId(Long id, Pageable pageable);
}
