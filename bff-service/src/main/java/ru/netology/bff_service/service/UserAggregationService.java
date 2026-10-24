package ru.netology.bff_service.service;

import ru.netology.bff_service.dto.OrderDto;
import ru.netology.bff_service.dto.UserDto;
import ru.netology.bff_service.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAggregationService {

    private final RestClient userServiceClient;
    private final RestClient orderServiceClient;

    public UserProfileDto getUserProfile(Long userId) {
        // Параллельно запрашиваем данные из двух сервисов
        CompletableFuture<UserDto> userFuture = CompletableFuture.supplyAsync(() ->
                userServiceClient.get()
                        .uri("/api/users/{id}", userId)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, (req, res) -> {
                            throw new RuntimeException(
                                    "User service error: " + res.getStatusCode());
                        })
                        .body(UserDto.class)
        );

        CompletableFuture<List<OrderDto>> ordersFuture = CompletableFuture.supplyAsync(() ->
                orderServiceClient.get()
                        .uri("/api/orders/by-user/{id}", userId)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, (req, res) -> {
                            throw new RuntimeException(
                                    "Order service error: " + res.getStatusCode());
                        })
                        .body(new ParameterizedTypeReference<List<OrderDto>>() {})
        );

        CompletableFuture.allOf(userFuture, ordersFuture).join();

        UserDto user = userFuture.join();
        List<OrderDto> orders = ordersFuture.join();

        return new UserProfileDto(user, orders);
    }
}