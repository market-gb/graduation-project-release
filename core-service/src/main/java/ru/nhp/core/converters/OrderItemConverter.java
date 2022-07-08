package ru.nhp.core.converters;

import org.springframework.stereotype.Component;
import ru.nhp.api.dto.core.OrderItemDto;
import ru.nhp.core.entities.OrderItem;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderItemConverter {

    public Set<OrderItemDto> setEntitiesToSetDto(Set<OrderItem> orderItems) {
        return orderItems.stream().map(this::entityToDto).collect(Collectors.toSet());
    }

    private OrderItemDto entityToDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProduct().getId(),
                orderItem.getProduct().getTitle(), orderItem.getQuantity(),
                orderItem.getPricePerProduct(), orderItem.getPrice());
    }
}
