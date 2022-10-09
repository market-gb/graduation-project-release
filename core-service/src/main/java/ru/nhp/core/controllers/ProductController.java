package ru.nhp.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.converters.ProductConverter;
import ru.nhp.core.entities.Product;
import ru.nhp.core.services.ProductService;
import ru.nhp.core.services.StorageService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Товары", description = "Методы работы с товарами")
public class ProductController {
    private final ProductService productService;
    private final ProductConverter productConverter;
    private final StorageService storageService;

    @Operation(
            summary = "Запрос на получение страницы товаров",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping
    public Page<ProductDto> searchProducts(
            @RequestParam(name = "p", defaultValue = "1") Integer page,
            @RequestParam(name = "min_price", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "title_part", required = false) String titlePart,
            @RequestParam(name = "category_title", required = false) String categoryTitle,
            @RequestParam(name = "category_id", required = false) Long categoryId,
            @RequestParam(name = "page_size", defaultValue = "9") Integer pageSize) {
        if (page < 1) {
            page = 1;
        }
        return productService.searchProducts(minPrice, maxPrice, titlePart, categoryTitle, categoryId, page, pageSize).map(
                productConverter::entityToDto);
    }

    @Operation(
            summary = "Запрос на получение товара по идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ProductDto getById(
            @PathVariable @Parameter(description = "Идентификатор товара", required = true) Long id) {
        Product product = productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Товар не найден, идентификатор: " + id));
        return productConverter.entityToDto(product);
    }

    @Operation(
            summary = "Создание нового товара",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ProductDto save(@RequestParam("price") String price,
                           @RequestParam("groupId") String groupId,
                           @RequestParam("description") String description,
                           @RequestParam("title") String title,
                           @RequestParam("file") MultipartFile file) {

        //TODO Сделать проверку полей на валидность

        ProductDto productDto = new ProductDto();
        productDto.setTitle(title);
        productDto.setDescription(description);
        productDto.setGroupId(Set.of(Long.parseLong(groupId)));
        productDto.setPrice(new BigDecimal(price));
        productDto.setImageId(storageService.store(file, "product", ".jpg"));
        return productConverter.entityToDto(productService.tryToSave(productDto));
    }

    @Operation(
            summary = "Изменение товара",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PutMapping
    @Transactional
    public ProductDto update(@RequestBody @Parameter(description = "Изменённый товар", required = true) @Valid ProductDto productDto,
                             @Parameter(description = "Ошибки валидации", required = true) BindingResult bindingResult) {
        return productConverter.entityToDto(productService.tryToSave(productDto, bindingResult));
    }

    @Operation(
            summary = "Удаление товара",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteById(@PathVariable @Parameter(description = "Идентификатор товара", required = true) Long id) {
        storageService.delete((Objects.requireNonNull(productService.findById(id).orElse(null))).getImageId(), "product");
        productService.deleteById(id);
    }
}
