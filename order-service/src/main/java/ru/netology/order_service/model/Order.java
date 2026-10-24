package ru.netology.order_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private Double totalAmount;   // сумма заказа
    private String currency;      // валюта заказа
    private List<OrderItem> items; // список позиций
}