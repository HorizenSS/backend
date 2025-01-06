package com.ines.service;

import com.ines.controller.WebSocketController;
import com.ines.dto.AlertDto;
import com.ines.dto.AlertResponseDto;
import com.ines.exception.ResourceNotFoundException;
import com.ines.mapper.CustomerDTOMapper;
import com.ines.model.Alert;
import com.ines.model.Customer;
import com.ines.repository.AlertRepository;
import com.ines.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;
    private final WebSocketController webSocketController;

    public AlertResponseDto getAlertById(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        return mapToResponseDto(alert);
    }

    public List<AlertResponseDto> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public AlertResponseDto createAlert(AlertDto alertDto, String email) {
        Customer user = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Alert alert = Alert.builder()
                .title(alertDto.getTitle())
                .description(alertDto.getDescription())
                .type(alertDto.getType())
                .latitude(alertDto.getLatitude())
                .longitude(alertDto.getLongitude())
                .user(user)
                .status(Alert.AlertStatus.ACTIVE)
                .severity(alertDto.getSeverity())
                .build();
        webSocketController.notifyNearbyUsers(alert);
        return mapToResponseDto(alertRepository.save(alert));
    }

    public AlertResponseDto updateAlert(Long id, AlertDto alertDto, String email) {
        Customer user = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));

        if (!alert.getUser().getId().equals(user.getId())) {
            throw new InsufficientAuthenticationException("Not authorized to update this alert");
        }

        alert.setTitle(alertDto.getTitle());
        alert.setDescription(alertDto.getDescription());
        alert.setType(alertDto.getType());
        alert.setLatitude(alertDto.getLatitude());
        alert.setLongitude(alertDto.getLongitude());
        alert.setSeverity(alertDto.getSeverity());
        alert.setUpdatedAt(LocalDateTime.now());

        return mapToResponseDto(alertRepository.save(alert));
    }

    public void deleteAlert(Long id, String email) {
        Customer user = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));

        if (!alert.getUser().getId().equals(user.getId())) {
            throw new InsufficientAuthenticationException("Not authorized to delete this alert");
        }

        alertRepository.delete(alert);
    }

    public AlertResponseDto updateAlertStatus(Long id, Alert.AlertStatus status, String email) {
        Customer user = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));

        if (!alert.getUser().getEmail().equals(email)) {
            throw new InsufficientAuthenticationException("Not authorized to update this alert status");
        }

        alert.setStatus(status);
        alert.setUpdatedAt(LocalDateTime.now());
        return mapToResponseDto(alertRepository.save(alert));
    }

    public List<AlertResponseDto> getNearbyAlerts(Double latitude, Double longitude, Double radiusInKm) {
        return alertRepository.findNearbyAlerts(latitude, longitude, radiusInKm)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<AlertResponseDto> getUserAlerts(String email) {
        Customer user = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return alertRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private AlertResponseDto mapToResponseDto(Alert alert) {
        return AlertResponseDto.builder()
                .id(alert.getId())
                .title(alert.getTitle())
                .description(alert.getDescription())
                .type(alert.getType())
                .status(alert.getStatus())
                .latitude(alert.getLatitude())
                .longitude(alert.getLongitude())
                .user(alert.getUser() != null ? customerDTOMapper.apply(alert.getUser()) : null)
                .createdAt(alert.getCreatedAt())
                .updatedAt(alert.getUpdatedAt())
                .severity(alert.getSeverity())
                .build();
    }

    public List<AlertResponseDto> getUserAlerts(Integer userId) {
        Customer user = customerRepository.findCustomerById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return alertRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }
}
