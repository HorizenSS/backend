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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerDTOMapper customerDTOMapper;

    @Mock
    private WebSocketController webSocketController;

    @InjectMocks
    private AlertService alertService;

    private Alert alert;
    private Customer customer;
    private AlertDto alertDto;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1)
                .email("test@example.com")
                .build();

        alert = Alert.builder()
                .id(1L)
                .title("Test Alert")
                .description("Description")
                .type(Alert.AlertType.TRAFFIC)
                .latitude(40.7128)
                .longitude(-74.0060)
                .status(Alert.AlertStatus.ACTIVE)
                .user(customer)
                .severity(Alert.Severity.HIGH)
                .createdAt(LocalDateTime.now())
                .build();

        alertDto = new AlertDto("Test Alert", "Description", Alert.AlertType.TRAFFIC, Alert.Severity.HIGH, 40.7128, -74.0060);
    }

    @Test
    void testGetAlertById_Success() {
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));

        AlertResponseDto response = alertService.getAlertById(1L);

        assertNotNull(response);
        assertEquals("Test Alert", response.getTitle());
        verify(alertRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAlertById_ThrowsResourceNotFoundException() {
        when(alertRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alertService.getAlertById(1L));
        verify(alertRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllAlerts() {
        when(alertRepository.findAll()).thenReturn(List.of(alert));

        List<AlertResponseDto> alerts = alertService.getAllAlerts();

        assertNotNull(alerts);
        assertEquals(1, alerts.size());
        verify(alertRepository, times(1)).findAll();
    }

    @Test
    void testCreateAlert_Success() {
        when(customerRepository.findCustomerByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(alertRepository.save(any(Alert.class))).thenReturn(alert);

        AlertResponseDto response = alertService.createAlert(alertDto, "test@example.com");

        assertNotNull(response);
        assertEquals("Test Alert", response.getTitle());
        verify(customerRepository, times(1)).findCustomerByEmail("test@example.com");
        verify(alertRepository, times(1)).save(any(Alert.class));
        verify(webSocketController, times(1)).notifyNearbyUsers(any(Alert.class));
    }

    @Test
    void testCreateAlert_ThrowsResourceNotFoundException() {
        when(customerRepository.findCustomerByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alertService.createAlert(alertDto, "test@example.com"));
        verify(customerRepository, times(1)).findCustomerByEmail("test@example.com");
    }

    @Test
    void testUpdateAlert_Success() {
        when(customerRepository.findCustomerByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any(Alert.class))).thenReturn(alert);

        AlertResponseDto response = alertService.updateAlert(1L, alertDto, "test@example.com");

        assertNotNull(response);
        assertEquals("Test Alert", response.getTitle());
        verify(alertRepository, times(1)).findById(1L);
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void testUpdateAlert_ThrowsInsufficientAuthenticationException() {
        Customer anotherCustomer = Customer.builder().id(2).email("other@example.com").build();
        alert.setUser(anotherCustomer);

        when(customerRepository.findCustomerByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));

        assertThrows(InsufficientAuthenticationException.class, () ->
                alertService.updateAlert(1L, alertDto, "test@example.com"));
        verify(alertRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteAlert_Success() {
        when(customerRepository.findCustomerByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));

        alertService.deleteAlert(1L, "test@example.com");

        verify(alertRepository, times(1)).delete(alert);
    }

    @Test
    void testDeleteAlert_ThrowsInsufficientAuthenticationException() {
        Customer anotherCustomer = Customer.builder().id(2).email("other@example.com").build();
        alert.setUser(anotherCustomer);

        when(customerRepository.findCustomerByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));

        assertThrows(InsufficientAuthenticationException.class, () ->
                alertService.deleteAlert(1L, "test@example.com"));
        verify(alertRepository, times(1)).findById(1L);
    }
}
