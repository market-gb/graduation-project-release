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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.converters.CategoryConverter;
import ru.nhp.core.entities.Category;
import ru.nhp.core.services.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Категории", description = "Методы работы с категориями")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryConverter categoryConverter;

    @Operation(
            summary = "Запрос на получение страницы категорий",
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
    public Page<CategoryDto> getAll(
            @RequestParam(name = "p", defaultValue = "1") Integer page) {
        if (page < 1) {
            page = 1;
        }
        return categoryService.findAll(page).map(
                categoryConverter::entityToDto);
    }

    @Operation(
            summary = "Запрос на получение категории по идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public CategoryDto getById(
            @PathVariable @Parameter(description = "Идентификатор категории", required = true) Long id) {
        Category category = categoryService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Категория не найдена, идентификатор: " + id));
        return categoryConverter.entityToDto(category);
    }

    @Operation(
            summary = "Создание новой категории",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = CategoryDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@RequestBody @Parameter(description = "Новая категория", required = true) @Valid CategoryDto categoryDto,
                            @Parameter(description = "Ошибки валидации", required = true) BindingResult bindingResult) {
        return categoryConverter.entityToDto(categoryService.tryToSave(categoryDto, bindingResult));
    }

    @Operation(
            summary = "Изменение категории",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = CategoryDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PatchMapping
    public CategoryDto update(@RequestBody @Parameter(description = "Изменённая категория", required = true) @Valid CategoryDto categoryDto,
                              @Parameter(description = "Ошибки валидации", required = true) BindingResult bindingResult) {
        return categoryConverter.entityToDto(categoryService.tryToSave(categoryDto, bindingResult));
    }

    @Operation(
            summary = "Удаление категории",
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
    public void deleteById(@PathVariable @Parameter(description = "Идентификатор категории", required = true) Long id) {
        categoryService.deleteById(id);
    }
}
