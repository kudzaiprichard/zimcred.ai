package com.intela.zimcredai.Services;

import com.intela.zimcredai.Exception.ResourceNotFoundException;
import com.intela.zimcredai.Models.Document;
import com.intela.zimcredai.Repositories.DocumentRepository;
import com.intela.zimcredai.Repositories.LoanApplicationRepository;
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
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final JwtService jwtService;

    // Create a new document
    public Document create(Document document) { // HttpServletRequest request
        // Optionally, you can set the user associated with the document if required
        // document.setUser(getUserByToken(request, jwtService, userRepository));

        // Ensure the loan application exists before associating it
        if (document.getLoanApplication() != null) {
            loanApplicationRepository.findById(document.getLoanApplication().getId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Document not found with ID: " + document.getLoanApplication().getId())
                    );
        }

        return documentRepository.save(document);
    }

    // Fetch a document by ID
    public Document fetchById(Integer id) throws NotFoundException {
        return documentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    // Update an existing document
    public Document update(Integer id, Document document) throws NotFoundException {
        Document dbDocument = this.fetchById(id);

        // Copy non-null properties from document to dbDocument
        BeanUtils.copyProperties(document, dbDocument, getNullPropertyNames(document));

        return documentRepository.save(dbDocument);
    }

    // Delete a document by ID
    public void delete(Integer id) throws NotFoundException {
        Document dbDocument = this.fetchById(id);
        documentRepository.delete(dbDocument);
    }

    // Fetch all documents
    public List<Document> fetchAll() {
        return documentRepository.findAll();
    }

    public List<Document> findByLoanId(Integer loanId) {
        return null;
    }
}
