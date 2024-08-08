package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.SupportRequest;
import com.intela.zimcredai.Models.SupportRequestStatus;
import com.intela.zimcredai.Repositories.SupportRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;


import static com.intela.zimcredai.Util.Util.getNullPropertyNames;

@Service
@RequiredArgsConstructor
public class SupportRequestService {
    private final SupportRequestRepository supportRequestRepository;

    // Create a new support request
    public SupportRequest create(SupportRequest supportRequest) {
        return supportRequestRepository.save(supportRequest);
    }

    // Fetch a support request by ID
    public SupportRequest fetchById(Integer id) throws NotFoundException {
        return supportRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Support request not found with ID: " + id));
    }

    // Update an existing support request with dynamic fields
    public SupportRequest update(Integer id, SupportRequest supportRequest) throws NotFoundException {
        SupportRequest dBsupportRequest = this.fetchById(id);
        // Copy non-null properties from customerPortfolio to dbCustomerPortfolio
        BeanUtils.copyProperties(supportRequest, dBsupportRequest, getNullPropertyNames(supportRequest));
        return this.supportRequestRepository.save(dBsupportRequest);
    }

    // Delete a support request by ID
    public void delete(Integer id) throws NotFoundException {
        SupportRequest supportRequest = this.fetchById(id);
        this.supportRequestRepository.delete(supportRequest);
    }

    // Fetch all support requests
    public List<SupportRequest> fetchAll() {
        return supportRequestRepository.findAll();
    }

    // Fetch support requests by status
    public List<SupportRequest> fetchByStatus(SupportRequestStatus status) {
        return supportRequestRepository.findByStatus(status);
    }

    // Fetch support requests by user ID
    public List<SupportRequest> fetchByUserId(Long userId) {
        return supportRequestRepository.findByUserId(userId);
    }

}
