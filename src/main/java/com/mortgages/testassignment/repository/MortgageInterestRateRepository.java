package com.mortgages.testassignment.repository;

import com.mortgages.testassignment.repository.entity.MortgageInterestRate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MortgageInterestRateRepository extends CrudRepository<MortgageInterestRate, Long> {
    Optional<MortgageInterestRate> findByMaturityPeriod(Integer maturityPeriod);
}
