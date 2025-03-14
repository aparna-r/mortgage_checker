package com.mortgages.testassignment.service;

import com.mortgages.testassignment.exception.ApplicationException;
import com.mortgages.testassignment.exception.ErrorDetail;
import com.mortgages.testassignment.repository.MortgageInterestRateRepository;
import com.mortgages.testassignment.repository.entity.MortgageInterestRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MortgageInterestRateService {
    private final MortgageInterestRateRepository mortgageInterestRateRepository;

    public List<MortgageInterestRate> getInterestRates() {
        return StreamSupport.stream(mortgageInterestRateRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public MortgageInterestRate getInterestRateByMaturityPeriod(int maturityPeriod) {
        return mortgageInterestRateRepository.findByMaturityPeriod(maturityPeriod).orElseThrow(() ->
                new ApplicationException(ErrorDetail.MATURITY_PERIOD_NOT_FOUND));
    }
}
