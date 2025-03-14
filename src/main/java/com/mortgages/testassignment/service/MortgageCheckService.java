package com.mortgages.testassignment.service;

import com.mortgages.testassignment.repository.entity.MortgageInterestRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MortgageCheckService {
    private final MortgageInterestRateService mortgageInterestRateService;

    private boolean isMortgageFeasible(double income, double loanValue, double homeValue) {
        return (!(loanValue > (4 * income))) && (!(loanValue > homeValue));
    }

    private double calculateMonthlyMortgage(int maturityPeriod, double loanValue) {
        MortgageInterestRate mortgageInterestRate = mortgageInterestRateService
                .getInterestRateByMaturityPeriod(maturityPeriod);
        BigDecimal r = BigDecimal.valueOf(mortgageInterestRate.getInterestRate())
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);
        BigDecimal P = BigDecimal.valueOf(loanValue);
        int N = maturityPeriod * 12;

        BigDecimal rP = r.multiply(P);
        BigDecimal t = BigDecimal.ONE.add(r).pow(N);
        BigDecimal numerator = rP.multiply(t);
        BigDecimal denominator = t.min(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP).doubleValue();
    }

    public Optional<Double> getMonthlyMortgageAmount(double income, int maturityPeriod, double loanValue,
                                                     double homeValue) {
        return isMortgageFeasible(income, loanValue, homeValue)
                ? Optional.of(calculateMonthlyMortgage(maturityPeriod, loanValue))
                : Optional.empty();
    }
}
