package ru.nhp.core.product_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.nhp.core.entities.Product;
import ru.nhp.core.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TestEntityManager entityManager;
    private static final String TITLE = "Beef";
    private static final String DESCRIPTION = "Lovely beef";
    private static final String PATHNAME = "img/3.png";
    private static final BigDecimal PRICE = BigDecimal.valueOf(100);
    private Long currentId;

    @BeforeEach
    public void init() {
        Product product = new Product();
        product.setTitle(TITLE);
        product.setPrice(PRICE);
        product.setDescription(DESCRIPTION);
        product.setPathname(PATHNAME);
        entityManager.persist(product);
        entityManager.flush();
        List<Product> products = productRepository.findAll();
        currentId = products.get(products.size() - 1).getId();
    }

    @Test
    public void countByTitleTest() {
        Long count = productRepository.countByTitle(TITLE);
        Assertions.assertEquals(1, count);
    }

    @Test
    public void findAllTest() {
        List<Product> products = productRepository.findAll();
        Assertions.assertEquals(3, products.size());
    }

    @Test
    public void findByIdTest() {
        Product product = productRepository.findById(currentId).orElse(new Product());
        Assertions.assertEquals(TITLE, product.getTitle());
    }
}
