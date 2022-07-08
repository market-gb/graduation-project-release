package ru.nhp.core.product_test;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;
import ru.nhp.core.converters.ProductConverter;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.core.entities.Category;
import ru.nhp.core.entities.Product;
import ru.nhp.core.repositories.ProductRepository;
import ru.nhp.core.services.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(classes = {ProductService.class})
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private ProductConverter productConverter;
    private final static String CATEGORY_TITLE = "test_category_title";
    private final static String TITLE = "Milk";
    private final static BigDecimal PRICE = BigDecimal.valueOf(100);
    private static Product product;
    private static ProductDto productDto;
    private static Page<Product> productPage;

    @BeforeAll
    public static void initEntities() {
        product = new Product();
        product.setId(1L);
        product.setTitle(TITLE);
        product.setPrice(PRICE);
        product.setCategories(Set.of(new Category(1L, CATEGORY_TITLE)));
        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle(TITLE);
        productDto.setPrice(PRICE);
        productDto.setCategories(Set.of(new CategoryDto(1L, CATEGORY_TITLE)));
        productPage = new PageImpl<>(List.of(product));
    }

    @Test
    public void findAllTest() {
        Specification<Product> spec = Specification.where(null);
        Mockito.doReturn(productPage).when(productRepository).findAll(spec, PageRequest.of(0, 8));
        Page<Product> page = productService.findAll(null, null, null, null, 1);
        Assertions.assertEquals(1L, page.getTotalElements());
        Assertions.assertEquals(1, page.getTotalPages());
        Assertions.assertEquals(TITLE, page.get().findFirst().orElse(new Product()).getTitle());
        Assertions.assertEquals(PRICE, page.get().findFirst().orElse(new Product()).getPrice());
        Assertions.assertEquals(1L, page.get().findFirst().orElse(new Product()).getId());
        Mockito.verify(productRepository, Mockito.times(1)).findAll(spec, PageRequest.of(0, 8));
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(product)).when(productRepository).findById(1L);
        Product newProduct = productService.findById(1L).orElse(new Product());
        Assertions.assertEquals(1L, newProduct.getId());
        Assertions.assertEquals(TITLE, newProduct.getTitle());
        Assertions.assertEquals(PRICE, newProduct.getPrice());
        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void deleteByIdTest() {
        productService.deleteById(1L);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void tryToSaveTest() {
        Mockito.doReturn(product).when(productConverter).dtoToEntity(productDto);
        Mockito.doReturn(product).when(productRepository).save(product);
        Product savedProduct = productService.tryToSave(productDto, bindingResult);
        Assertions.assertEquals(1L, savedProduct.getId());
        Assertions.assertEquals(TITLE, savedProduct.getTitle());
        Assertions.assertEquals(PRICE, savedProduct.getPrice());
        Mockito.verify(productConverter, Mockito.times(1)).dtoToEntity(productDto);
        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }
}
