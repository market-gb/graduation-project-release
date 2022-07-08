package ru.nhp.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nhp.api.dto.core.OrderDto;
import ru.nhp.core.entities.Order;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final OrderItemConverter orderItemConverter;

    public OrderDto entityToDto(Order order) {
        OrderDto out = new OrderDto();
        out.setId(order.getId());
        out.setAddress(order.getAddress());
        out.setPhone(order.getPhone());
        out.setTotalPrice(order.getTotalPrice());
        out.setUsername(order.getUsername());
        out.setItems(orderItemConverter.setEntitiesToSetDto(order.getItems()));
        out.setOrderStatus(order.getOrderStatus());
        return out;
    }
}
