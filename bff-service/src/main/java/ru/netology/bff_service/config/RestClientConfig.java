package ru.netology.bff_service.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${services.user-url}")
    private String userServiceUrl;

    @Value("${services.order-url}")
    private String orderServiceUrl;

    @Bean
    public RestClient userServiceClient() {
        return RestClient.builder().baseUrl(userServiceUrl).build();
    }

    @Bean
    public RestClient orderServiceClient() {
        return RestClient.builder().baseUrl(orderServiceUrl).build();
    }
}