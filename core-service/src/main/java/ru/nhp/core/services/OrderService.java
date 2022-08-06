package ru.nhp.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nhp.api.dto.cart.CartDto;
import ru.nhp.api.dto.core.OrderDetailsDto;
import ru.nhp.api.dto.core.enums.OrderStatus;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.entities.Order;
import ru.nhp.core.entities.OrderItem;
import ru.nhp.core.integrations.CartServiceIntegration;
import ru.nhp.core.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository ordersRepository;
    private final CartServiceIntegration cartServiceIntegration;
    private final ProductService productsService;

    @Transactional
    public Order save(String username, OrderDetailsDto orderDetailsDto) {
        if (username == null || orderDetailsDto == null) {
            throw new InvalidParamsException("Невалидные параметры");
        }
        CartDto currentCart = cartServiceIntegration.getUserCart(username);
        Order order = new Order();
        order.setFullName(orderDetailsDto.getFullName());
        order.setAddress(orderDetailsDto.getAddress());
        order.setPhone(orderDetailsDto.getPhone());
        order.setUsername(username);
        order.setTotalPrice(currentCart.getTotalPrice());
        order.setOrderStatus(OrderStatus.CREATED);
        Set<OrderItem> items = currentCart.getItems().stream()
                .map(i -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuantity(i.getQuantity());
                    item.setPricePerProduct(i.getPricePerProduct());
                    item.setPrice(i.getPrice());
                    item.setProduct(productsService.findById(i.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Товар не найден")));
                    return item;
                }).collect(Collectors.toSet());
        order.setItems(items);
        ordersRepository.save(order);
        cartServiceIntegration.clearUserCart(username);
        return order;
    }

    public List<Order> findAllByUsername(String username) {
        if (username == null) {
            throw new InvalidParamsException("Невалидные параметры");
        }
        return ordersRepository.findAllByUsername(username);
    }

    public Optional<Order> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return ordersRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new InvalidParamsException("Невалидный параметр идентификатор:" + null);
        }
        try {
            ordersRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка удаления заказа. Заказ " + id + "не существует");
        }
    }

    @Transactional
    public void changeStatus(String orderStatusName, Long id) {
        if (orderStatusName == null || id == null) {
            throw new InvalidParamsException("Невалидные параметры");
        }
        OrderStatus orderStatus;
        try{
            orderStatus = OrderStatus.valueOf(orderStatusName);
        }catch (Exception e){
            throw new ResourceNotFoundException("Ошибка изменения статуса заказа. Статус " + orderStatusName + "не существует");
        }
        try {
            ordersRepository.changeStatus(orderStatus, id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка изменения статуса заказа. Заказ " + id + "не существует");
        }
    }
}
