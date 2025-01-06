package com.ines.repository;

import com.ines.model.Alert;
import com.ines.model.Customer;
import com.ines.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AlertRepositoryTest {

    @Mock
    private AlertRepository alertRepository;

    private Customer mockCustomer;
    private Alert mockAlert1;
    private Alert mockAlert2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockCustomer = new Customer(1, "Alex", "alex@example.com", "password", 25, Gender.MALE);

        mockAlert1 = new Alert(1L, "Test Alert 1", 40.7128, -74.0060, "ACTIVE");
        mockAlert2 = new Alert(2L, "Test Alert 2", 40.7138, -74.0070, "ACTIVE");
    }

    @Test
    void findByUserOrderByCreatedAtDesc_shouldReturnAlertsForGivenUser() {
        when(alertRepository.findByUserOrderByCreatedAtDesc(mockCustomer))
                .thenReturn(Arrays.asList(mockAlert1, mockAlert2));


        List<Alert> alerts = alertRepository.findByUserOrderByCreatedAtDesc(mockCustomer);


        assertThat(alerts).hasSize(2);
        assertThat(alerts).containsExactly(mockAlert1, mockAlert2);

        verify(alertRepository, times(1)).findByUserOrderByCreatedAtDesc(mockCustomer);
    }

    @Test
    void findNearbyAlerts_shouldReturnNearbyAlertsWithinRadius() {
        // Arrange
        Double latitude = 40.7128;
        Double longitude = -74.0060;
        Double radius = 10.0; // 10 km

        when(alertRepository.findNearbyAlerts(latitude, longitude, radius))
                .thenReturn(Arrays.asList(mockAlert1, mockAlert2));

        // Act
        List<Alert> alerts = alertRepository.findNearbyAlerts(latitude, longitude, radius);

        // Assert
        assertThat(alerts).hasSize(2);
        assertThat(alerts).containsExactly(mockAlert1, mockAlert2);

        verify(alertRepository, times(1)).findNearbyAlerts(latitude, longitude, radius);
    }
}
