package ru.nhp.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Модель продукта")
public class ProductDto {
    @Schema(description = "ID продукта", required = true, example = "1")
    private Long id;

    @Schema(description = "Название продукта", required = true, maxLength = 255, minLength = 3, example = "Коробка конфет")
    private String title;

    @Schema(description = "Цена продукта", required = true, example = "120")
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductDto() {
    }

    public ProductDto(Long id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }
}
