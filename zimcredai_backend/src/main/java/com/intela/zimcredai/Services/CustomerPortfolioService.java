package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.CustomerPortfolio;
import com.intela.zimcredai.Repositories.CustomerPortfolioRepository;
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
public class CustomerPortfolioService {

    private final CustomerPortfolioRepository customerPortfolioRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Create a new customer portfolio
    public CustomerPortfolio create(CustomerPortfolio customerPortfolio, HttpServletRequest request) {
        customerPortfolio.setUser(getUserByToken(request, jwtService, userRepository)); // set user
        return customerPortfolioRepository.save(customerPortfolio);
    }

    // Fetch a customer portfolio by ID
    public CustomerPortfolio fetchPortfolio(Integer id) throws NotFoundException {
        return customerPortfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer portfolio not found with ID: " + id));
    }

    // Update an existing customer portfolio
    public CustomerPortfolio updatePortfolio(Integer id, CustomerPortfolio customerPortfolio) throws NotFoundException {
        CustomerPortfolio dbCustomerPortfolio = this.fetchPortfolio(id);

        // Copy non-null properties from customerPortfolio to dbCustomerPortfolio
        BeanUtils.copyProperties(customerPortfolio, dbCustomerPortfolio, getNullPropertyNames(customerPortfolio));

        // Update portfolioCategory and portfolioClassification
        dbCustomerPortfolio.updatePortfolioCategory();  // Todo: check if necessary on testing
        dbCustomerPortfolio.updatePortfolioClassification();

        return customerPortfolioRepository.save(dbCustomerPortfolio);
    }

    // Delete a customer portfolio by ID
    public void deletePortfolio(Integer id) throws NotFoundException {
        CustomerPortfolio dbCustomerPortfolio = this.fetchPortfolio(id);
        customerPortfolioRepository.delete(dbCustomerPortfolio);
    }

    // Fetch all customer portfolios
    public List<CustomerPortfolio> fetchAll() {
        return customerPortfolioRepository.findAll();
    }
}
