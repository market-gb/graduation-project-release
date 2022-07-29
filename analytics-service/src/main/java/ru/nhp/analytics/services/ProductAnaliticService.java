package ru.nhp.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nhp.analytics.repositories.ProductRepository;
import ru.nhp.api.dto.core.ProductAnaliticsDto;
import ru.nhp.api.dto.core.ProductDto;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAnaliticService {
    private final ProductRepository productRepository;

    public ProductDto addProduct (ProductDto product) {
        return productRepository.add(product);
    }

    public List<ProductAnaliticsDto> getProductAnalitic () {
        return productRepository.getProductsAnalitic();
    }

}
