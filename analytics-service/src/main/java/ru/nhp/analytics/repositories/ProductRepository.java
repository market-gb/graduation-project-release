package ru.nhp.analytics.repositories;

import org.springframework.stereotype.Repository;
import ru.nhp.api.dto.core.ProductDto;

import java.util.HashMap;

@Repository
public class ProductRepository {
    private final HashMap<ProductDto, Long> products = new HashMap<>();

    public void add (ProductDto product) {
        if (products.get(product) == null) {
            products.put(product, 1L);
        } else {
            products.replace(product, products.get(product) + 1L);
        }
    }

    public HashMap<String, Long> getProductsAnalitic () {
        HashMap<String, Long> productsAnalitic = new HashMap<>();
        products.forEach((key, val) -> productsAnalitic.put(key.getTitle(),val));
        return productsAnalitic;
    }

    public Long getQuantityOneProduct(ProductDto product) {
        return products.get(product);
    }



}
