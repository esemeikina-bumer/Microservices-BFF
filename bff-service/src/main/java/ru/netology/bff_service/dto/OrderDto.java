package ru.netology.bff_service.dto;



import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Double totalAmount;
    private String currency;
    private List<OrderItemDto> items;
}