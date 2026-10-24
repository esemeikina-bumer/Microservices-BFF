package ru.netology.bff_service.controller;


import ru.netology.bff_service.dto.UserProfileDto;
import ru.netology.bff_service.service.UserAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site-bff")
@RequiredArgsConstructor
public class SiteBffController {

    private final UserAggregationService userAggregationService;

    @GetMapping("/user/{userId}")
    public UserProfileDto getUserProfile(@PathVariable Long userId) {
        return userAggregationService.getUserProfile(userId);
    }
}
