package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.CustomerProfile;
import com.intela.zimcredai.Repositories.CustomerProfileRepository;
import com.intela.zimcredai.Repositories.UserRepository;
import com.intela.zimcredai.Services.JwtService;
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
public class CustomerProfileService {

    private final CustomerProfileRepository customerProfileRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Create a new customer profile
    public CustomerProfile create(CustomerProfile customerProfile, HttpServletRequest request) {
        customerProfile.setUser(getUserByToken(request, jwtService, userRepository)); // set user
        return customerProfileRepository.save(customerProfile);
    }

    // Fetch a customer profile by ID
    public CustomerProfile fetchProfile(Integer id) throws NotFoundException {
        return customerProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found with ID: " + id));
    }

    // Update an existing customer profile
    public CustomerProfile updateProfile(Integer id, CustomerProfile customerProfile) throws NotFoundException {
        CustomerProfile dbCustomerProfile = this.fetchProfile(id);

        // Copy non-null properties from customerProfile to dbCustomerProfile
        BeanUtils.copyProperties(customerProfile, dbCustomerProfile, getNullPropertyNames(customerProfile));

        // Update risk level
        dbCustomerProfile.updateRiskLevel(); // Todo: check if its necessary on testing

        return customerProfileRepository.save(dbCustomerProfile);
    }

    // Delete a customer profile by ID
    public void deleteProfile(Integer id) throws NotFoundException {
        CustomerProfile dbCustomerProfile = this.fetchProfile(id);
        customerProfileRepository.delete(dbCustomerProfile);
    }

    // Fetch all customer profiles
    public List<CustomerProfile> fetchAll() {
        return customerProfileRepository.findAll();
    }
}
