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
        return new Product(productDto.getId(), productDto.getTitle(), productDto.getPrice(), categoryConverter.setDtoToSetEntities(productDto.getCategories()));
    }

    public ProductDto entityToDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice(), categoryConverter.setEntitiesToSetDto(product.getCategories()));
    }
}
