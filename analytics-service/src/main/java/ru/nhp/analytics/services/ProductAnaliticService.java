package ru.nhp.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nhp.analytics.integrations.ProductServiceIntegration;
import ru.nhp.analytics.repositories.ProductAnaliticRepository;
import ru.nhp.api.dto.analitics.ProductAnaliticsDto;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAnaliticService {
    private final ProductAnaliticRepository productAnaliticRepository;
    private final ProductServiceIntegration productServiceIntegration;

    public void addProduct (Long productId) {
        productAnaliticRepository.add(productId);
    }

    public List<ProductAnaliticsDto> getProductAnalitic () {
        HashMap<Long, Long> products = productAnaliticRepository.getProductsAnalitic();
        List<ProductAnaliticsDto> productAnalitics = new ArrayList<>();
        products.forEach((key, val) -> {
                    ProductDto productDto =  productServiceIntegration.findById(key).orElseThrow(() -> new ResourceNotFoundException("Аналитика рушится. Продукт не найдет, id: " + key));
                        productAnalitics
                            .add(new ProductAnaliticsDto(
                                   productDto.getTitle(),
                                    val)
                            );
                }
        );

        return productAnalitics;
    }

}
