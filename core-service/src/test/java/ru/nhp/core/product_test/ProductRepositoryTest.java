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

    @BeforeEach
    public void init() {
        Product product = new Product();
        product.setTitle(TITLE);
        product.setPrice(PRICE);
        product.setDescription(DESCRIPTION);
        product.setPathname(PATHNAME);
        entityManager.persist(product);
        entityManager.flush();
    }

    @Test
    public void countByTitleTest() {
        Long count = productRepository.countByTitle(TITLE);
        Assertions.assertEquals(1, count);
    }
}
