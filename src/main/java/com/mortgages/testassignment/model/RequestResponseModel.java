package com.mortgages.testassignment.model;

import com.mortgages.testassignment.exception.ErrorDetail;
import jakarta.validation.constraints.Max;

import java.util.List;

public interface RequestResponseModel {
    MortgageCheckResponse MORTGAGE_NOT_FEASIBLE = new MortgageCheckResponse(false, 0);
    // Request models
    record MortgageCheckRequest(double income,
                                       @Max(value = 30) int maturityPeriod,
                                       double loanValue,
                                       double homeValue){}

    // Response models
    record MortgageInterestRateResponse(int maturityPeriod, double interestRate){}
    record MortgageInterestRateListResponse(List<MortgageInterestRateResponse> interestRates){}
    record MortgageCheckResponse(boolean mortgagePossible, double mortgageMonthlyAmount){}

    // Error Response Models
    record ErrorResponse(int code, String message)  {
        public ErrorResponse(ErrorDetail error) {
            this(error.getErrorCode(), error.getMessage());
        }
    }
}
