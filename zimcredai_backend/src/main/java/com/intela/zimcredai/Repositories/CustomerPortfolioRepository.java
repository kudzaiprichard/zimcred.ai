package com.intela.zimcredai.Repositories;

import com.intela.zimcredai.Models.CustomerPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPortfolioRepository extends JpaRepository<CustomerPortfolio, Integer> {
}
