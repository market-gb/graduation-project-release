package ru.nhp.analytics.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nhp.analytics.repositories.ProductAnaliticRepository;
import ru.nhp.api.dto.analitics.ProductAnaliticsDto;
import ru.nhp.api.dto.core.OrderDto;
import ru.nhp.api.dto.core.OrderItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ProductAnaliticService {
    private final ProductAnaliticRepository productAnaliticRepository;

    public void addAnalitics(OrderDto orderDto) {
        String userName = orderDto.getFullName();
        Set<OrderItemDto> orderItemDtos = orderDto.getItems();
        orderItemDtos.forEach(item -> {
            productAnaliticRepository.addProduct(item.getProductTitle(), item.getQuantity());
            productAnaliticRepository.addUserProduct(userName, item.getQuantity());
        });
        productAnaliticRepository.addOrderStatus(orderDto.getOrderStatus().name(), 1);
    }

    public List<ProductAnaliticsDto> getProductsAnalitic() {
        return getAnaliticsDtos(productAnaliticRepository.getProductsAnalitic());
    }

    public List<ProductAnaliticsDto> getUsersAnalitic () {
        return getAnaliticsDtos(productAnaliticRepository.getUsersAnalitic());
    }

    public List<ProductAnaliticsDto> getOrderStatusesAnalitic () {
        return getAnaliticsDtos(productAnaliticRepository.getOrderStatusesAnalitic());
    }

    private List<ProductAnaliticsDto> getAnaliticsDtos(ConcurrentHashMap<String, Integer> products) {
        List<ProductAnaliticsDto> productAnalitics = new ArrayList<>();
        products.forEach((key, val) -> productAnalitics.add(new ProductAnaliticsDto(key, val))
        );
        return productAnalitics;
    }
}
