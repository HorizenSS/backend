package com.ines.service;

import com.ines.dto.CustomerDTO;
import com.ines.dto.CustomerRegistrationRequest;
import com.ines.exception.DuplicateResourceException;
import com.ines.exception.ResourceNotFoundException;
import com.ines.mapper.CustomerDTOMapper;
import com.ines.model.Customer;
import com.ines.model.Gender;
import com.ines.repository.CustomerDao;
import com.ines.s3.S3Buckets;
import com.ines.s3.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerDao customerDao;
    private CustomerDTOMapper customerDTOMapper;
    private PasswordEncoder passwordEncoder;
    private S3Service s3Service;
    private S3Buckets s3Buckets;

    @BeforeEach
    void setUp() {
        customerDao = mock(CustomerDao.class);
        customerDTOMapper = mock(CustomerDTOMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        s3Service = mock(S3Service.class);
        s3Buckets = mock(S3Buckets.class);
        customerService = new CustomerService(customerDao, customerDTOMapper, passwordEncoder, s3Service, s3Buckets);
    }

    @Test
    void getAllCustomers_shouldReturnListOfCustomerDTOs() {
        when(customerDao.selectAllCustomers()).thenReturn(Stream.of(new Customer()).toList());
        when(customerDTOMapper.apply(any(Customer.class))).thenReturn(mock(CustomerDTO.class));

        var result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerDao, times(1)).selectAllCustomers();
    }

    @Test
    void getCustomer_shouldThrowResourceNotFoundException_whenCustomerDoesNotExist() {
        int customerId = 1;
        when(customerDao.selectCustomerById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(customerId));
    }

    @Test
    void addCustomer_shouldInsertCustomer_whenEmailDoesNotExist() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("name", "email@example.com", "password", 25, Gender.MALE);
        when(customerDao.existsCustomerWithEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        customerService.addCustomer(request);

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerCaptor.capture());
        assertEquals("name", customerCaptor.getValue().getName());
    }

    @Test
    void addCustomer_shouldThrowDuplicateResourceException_whenEmailExists() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("name", "email@example.com", "password", 25, Gender.MALE);
        when(customerDao.existsCustomerWithEmail(request.email())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> customerService.addCustomer(request));
    }

    @Test
    void deleteCustomerById_shouldCallDelete_whenCustomerExists() {
        int customerId = 1;
        when(customerDao.existsCustomerById(customerId)).thenReturn(true);

        customerService.deleteCustomerById(customerId);

        verify(customerDao).deleteCustomerById(customerId);
    }

    @Test
    void deleteCustomerById_shouldThrowResourceNotFoundException_whenCustomerDoesNotExist() {
        int customerId = 1;
        when(customerDao.existsCustomerById(customerId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomerById(customerId));
    }

    @Test
    void uploadCustomerProfileImage_shouldThrowException_whenUploadFails() throws IOException {
        int customerId = 1;
        MultipartFile file = mock(MultipartFile.class);
        when(customerDao.existsCustomerById(customerId)).thenReturn(true);
        when(file.getBytes()).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> customerService.uploadCustomerProfileImage(customerId, file));
    }
}
