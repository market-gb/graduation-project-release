package ru.nhp.analytics.repositories;

import org.springframework.stereotype.Repository;
import ru.nhp.api.dto.core.ProductAnaliticsDto;
import ru.nhp.api.dto.core.ProductDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ProductRepository {
    private final HashMap<ProductDto, Long> products = new HashMap<>();

    public ProductDto add (ProductDto product) {
        if (products.get(product) == null) {
            products.put(product, 1L);
        } else {
            products.replace(product, products.get(product) + 1L);
        }
        return product;
    }

    public List<ProductAnaliticsDto> getProductsAnalitic () {
        List<ProductAnaliticsDto> productAnaliticsDtos = new ArrayList<>();
        products.forEach((key, val) -> productAnaliticsDtos
                .add(new ProductAnaliticsDto(key.getTitle(), val))
        );
        return productAnaliticsDtos;
    }
}
