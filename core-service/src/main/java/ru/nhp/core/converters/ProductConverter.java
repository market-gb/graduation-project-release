package ru.nhp.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.core.entities.Product;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final CategoryConverter categoryConverter;

    public Product dtoToEntity(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .title(productDto.getTitle())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .pathname(productDto.getPathname())
                .categories(categoryConverter.setIdToSetCategory(productDto.getGroupId()))
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
                .groupId(categoryConverter.setEntitiesToSetId(product.getCategories()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
