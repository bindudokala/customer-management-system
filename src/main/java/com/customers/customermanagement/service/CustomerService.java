package com.customers.customermanagement.service;

import com.customers.customermanagement.dto.CustomerDTO;
import com.customers.customermanagement.entity.Customer;
import com.customers.customermanagement.exception.CustomerAlreadyExistsException;
import com.customers.customermanagement.exception.CustomerNotFoundException;
import com.customers.customermanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDto) {
        // Check if a customer with the same email already exists
        if (customerRepository.existsByEmailAddress(customerDto.getEmailAddress())) {
            throw new CustomerAlreadyExistsException("Customer with email " + customerDto.getEmailAddress() + " already exists.");
        }
        customerRepository.flush();
        Customer savedCustomer = customerRepository.save(convertToEntity(customerDto));
        return convertToDTO(savedCustomer); // Convert entity to DTO before returning
    }

    public CustomerDTO getCustomerById(UUID id) {
        // Throw exception if the customer is not found
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found."));
        return convertToDTO(customer); // Convert entity to DTO before returning
    }

    public List<CustomerDTO> getAllCustomers() {
        // Convert all customers to DTO and return
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CustomerDTO updateCustomer(UUID id, CustomerDTO customerDto) {
        Optional<Customer> existingCustomer = customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
            Customer updatedCustomer = existingCustomer.get();
            updatedCustomer.setFirstName(customerDto.getFirstName());
            updatedCustomer.setMiddleName(customerDto.getMiddleName());
            updatedCustomer.setLastName(customerDto.getLastName());
            updatedCustomer.setEmailAddress(customerDto.getEmailAddress());
            updatedCustomer.setPhoneNumber(customerDto.getPhoneNumber());
            customerRepository.flush();
            Customer savedCustomer = customerRepository.save(updatedCustomer);
            return convertToDTO(savedCustomer); // Convert entity to DTO before returning
        }
        throw new CustomerNotFoundException("Customer with ID " + id + " not found.");
    }

    public boolean deleteCustomer(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        throw new CustomerNotFoundException("Customer with ID " + id + " not found.");
    }

    // Method to convert a Customer entity to CustomerDTO
    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getPhoneNumber(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                customer.getEmailAddress()
        );
    }

    private Customer convertToEntity(CustomerDTO customerDto) {
        return new Customer(
                customerDto.getId(),
                customerDto.getPhoneNumber(),
                customerDto.getFirstName(),
                customerDto.getMiddleName(),
                customerDto.getLastName(),
                customerDto.getEmailAddress()
        );
    }
}
