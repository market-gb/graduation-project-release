package ru.nhp.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nhp.analytics.repositories.ProductRepository;
import ru.nhp.api.dto.core.ProductDto;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ProductAnaliticService {
    private final ProductRepository productRepository;

    public void addProduct (ProductDto product) {
        productRepository.add(product);
    }

    public HashMap<String, Long> getProductAnalitic () {
        return productRepository.getProductsAnalitic();
    }

}
