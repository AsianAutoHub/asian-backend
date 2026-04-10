package com.asian.auto.hub.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.asian.auto.hub.apiresponse.ApiResponse;
import com.asian.auto.hub.dto.UserMetricsResponseDto;
import com.asian.auto.hub.serviceimpl.UserMetricsServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/user-metrics")
@RequiredArgsConstructor
public class UserMetricsController {

    private final UserMetricsServiceImpl userMetricsService;

    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserMetricsResponseDto>> getUserMetrics(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                ApiResponse.success(userMetricsService.getUserMetrics(userId)));
    }

    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserMetricsResponseDto>>> getAllMetrics() {
        return ResponseEntity.ok(
                ApiResponse.success(userMetricsService.getAllUsersMetrics()));
    }
}
