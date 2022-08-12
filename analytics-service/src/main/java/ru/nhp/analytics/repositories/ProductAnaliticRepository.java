package ru.nhp.analytics.repositories;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductAnaliticRepository {
    private final ConcurrentHashMap<String, Integer> products = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> orderStatuses = new ConcurrentHashMap<>();

    public void addProduct(String title, int value) {
        addToAnalitic(products, title, value);
    }

    public void addUserProduct (String title, int value) {
        addToAnalitic(users, title, value);
    }

    public void addOrderStatus (String title, int value) {
        addToAnalitic(orderStatuses, title, value);
    }

    private void addToAnalitic(ConcurrentHashMap<String, Integer> analiticList, String title, int value) {
        if (analiticList.containsKey(title)) {
            analiticList.replace(title, analiticList.get(title) + value);
        } else {
            analiticList.put(title, value);
        }
    }

    public ConcurrentHashMap<String, Integer> getProductsAnalitic () {
        return products;
    }

    public ConcurrentHashMap<String, Integer> getUsersAnalitic () {
        return users;
    }

    public ConcurrentHashMap<String, Integer> getOrderStatusesAnalitic () {
        return orderStatuses;
    }

}
