package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.Customer;
import com.intela.zimcredai.Repositories.CustomerRepository;
import com.intela.zimcredai.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import java.util.List;

import static com.intela.zimcredai.Util.Util.getNullPropertyNames;
import static com.intela.zimcredai.Util.Util.getUserByToken;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    // Create a new customer
    public Customer create(Customer customer, HttpServletRequest request) {
        customer.setUser(getUserByToken(request , jwtService, userRepository)); // set user
        return this.customerRepository.save(customer);
    }

    // Fetch a customer's profile by ID
    public Customer fetchProfile(Integer id) throws NotFoundException {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + id));
    }

    // Update an existing customer's profile
    public Customer updateProfile(Integer id, Customer customer) throws NotFoundException {
        Customer dbCustomer = this.fetchProfile(id);

        // Copy non-null properties from customer to dbCustomer
        BeanUtils.copyProperties(customer, dbCustomer, getNullPropertyNames(customer));

        return this.customerRepository.save(dbCustomer);
    }

    // Delete a customer by ID
    public void deleteProfile(Integer id) throws NotFoundException {
        Customer dbCustomer = this.fetchProfile(id);
        this.customerRepository.delete(dbCustomer);
    }

    // Fetch all customers
    public List<Customer> fetchAll() {
        return this.customerRepository.findAll();
    }
}
