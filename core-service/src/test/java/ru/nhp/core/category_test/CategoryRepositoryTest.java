package ru.nhp.core.category_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.nhp.core.entities.Category;
import ru.nhp.core.repositories.CategoryRepository;

@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestEntityManager entityManager;
    private static final String TITLE = "Category";
    private static final String DESCRIPTION = "description";
    private static final String PATHNAME = "img/3.png";

    @BeforeEach
    public void init() {
        Category category = Category.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .pathname(PATHNAME)
                .build();
        entityManager.persist(category);
        entityManager.flush();
    }

    @Test
    public void countByTitleTest() {
        Long count = categoryRepository.countByTitle(TITLE);
        Assertions.assertEquals(1, count);
    }
}
