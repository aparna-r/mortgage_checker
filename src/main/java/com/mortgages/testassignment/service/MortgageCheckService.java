package com.mortgages.testassignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.BiPredicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MortgageCheckService {
    private final MortgageInterestRateService mortgageInterestRateService;

    private boolean isMortgageFeasibility(double income, double loanValue, double homeValue) {
        BiPredicate<Double, Double> loanShouldNotExceedFourTimesIncome = (lValue, iValue) ->
                BigDecimal.valueOf(lValue).compareTo(BigDecimal.valueOf(iValue).multiply(BigDecimal.valueOf(4))) <= 0;
        BiPredicate<Double, Double> loanShouldNotExceedHomeValue = (lValue, hValue) ->
                BigDecimal.valueOf(lValue).compareTo(BigDecimal.valueOf(hValue)) <= 0;

        if (!loanShouldNotExceedFourTimesIncome.test(loanValue, income)) {
            log.debug("loan can not be more than 4 times income");
            return false;
        }
        if (!loanShouldNotExceedHomeValue.test(loanValue, homeValue)) {
            log.debug("loan can not be more than home value");
            return false;
        }
        return true;
    }

    /**
     * formula emi = (r(1+r)^N * P) / ((1+r)^N - 1)
     * @param r monthly interest rate
     * @param N number of monthly payments
     * @param P principle
     * @return monthly mortgage payment rounded upto 2 decimal places
     */
    private double calculateMonthlyCost(double r, int N, double P) {
        BigDecimal r_mul_P = BigDecimal.valueOf(r).multiply(BigDecimal.valueOf(P));
        BigDecimal one_plus_r = BigDecimal.ONE.add(BigDecimal.valueOf(r));
        BigDecimal one_plus_r_power_N = one_plus_r.pow(N);
        BigDecimal numerator = r_mul_P.multiply(one_plus_r_power_N);
        BigDecimal denominator = one_plus_r_power_N.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     *
     * @param income annual income
     * @param maturityPeriodInYears
     * @param loanValue
     * @param homeValue
     * @return
     */
    public Optional<Double> getMonthlyCost(double income, int maturityPeriodInYears, double loanValue,
                                           double homeValue) {
        if (!isMortgageFeasibility(income, loanValue, homeValue)) {
            return Optional.empty();
        }
        double annualInterestRatePercentage = mortgageInterestRateService
                .getInterestRateByMaturityPeriod(maturityPeriodInYears).getAnnualInterestRatePercentage();

        double monthlyInterestRate = BigDecimal.valueOf(annualInterestRatePercentage)
                .divide(BigDecimal.valueOf(100 * 12), MathContext.DECIMAL64).doubleValue();
        int totalNumberOfMonthlyPayments = maturityPeriodInYears * 12;

        return Optional.of(calculateMonthlyCost(monthlyInterestRate, totalNumberOfMonthlyPayments, loanValue));
    }
}
