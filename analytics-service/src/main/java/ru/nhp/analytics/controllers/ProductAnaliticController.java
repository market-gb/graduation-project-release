package ru.nhp.analytics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nhp.analytics.services.ProductAnaliticService;
import ru.nhp.api.dto.analitics.ProductAnaliticsDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analitics")
@RequiredArgsConstructor
@Tag(name = "Аналитика товаров", description = "Методы сбора статистики и получения аналитики по товарам")
public class ProductAnaliticController {
    private final ProductAnaliticService productAnaliticService;

    @Operation(
            summary = "Добавление продукта в статистику",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/add/{id}")
    public void addProduct (@PathVariable Long id) {
        productAnaliticService.addProduct(id);
    }

    @Operation(
            summary = "Выдача аналитики по продуктам: список товаров с количеством покупок",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/products")
    public List<ProductAnaliticsDto> getListProducts () {
        return productAnaliticService.getProductAnalitic();
    }

    @Operation(
            summary = "Выдача аналитики по категориям: список категорий с количеством покупок товаров с каждой категории",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/categories")
    public List<ProductAnaliticsDto> getListCategories () {
        return productAnaliticService.getCategoriesAnalitic();
    }

}
