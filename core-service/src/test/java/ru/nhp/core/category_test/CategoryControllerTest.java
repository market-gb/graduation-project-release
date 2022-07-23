package ru.nhp.core.category_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.core.controllers.CategoryController;
import ru.nhp.core.converters.CategoryConverter;
import ru.nhp.core.entities.Category;
import ru.nhp.core.services.CategoryService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private CategoryConverter categoryConverter;
    private final static String CATEGORY_TITLE = "Category";
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
    public void getAllTest() throws Exception {
        given(categoryService.findAll(1)).willReturn(categoryPage);
        given(categoryConverter.entityToDto(category)).willReturn(categoryDto);
        mvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is(CATEGORY_TITLE)));
    }

    @Test
    public void getByIdTest() throws Exception {
        given(categoryService.findById(1L)).willReturn(Optional.of(category));
        given(categoryConverter.entityToDto(category)).willReturn(categoryDto);
        mvc.perform(get("/api/v1/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(CATEGORY_TITLE)))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void saveTest() throws Exception {
        given(categoryService.tryToSave(categoryDto, bindingResult)).willReturn(category);
        given(categoryConverter.entityToDto(category)).willReturn(categoryDto);
        mvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(categoryDto
                                )))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateTest() throws Exception {
        given(categoryService.tryToSave(categoryDto, bindingResult)).willReturn(category);
        given(categoryConverter.entityToDto(category)).willReturn(categoryDto);
        mvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(categoryDto
                                )))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteByIdTest() throws Exception {
        mvc.perform(delete("/api/v1/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
