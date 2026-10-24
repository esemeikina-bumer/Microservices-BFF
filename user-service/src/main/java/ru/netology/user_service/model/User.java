package ru.netology.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String fullName;        // ФИО
    private String deliveryAddress; // адрес доставки
    private String phoneNumber;     // номер телефона
    private String email;           // email
}