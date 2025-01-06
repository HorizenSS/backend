package com.ines.controller;

import com.ines.dto.ApiResponse;
import com.ines.dto.CustomerDTO;
import com.ines.dto.CustomerRegistrationRequest;
import com.ines.dto.CustomerUpdateRequest;
import com.ines.jwt.JWTUtil;
import com.ines.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, "Customers retrieved successfully."));
    }

    @GetMapping("{customerId}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(
            @PathVariable("customerId") Integer customerId) {
        CustomerDTO customer = customerService.getCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(customer, "Customer retrieved successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerCustomer(
            @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(ApiResponse.success(null, "Customer registered successfully."));
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @PathVariable("customerId") Integer customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer deleted successfully."));
    }

    @PutMapping("{customerId}")
    public ResponseEntity<ApiResponse<Void>> updateCustomer(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CustomerUpdateRequest updateRequest) {
        customerService.updateCustomer(customerId, updateRequest);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer updated successfully."));
    }

    @PostMapping(
            value = "{customerId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<Void>> uploadCustomerProfileImage(
            @PathVariable("customerId") Integer customerId,
            @RequestParam("file") MultipartFile file) {
        customerService.uploadCustomerProfileImage(customerId, file);
        return ResponseEntity.ok(ApiResponse.success(null, "Profile image uploaded successfully."));
    }

    @GetMapping(
            value = "{customerId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> getCustomerProfileImage(
            @PathVariable("customerId") Integer customerId) {
        byte[] image = customerService.getCustomerProfileImage(customerId);
        return ResponseEntity.ok(image);
    }
}

