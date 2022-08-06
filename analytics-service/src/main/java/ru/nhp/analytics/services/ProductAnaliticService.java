package ru.nhp.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nhp.analytics.integrations.ProductServiceIntegration;
import ru.nhp.analytics.repositories.ProductAnaliticRepository;
import ru.nhp.api.dto.analitics.ProductAnaliticsDto;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
                    ProductDto productDto =  productServiceIntegration.findProductById(key).orElseThrow(() -> new ResourceNotFoundException("Аналитика рушится. Продукт не найден, id: " + key));
                    productAnalitics
                            .add(new ProductAnaliticsDto(
                                   productDto.getTitle(),
                                    val)
                            );
                }
        );

        return productAnalitics;
    }

    public List<ProductAnaliticsDto> getCategoriesAnalitic () {
        HashMap<Long, Long> products = productAnaliticRepository.getProductsAnalitic();
        List<ProductAnaliticsDto> productAnalitics = new ArrayList<>();
        products.forEach((key, val) -> {
                    ProductDto productDto =  productServiceIntegration.findProductById(key).orElseThrow(() -> new ResourceNotFoundException("Аналитика рушится. Продукт не найден, id: " + key));
                    Set<Long> categoruIds= productDto.getGroupId();
                    categoruIds.forEach(id -> {
                        CategoryDto categoryDto = productServiceIntegration.findCategoryById(id).orElseThrow(() -> new ResourceNotFoundException("Аналитика рушится. Категория не найдена, id: " + key));
                        int index = getIndexCategoryInList(productAnalitics, categoryDto);
                        if (index == -1) {
                            productAnalitics
                                    .add(new ProductAnaliticsDto(
                                            categoryDto.getTitle(),
                                            val)
                                    );
                        } else {
                            ProductAnaliticsDto dto = productAnalitics.get(index);
                            productAnalitics.get(index).setQuantity(dto.getQuantity()+val);
                        }
                    });
                }
        );

        return productAnalitics;
    }

    private int getIndexCategoryInList (List<ProductAnaliticsDto> list, CategoryDto dto) {
        for (int i=0; i < list.size(); i++) {
            if (list.get(i).getTitle().equals(dto.getTitle()))
                return i;
        }
        return -1;
    }

}
