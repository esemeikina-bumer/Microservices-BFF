package ru.netology.order_service.controller;



import ru.netology.order_service.model.Order;
import ru.netology.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping("/by-user/{user}")
    public List<Order> getOrdersByUser(@PathVariable("user") Long userId) {
        return orderRepository.findByUserId(userId);
    }
}