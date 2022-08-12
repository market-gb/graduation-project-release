package ru.nhp.core.category_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.core.converters.CategoryConverter;
import ru.nhp.core.entities.Category;
import ru.nhp.core.repositories.CategoryRepository;
import ru.nhp.core.services.CategoryService;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {CategoryService.class})
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private CategoryConverter categoryConverter;
    private final static String CATEGORY_TITLE = "test_category_title";
    private final static String CATEGORY_DESCRIPTION = "Category_description";
    private final static String CATEGORY_PATHNAME = "Category_pathname";
    private static Category category;
    private static CategoryDto categoryDto;
    private static Page<Category> categoryPage;

    @BeforeAll
    public static void initEntities() {
        category = Category.builder()
                .id(1L)
                .title(CATEGORY_TITLE)
                .description(CATEGORY_DESCRIPTION)
                .pathname(CATEGORY_PATHNAME)
                .build();

        categoryDto = CategoryDto.builder()
                .id(1L)
                .title(CATEGORY_TITLE)
                .description(CATEGORY_DESCRIPTION)
                .pathname(CATEGORY_PATHNAME)
                .build();

        categoryPage = new PageImpl<>(List.of(category));
    }

    @Test
    public void findAllTest() {
        Mockito.doReturn(categoryPage).when(categoryRepository).findAll(PageRequest.of(0, 9));
        Page<Category> page = categoryService.searchCategories(1);
        Assertions.assertEquals(1L, page.getTotalElements());
        Assertions.assertEquals(1, page.getTotalPages());
        Assertions.assertEquals(CATEGORY_TITLE, page.get().findFirst().orElse(new Category()).getTitle());
        Assertions.assertEquals(CATEGORY_DESCRIPTION, page.get().findFirst().orElse(new Category()).getDescription());
        Assertions.assertEquals(1L, page.get().findFirst().orElse(new Category()).getId());
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll(PageRequest.of(0, 9));
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(category)).when(categoryRepository).findById(1L);
        Category newCategory = categoryService.findById(1L).orElse(new Category());
        Assertions.assertEquals(1L, newCategory.getId());
        Assertions.assertEquals(CATEGORY_TITLE, newCategory.getTitle());
        Assertions.assertEquals(CATEGORY_DESCRIPTION, newCategory.getDescription());
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void deleteByIdTest() {
        categoryService.deleteById(1L);
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void tryToSaveTest() {
        Mockito.doReturn(category).when(categoryConverter).dtoToEntity(categoryDto);
        Mockito.doReturn(category).when(categoryRepository).save(category);
        Category savedCategory = categoryService.tryToSave(categoryDto, bindingResult);
        Assertions.assertEquals(1L, savedCategory.getId());
        Assertions.assertEquals(CATEGORY_TITLE, savedCategory.getTitle());
        Assertions.assertEquals(CATEGORY_DESCRIPTION, savedCategory.getDescription());
        Mockito.verify(categoryConverter, Mockito.times(1)).dtoToEntity(categoryDto);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(category);
    }
}
