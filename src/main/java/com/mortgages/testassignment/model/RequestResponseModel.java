package com.mortgages.testassignment.model;

import com.mortgages.testassignment.exception.ErrorDetail;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface RequestResponseModel {
    MortgageCheckResponse MORTGAGE_NOT_FEASIBLE = new MortgageCheckResponse(false, 0);
    // Request models
    record MortgageCheckRequest(@Positive double income,
                                @Min(value = 1) @Max(value = 30) int maturityPeriodInYears,
                                @Positive double loanValue,
                                @Positive double homeValue){}

    // Response models
    record MortgageInterestRateResponse(int maturityPeriodInYears, double interestRatePercentage,
                                        String lastUpdateInUTC){}
    record MortgageInterestRateListResponse(List<MortgageInterestRateResponse> interestRates){}
    record MortgageCheckResponse(boolean mortgagePossible, double monthlyCost){}

    // Error Response Models
    record ErrorResponse(int code, String message)  {
        public ErrorResponse(ErrorDetail error) {
            this(error.getErrorCode(), error.getMessage());
        }
    }
}
