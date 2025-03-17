package com.mortgages.testassignment.service;

import com.mortgages.testassignment.repository.entity.MortgageInterestRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MortgageCheckServiceTest {
    @Mock
    private MortgageInterestRateService mortgageInterestRateService;
    @InjectMocks
    private MortgageCheckService mortgageCheckService;

    @DisplayName("get monthly mortgage amount should return monthly cost when inputs are valid")
    @Test
    public void testGetMonthlyCostForSuccess() {
        MortgageInterestRate mortgageInterestRate = mock(MortgageInterestRate.class);
        when(mortgageInterestRate.getAnnualInterestRatePercentage()).thenReturn(4.71);
        when(mortgageInterestRateService.getInterestRateByMaturityPeriod(eq(30))).thenReturn(mortgageInterestRate);

        Optional<Double> monthlyCost = mortgageCheckService.getMonthlyCost(100000, 30, 85000, 90000);
        assertThat(monthlyCost).isPresent().hasValue(441.35);
        verify(mortgageInterestRateService, times(1)).getInterestRateByMaturityPeriod(eq(30));
    }

    @DisplayName("get monthly mortgage amount should return nothing when loan is more than home value")
    @Test
    public void testGetMonthlyCostForFailure1() {
        Optional<Double> monthlyCost = mortgageCheckService.getMonthlyCost(100, 30, 500, 400);
        assertThat(monthlyCost).isEmpty();
    }

    @DisplayName("get monthly mortgage amount should return nothing when loan is more than 4 times income")
    @Test
    public void testGetMonthlyCostForFailure2() {
        Optional<Double> monthlyCost = mortgageCheckService.getMonthlyCost(100, 30, 500, 600);
        assertThat(monthlyCost).isEmpty();
    }
}
