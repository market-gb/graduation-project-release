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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.converters.ProductConverter;
import ru.nhp.core.entities.Product;
import ru.nhp.core.exceptions.CoreAppError;
import ru.nhp.core.services.ProductService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Товары", description = "Методы работы с товарами")
public class ProductController {
    private final ProductService productService;
    private final ProductConverter productConverter;

    @Operation(
            summary = "Запрос на получение страницы товаров",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @GetMapping
    public Page<ProductDto> getAll(
            @RequestParam(name = "p", defaultValue = "1") Integer page,
            @RequestParam(name = "min_price", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "title_part", required = false) String titlePart,
            @RequestParam(name = "category_title", required = false) String categoryTitle) {
        if (page < 1) {
            page = 1;
        }
        return productService.findAll(minPrice, maxPrice, titlePart, categoryTitle, page).map(
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
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
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
            summary = "Запрос на получение товаров по идентификатору категории",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @GetMapping("category/{id}")
    public Page<ProductDto> getProductsByCategoryId(
            @PathVariable @Parameter(description = "Идентификатор категории", required = true) Long id,
            @RequestParam(name = "p", defaultValue = "1") Integer page) {
        return productService.findAllByCategoryId(id, page).map(
                productConverter::entityToDto);
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
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto save(@RequestBody @Parameter(description = "Новый товар", required = true) @Valid ProductDto productDto,
                           @Parameter(description = "Ошибки валидации", required = true) BindingResult bindingResult) {
        return productConverter.entityToDto(productService.tryToSave(productDto, bindingResult));
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
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @PutMapping
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
                            content = @Content(schema = @Schema(implementation = CoreAppError.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Parameter(description = "Идентификатор товара", required = true) Long id) {
        productService.deleteById(id);
    }
}
