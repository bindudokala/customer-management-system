package com.customers.customermanagement.unit;

import com.customers.customermanagement.controller.CustomerController;
import com.customers.customermanagement.dto.CustomerDTO;
import com.customers.customermanagement.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerService customerService;
    private CustomerController controller;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        controller = new CustomerController(customerService);
    }

    @Test
    void testCreateCustomer() {
        // Create a CustomerDTO instead of Customer
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmailAddress("test@example.com");

        // Return the same DTO object from service
        when(customerService.createCustomer(customerDTO)).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = controller.createCustomer(customerDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("test@example.com", response.getBody().getEmailAddress());
    }

    @Test
    void testGetCustomerById() {
        UUID id = UUID.randomUUID();
        // Create a CustomerDTO instead of Customer
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(id);

        when(customerService.getCustomerById(id)).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = controller.getCustomer(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void testGetAllCustomers() {
        // Create a list of CustomerDTO objects
        CustomerDTO c1 = new CustomerDTO();
        CustomerDTO c2 = new CustomerDTO();

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(c1, c2));

        List<CustomerDTO> result = controller.getAllCustomers();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateCustomer() {
        UUID id = UUID.randomUUID();
        // Create a CustomerDTO instead of Customer
        CustomerDTO customerDTO = new CustomerDTO();

        when(customerService.updateCustomer(id, customerDTO)).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = controller.updateCustomer(id, customerDTO);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteCustomer() {
        UUID id = UUID.randomUUID();
        when(customerService.deleteCustomer(id)).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteCustomer(id);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteNonExistentCustomer() {
        UUID id = UUID.randomUUID();
        when(customerService.deleteCustomer(id)).thenReturn(false);

        ResponseEntity<Void> response = controller.deleteCustomer(id);

        assertEquals(404, response.getStatusCodeValue());
    }
}
