package com.ines.controller;

import com.ines.dto.AlertDto;
import com.ines.dto.AlertResponseDto;
import com.ines.dto.AlertStatusDto;
import com.ines.dto.ApiResponse;
import com.ines.service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

        @PostMapping
        public ResponseEntity<ApiResponse<AlertResponseDto>> createAlert(
                @Valid @RequestBody AlertDto alertDto,
                @AuthenticationPrincipal UserDetails userDetails) {
            AlertResponseDto alert = alertService.createAlert(alertDto, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(alert, "Alert created successfully."));
        }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertResponseDto>> getAlertById(@PathVariable Long id) {
        AlertResponseDto alert = alertService.getAlertById(id);
        return ResponseEntity.ok(ApiResponse.success(alert, "Alert retrieved successfully."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AlertResponseDto>>> getAllAlerts() {
        List<AlertResponseDto> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts, "All alerts retrieved successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertResponseDto>> updateAlert(
            @PathVariable Long id,
            @Valid @RequestBody AlertDto alertDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        AlertResponseDto alert = alertService.updateAlert(id, alertDto,userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(alert, "Alert updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAlert(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        alertService.deleteAlert(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null, "Alert deleted successfully."));
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<AlertResponseDto>>> getNearbyAlerts(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double radius) {
        List<AlertResponseDto> alerts = alertService.getNearbyAlerts(latitude, longitude, radius);
        return ResponseEntity.ok(ApiResponse.success(alerts, "Nearby alerts retrieved successfully."));
    }

    @GetMapping("/my-alerts")
    public ResponseEntity<ApiResponse<List<AlertResponseDto>>> getUserAlerts(@AuthenticationPrincipal UserDetails userDetails) {
        List<AlertResponseDto> alerts = alertService.getUserAlerts(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(alerts, "User alerts retrieved successfully."));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlertResponseDto>> getAlertsByUser(@PathVariable Integer userId) {
        List<AlertResponseDto> alerts = alertService.getUserAlerts(userId);
        return ResponseEntity.ok(alerts);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AlertResponseDto>> updateAlertStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlertStatusDto statusDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        AlertResponseDto alert = alertService.updateAlertStatus(id, statusDto.getStatus(), userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(alert, "Alert status updated successfully."));
    }
}
