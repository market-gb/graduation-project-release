package ru.nhp.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.entities.Category;
import ru.nhp.core.entities.Product;
import ru.nhp.core.services.CategoryService;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final CategoryService categoryService;

    public Set<Long> setEntitiesToSetId(Set<Category> categories) {
        return categories.stream().map(Category::getId).collect(Collectors.toSet());
    }

    public Set<Category> setIdToSetCategory(Set<Long> setId) {
        return setId.stream()
                .map(i -> categoryService.findById(i)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Категория не найдена, идентификатор: " + i)))
                .collect(Collectors.toSet());
    }

    public Product dtoToEntity(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .title(productDto.getTitle())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .pathname(productDto.getPathname())
                .categories(setIdToSetCategory(productDto.getGroupId()))
                .createdAt(productDto.getCreatedAt())
                .updatedAt(productDto.getUpdatedAt())
                .build();
    }

    public ProductDto entityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .pathname(product.getPathname())
                .groupId(setEntitiesToSetId(product.getCategories()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
