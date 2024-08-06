package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DocumentType {
    IDENTITY_PROOF("identity_proof"), // Government-issued identification documents like passport, driver's license, national ID card
    PROOF_OF_INCOME("proof_of_income"), // Documents showing proof of income like pay stubs, tax returns, bank statements
    CREDIT_INFORMATION("credit_information"), // Credit report or score and list of existing debts
    PROOF_OF_RESIDENCE("proof_of_residence"), // Documents showing proof of residence like utility bills, rental agreements
    LOAN_APPLICATION_FORM("loan_application_form"), // Completed loan application form
    BUSINESS_PLAN("business_plan"), // Business plan for business loans
    PROPERTY_DOCUMENTS("property_documents"), // Documents related to property for mortgage loans
    QUOTATIONS_OR_INVOICES("quotations_or_invoices"), // Quotations or invoices for specific purpose loans
    COLLATERAL_DOCUMENTS("collateral_documents"), // Documents related to collateral like title deeds, vehicle registration
    MISCELLANEOUS_DOCUMENTS("miscellaneous_documents"), // Other required documents like photographs, insurance documents, legal documents for co-signers or guarantors,
    OTHER("other");

    @Getter
    private final String documentType;

}
