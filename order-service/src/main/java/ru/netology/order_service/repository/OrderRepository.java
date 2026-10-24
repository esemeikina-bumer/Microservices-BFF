package ru.netology.order_service.repository;

import ru.netology.order_service.model.Order;
import ru.netology.order_service.model.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    public OrderRepository() {
        orders.add(new Order(101L, 1L, 1500.0, "RUB", List.of(
                new OrderItem("Книга", 1, 500.0),
                new OrderItem("Ручка", 2, 500.0)
        )));
        orders.add(new Order(102L, 1L, 3200.0, "RUB", List.of(
                new OrderItem("Монитор", 1, 3000.0),
                new OrderItem("Кабель HDMI", 1, 200.0)
        )));
        orders.add(new Order(103L, 2L, 800.0, "RUB", List.of(
                new OrderItem("Чехол", 1, 800.0)
        )));
        orders.add(new Order(104L, 3L, 45.0, "USD", List.of(
                new OrderItem("Электронная книга", 1, 45.0)
        )));
    }

    public List<Order> findByUserId(Long userId) {
        return orders.stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}