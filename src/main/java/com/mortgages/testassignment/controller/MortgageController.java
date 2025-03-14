package com.mortgages.testassignment.controller;

import com.mortgages.testassignment.model.RequestResponseModel.MortgageCheckRequest;
import com.mortgages.testassignment.model.RequestResponseModel.MortgageCheckResponse;
import com.mortgages.testassignment.model.RequestResponseModel.MortgageInterestRateListResponse;
import com.mortgages.testassignment.model.RequestResponseModel.MortgageInterestRateResponse;
import com.mortgages.testassignment.service.MortgageCheckService;
import com.mortgages.testassignment.service.MortgageInterestRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static com.mortgages.testassignment.model.RequestResponseModel.MORTGAGE_NOT_FEASIBLE;

@RestController
@RequiredArgsConstructor
public class MortgageController {

    private final MortgageInterestRateService mortgageInterestRateService;
    private final MortgageCheckService mortgageCheckService;

    @GetMapping("/api/interest-rates")
    public MortgageInterestRateListResponse getMortgageInterestRates() {
        return new MortgageInterestRateListResponse(mortgageInterestRateService.getInterestRates().stream()
                .map(mortgageInterestRate -> new MortgageInterestRateResponse(
                        mortgageInterestRate.getMaturityPeriodInYears(),
                        mortgageInterestRate.getAnnualInterestRatePercentage(),
                        mortgageInterestRate.getLastUpdate().withZoneSameInstant(ZoneOffset.UTC)
                                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .collect(Collectors.toList()));
    }

    @PostMapping("/api/mortgage-check")
    public MortgageCheckResponse mortgageCheck(@RequestBody @Valid final MortgageCheckRequest checkRequest) {
        return mortgageCheckService.getMonthlyCost(checkRequest.income(),
                        checkRequest.maturityPeriodInYears(), checkRequest.loanValue(), checkRequest.homeValue())
                .map(m -> new MortgageCheckResponse(true, m))
                .orElse(MORTGAGE_NOT_FEASIBLE);
    }
}
