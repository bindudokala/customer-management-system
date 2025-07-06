package com.customers.customermanagement.unit;

import com.customers.customermanagement.dto.CustomerDTO;
import com.customers.customermanagement.entity.Customer;
import com.customers.customermanagement.exception.CustomerAlreadyExistsException;
import com.customers.customermanagement.exception.CustomerNotFoundException;
import com.customers.customermanagement.repository.CustomerRepository;
import com.customers.customermanagement.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void shouldCreateCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmailAddress("test@example.com");

        // Simulate that customer does not exist
        when(customerRepository.existsByEmailAddress(customerDTO.getEmailAddress())).thenReturn(false);

        // Simulate saving the customer and returning it
        Customer customer = new Customer();
        customer.setEmailAddress(customerDTO.getEmailAddress());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Call service method to create customer
        CustomerDTO saved = customerService.createCustomer(customerDTO);

        assertEquals("test@example.com", saved.getEmailAddress());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldThrowIfCustomerAlreadyExists() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmailAddress("test@example.com");

        // Simulate customer already exists
        when(customerRepository.existsByEmailAddress(customerDTO.getEmailAddress())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.createCustomer(customerDTO));
    }

    @Test
    void shouldGetCustomerById() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);

        // Simulate customer found by ID
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerDTO found = customerService.getCustomerById(id);

        assertEquals(id, found.getId());
    }

    @Test
    void shouldThrowIfCustomerNotFoundById() {
        UUID id = UUID.randomUUID();
        // Simulate customer not found
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void shouldUpdateCustomer() {
        UUID id = UUID.randomUUID();
        Customer existing = new Customer();
        existing.setId(id);

        CustomerDTO updates = new CustomerDTO();
        updates.setFirstName("John");
        updates.setLastName("Doe");
        updates.setEmailAddress("updated@example.com");
        updates.setPhoneNumber("1234567890");

        // Simulate finding the existing customer and updating
        when(customerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);

        // Update customer and return the DTO
        CustomerDTO updated = customerService.updateCustomer(id, updates);

        assertEquals("John", updated.getFirstName());
    }

    @Test
    void shouldDeleteCustomer() {
        UUID id = UUID.randomUUID();
        when(customerRepository.existsById(id)).thenReturn(true);

        boolean deleted = customerService.deleteCustomer(id);

        assertTrue(deleted);
        verify(customerRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentCustomer() {
        UUID id = UUID.randomUUID();
        when(customerRepository.existsById(id)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(id));
    }
}
