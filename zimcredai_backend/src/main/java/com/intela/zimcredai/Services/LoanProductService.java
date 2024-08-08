package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.LoanProduct;
import com.intela.zimcredai.Repositories.LoanProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.intela.zimcredai.Util.Util.getNullPropertyNames;

@Service
@RequiredArgsConstructor
public class LoanProductService {

    private final LoanProductRepository loanProductRepository;

    // Create a new loan product
    public LoanProduct create(LoanProduct loanProduct) {
        return loanProductRepository.save(loanProduct);
    }

    // Fetch a loan product by ID
    public LoanProduct fetchById(Integer id) throws NotFoundException {
        return loanProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan product not found with ID: " + id));
    }

    // Update an existing loan product
    public LoanProduct update(Integer id, LoanProduct loanProduct) throws NotFoundException {
        LoanProduct dbLoanProduct = this.fetchById(id);

        // Copy non-null properties from loanProduct to dbLoanProduct
        BeanUtils.copyProperties(loanProduct, dbLoanProduct, getNullPropertyNames(loanProduct));

        return loanProductRepository.save(dbLoanProduct);
    }

    // Delete a loan product by ID
    public void delete(Integer id) throws NotFoundException {
        LoanProduct dbLoanProduct = this.fetchById(id);
        loanProductRepository.delete(dbLoanProduct);
    }

    // Fetch all loan products
    public List<LoanProduct> fetchAll() {
        return loanProductRepository.findAll();
    }
}
