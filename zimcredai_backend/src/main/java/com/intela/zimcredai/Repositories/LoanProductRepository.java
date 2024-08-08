package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, Integer> {
}
