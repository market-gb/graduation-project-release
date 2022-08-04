package ru.nhp.analytics.repositories;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class ProductAnaliticRepository {
    private final HashMap<Long, Long> products = new HashMap<>();

    public void add (Long productId) {
        if (products.get(productId) == null) {
            products.put(productId, 1L);
        } else {
            products.replace(productId, products.get(productId) + 1L);
        }
    }

    public HashMap<Long, Long> getProductsAnalitic () {
        return products;
    }
}
