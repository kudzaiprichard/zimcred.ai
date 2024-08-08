package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.DocumentRequirement;
import com.intela.zimcredai.Repositories.DocumentRequirementRepository;
import com.intela.zimcredai.Repositories.LoanApplicationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static com.intela.zimcredai.Util.Util.getNullPropertyNames;

@Service
@RequiredArgsConstructor
public class DocumentRequirementService {

    private final DocumentRequirementRepository documentRequirementRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    // Create a new document requirement
    public DocumentRequirement create(DocumentRequirement documentRequirement) {
        // Ensure the loan application exists before associating it
        if (documentRequirement.getLoanApplication() != null) {
            loanApplicationRepository.findById(documentRequirement.getLoanApplication().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + documentRequirement.getLoanApplication().getId()));
        }

        return documentRequirementRepository.save(documentRequirement);
    }

    // Fetch a document requirement by ID
    public DocumentRequirement fetchById(Integer id) throws NotFoundException {
        return documentRequirementRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    // Update an existing document requirement
    public DocumentRequirement update(Integer id, DocumentRequirement documentRequirement) throws NotFoundException {
        DocumentRequirement dbDocumentRequirement = this.fetchById(id);

        // Copy non-null properties from documentRequirement to dbDocumentRequirement
        BeanUtils.copyProperties(documentRequirement, dbDocumentRequirement, getNullPropertyNames(documentRequirement));

        return documentRequirementRepository.save(dbDocumentRequirement);
    }

    // Delete a document requirement by ID
    public void delete(Integer id) throws NotFoundException {
        DocumentRequirement dbDocumentRequirement = this.fetchById(id);
        documentRequirementRepository.delete(dbDocumentRequirement);
    }

    // Fetch all document requirements
    public List<DocumentRequirement> fetchAll() {
        return documentRequirementRepository.findAll();
    }
}
