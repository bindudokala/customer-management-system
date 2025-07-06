package com.customers.customermanagement.entity;

import com.customers.customermanagement.dto.CustomerDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import jakarta.validation.constraints.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")  // Using uuid2 strategy for UUID generation
    private UUID id;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank(message = "First name is mandatory")
    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is mandatory")
    @Column(nullable = false)
    private String lastName;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email address is mandatory")
    @Column(unique = true, nullable = false)
    private String emailAddress;

    public Customer() {
    }

    public Customer(UUID id, String phoneNumber, String firstName, String middleName, String lastName, String emailAddress) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public CustomerDTO getCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();

        // Setting fields in customerDTO
        customerDTO.setId(customer.getId());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setMiddleName(customer.getMiddleName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmailAddress(customer.getEmailAddress());

        return customerDTO;
    }

}
