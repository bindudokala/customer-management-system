package com.customers.customermanagement.controller;

import com.customers.customermanagement.dto.CustomerDTO;
import com.customers.customermanagement.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        // CustomerDTO is passed and will be converted to Customer in the service layer
        return new ResponseEntity<>(customerService.createCustomer(customerDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable UUID id) {
        // Return CustomerDTO from service
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDTO);
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        // Get a list of CustomerDTOs
        return customerService.getAllCustomers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID id, @Valid @RequestBody CustomerDTO customerDTO) {
        // Update and return the updated CustomerDTO
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(id, customerDTO);
        return updatedCustomerDTO != null ? ResponseEntity.ok(updatedCustomerDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id) {
        // Delete customer by id and return appropriate response
        return customerService.deleteCustomer(id) ? ResponseEntity.ok("Successfully deleted the customer with ID: " + id) : ResponseEntity.notFound().build();
    }
}
