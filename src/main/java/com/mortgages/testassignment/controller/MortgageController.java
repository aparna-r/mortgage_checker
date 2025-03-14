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
                        mortgageInterestRate.getMaturityPeriod(),
                        mortgageInterestRate.getInterestRate()))
                .collect(Collectors.toList()));
    }

    @PostMapping("/api/mortgage-check")
    public MortgageCheckResponse mortgageCheck(@RequestBody @Valid final MortgageCheckRequest checkRequest) {
        return mortgageCheckService.getMonthlyMortgageAmount(checkRequest.income(), checkRequest.maturityPeriod(),
                        checkRequest.loanValue(), checkRequest.homeValue())
                .map(v -> new MortgageCheckResponse(true, v))
                .orElse(MORTGAGE_NOT_FEASIBLE);
    }
}
