package com.ines.repository;

import com.ines.model.Customer;
import com.ines.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerRepositoryTest customerRepositoryTest;

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock customer
        mockCustomer = new Customer(1, "Alex", "alex@example.com", "password", 25, Gender.MALE);
    }

    @Test
    void existsCustomerByEmail_shouldReturnTrueIfCustomerExists() {
        // Arrange
        when(customerRepository.existsCustomerByEmail(mockCustomer.getEmail())).thenReturn(true);

        // Act
        boolean exists = customerRepository.existsCustomerByEmail(mockCustomer.getEmail());

        // Assert
        assertThat(exists).isTrue();
        verify(customerRepository, times(1)).existsCustomerByEmail(mockCustomer.getEmail());
    }

    @Test
    void existsCustomerById_shouldReturnTrueIfCustomerExists() {
        // Arrange
        when(customerRepository.existsCustomerById(mockCustomer.getId())).thenReturn(true);

        // Act
        boolean exists = customerRepository.existsCustomerById(mockCustomer.getId());

        // Assert
        assertThat(exists).isTrue();
        verify(customerRepository, times(1)).existsCustomerById(mockCustomer.getId());
    }

    @Test
    void findCustomerByEmail_shouldReturnCustomerIfExists() {
        // Arrange
        when(customerRepository.findCustomerByEmail(mockCustomer.getEmail()))
                .thenReturn(Optional.of(mockCustomer));

        // Act
        Optional<Customer> foundCustomer = customerRepository.findCustomerByEmail(mockCustomer.getEmail());

        // Assert
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get()).isEqualTo(mockCustomer);
        verify(customerRepository, times(1)).findCustomerByEmail(mockCustomer.getEmail());
    }

    @Test
    void findCustomerByEmail_shouldReturnEmptyIfCustomerDoesNotExist() {
        // Arrange
        when(customerRepository.findCustomerByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<Customer> foundCustomer = customerRepository.findCustomerByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundCustomer).isNotPresent();
        verify(customerRepository, times(1)).findCustomerByEmail("nonexistent@example.com");
    }

    @Test
    void updateProfileImageId_shouldUpdateImageIdSuccessfully() {
        // Arrange
        String newProfileImageId = "profile123";
        Integer customerId = mockCustomer.getId();

        when(customerRepository.updateProfileImageId(newProfileImageId, customerId)).thenReturn(1);

        // Act
        int updatedRows = customerRepository.updateProfileImageId(newProfileImageId, customerId);

        // Assert
        assertThat(updatedRows).isEqualTo(1);
        verify(customerRepository, times(1)).updateProfileImageId(newProfileImageId, customerId);
    }
}
