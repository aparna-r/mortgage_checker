package com.mortgages.testassignment.service;

import com.mortgages.testassignment.exception.ApplicationException.MaturityPeriodNotFoundException;
import com.mortgages.testassignment.repository.MortgageInterestRateRepository;
import com.mortgages.testassignment.repository.entity.MortgageInterestRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MortgageInterestRateServiceTest {
    @Mock
    private MortgageInterestRateRepository mortgageInterestRateRepository;
    @InjectMocks
    private MortgageInterestRateService mortgageInterestRateService;

    @DisplayName("get interest rates should return success")
    @Test
    public void testGetInterestRatesForSuccess() {
        List<MortgageInterestRate> mortgageInterestRates = Arrays.asList(mock(MortgageInterestRate.class),
                mock(MortgageInterestRate.class));
        when(mortgageInterestRateRepository.findAll()).thenReturn(mortgageInterestRates);

        assertThat(mortgageInterestRateService.getInterestRates()).isEqualTo(mortgageInterestRates);

        verify(mortgageInterestRateRepository, times(1)).findAll();
    }

    @DisplayName("get interest rate by valid maturity period should return success")
    @Test
    public void testGetInterestRateByMaturityPeriodForSuccess() {
        MortgageInterestRate mortgageInterestRate = mock(MortgageInterestRate.class);
        when(mortgageInterestRateRepository.findByMaturityPeriodInYears(eq(1))).thenReturn(Optional.of(mortgageInterestRate));

        assertThat(mortgageInterestRateService.getInterestRateByMaturityPeriod(1)).isEqualTo(mortgageInterestRate);

        verify(mortgageInterestRateRepository, times(1)).findByMaturityPeriodInYears(eq(1));
    }

    @DisplayName("get interest rate by invalid maturity period should throw exception")
    @Test
    public void testGetInterestRateByMaturityPeriodForFailure() {
        when(mortgageInterestRateRepository.findByMaturityPeriodInYears(eq(1))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mortgageInterestRateService.getInterestRateByMaturityPeriod(1))
                .isInstanceOf(MaturityPeriodNotFoundException.class);
        verify(mortgageInterestRateRepository, times(1)).findByMaturityPeriodInYears(eq(1));
    }
}
