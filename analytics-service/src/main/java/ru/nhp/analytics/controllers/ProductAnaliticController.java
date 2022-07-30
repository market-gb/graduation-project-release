package ru.nhp.analytics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nhp.analytics.services.ProductAnaliticService;
import ru.nhp.api.dto.core.ProductAnaliticsDto;
import ru.nhp.api.dto.core.ProductDto;

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
    @PostMapping("/add") //TODO возвращает ошибку 404
    public void addProduct (@RequestBody ProductDto product) {
        productAnaliticService.addProduct(product);
    }

    @Operation(
            summary = "Выдача аналитики по продуктам: список товаров с количеством покупок",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/list") //TODO возвращает ошибку 404
    public List<ProductAnaliticsDto> getListProducts () {
        return productAnaliticService.getProductAnalitic();
    }





}
