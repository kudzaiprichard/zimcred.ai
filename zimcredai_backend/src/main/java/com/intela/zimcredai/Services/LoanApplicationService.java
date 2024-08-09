package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.LoanApplication;
import com.intela.zimcredai.Repositories.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.intela.zimcredai.Util.Util.getNullPropertyNames;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;

    // Create a new loan application
    public LoanApplication create(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    // Fetch a loan application by ID
    public LoanApplication fetchById(Integer id) throws NotFoundException {
        return loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found with ID: " + id));
    }

    // Update an existing loan application
    public LoanApplication update(Integer id, LoanApplication loanApplication) throws NotFoundException {
        LoanApplication dbLoanApplication = this.fetchById(id);

        // Copy non-null properties from loanApplication to dbLoanApplication
        BeanUtils.copyProperties(loanApplication, dbLoanApplication, getNullPropertyNames(loanApplication));

        return loanApplicationRepository.save(dbLoanApplication);
    }

    // Delete a loan application by ID
    public void delete(Integer id) throws NotFoundException {
        LoanApplication dbLoanApplication = this.fetchById(id);
        loanApplicationRepository.delete(dbLoanApplication);
    }

    // Fetch all loan applications
    public List<LoanApplication> fetchAll() {
        return loanApplicationRepository.findAll();
    }

}
