package com.intela.zimcredai.Controllers;

import com.intela.zimcredai.Models.Customer;
import com.intela.zimcredai.Services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /*
     * Profile management endpoints (Create, Read, Update)
     * */

    // Set up profile
    @PostMapping("/create/profile")
    public ResponseEntity<Customer> createProfile(@Valid Customer customer, HttpServletRequest request) {
        return ResponseEntity.ok()
                .body(this.customerService.create(customer, request));
    }

    // View profile
    @GetMapping("view/profile")
    public ResponseEntity<Customer> viewProfile(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok()
                .body(this.customerService.fetchProfile(id));
    }


    // Update profile
    @PutMapping("/update/profile")
    public ResponseEntity<Customer> updateProfile(@PathVariable Integer id, Customer customer) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok()
                .body(this.customerService.updateProfile(id, customer));
    }


    /*
     * Loan Application endpoints (Apply, View, View All)
     * */

    // Apply for a loan

    // View loan application by loan id

    // View All user loan applications

    // Get user loan history

    // Track Application (get application status)


    /*
     * Document Management (Upload, Get, List, check status)
     * */

    // Upload document

    // Get documents by document id

    // Get documents by loan id

    // Check document status by document id


    /*
     * Profiling  (Should happen automatic) Todo : triggered by something
     * */

    // Get customer `profile`

    // Get customer portfolio

    /*
     *  Loan Product Endpoints
     * */

    // Todo: also add advertisements to advertise and promote loan products
    // Also add endpoint to fetch loan products and allow customer to apply for a loan

}