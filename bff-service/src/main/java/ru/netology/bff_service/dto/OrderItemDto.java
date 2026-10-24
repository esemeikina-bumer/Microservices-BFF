package ru.netology.bff_service.dto;



import lombok.Data;

@Data
public class OrderItemDto {
    private String productName;
    private Integer quantity;
    private Double price;
}
