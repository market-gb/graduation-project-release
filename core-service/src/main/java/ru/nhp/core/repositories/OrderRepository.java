package ru.nhp.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nhp.api.enums.OrderStatus;
import ru.nhp.core.entities.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.username = ?1")
    List<Order> findAllByUsername(String username);

    @Modifying
    @Query("update Order o set o.orderStatus = ?1, o.updatedAt = CURRENT_TIMESTAMP where o.id = ?2")
    void changeStatus(OrderStatus orderStatus, Long orderId);
}
