package it;

import com.mortgages.testassignment.model.RequestResponseModel;
import com.mortgages.testassignment.model.RequestResponseModel.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class StepDefsIntegrationTest extends SpringIntegrationTest {

    @When("client makes get interest rate request")
    public void clientMakesGetInterestCall() {
        executeGetEntity("http://localhost:8080/api/interest-rates");
    }

    @When("client makes check mortgage request with {double}, {int}, {double}, {double}")
    public void clientMakesPostMortgageCheck(double income, int maturityPeriod, double loanValue, double homeValue) {
        executePostEntity("http://localhost:8080/api/mortgage-check",
                new MortgageCheckRequest(income, maturityPeriod, loanValue, homeValue));
    }

    @Then("client receives http status code {int}")
    public void clientReceivesStatusCode(int statusCode) {
        assertThat(getLastResponseHttpStatus()).isEqualTo(statusCode);
    }

    @Then("the client receives following interest rates")
    public void clientReceivesInterestRates(DataTable dataTable) {
        MortgageInterestRateListResponse expected = new MortgageInterestRateListResponse(
                dataTable.asMaps(String.class, String.class).stream()
                .map(m -> new MortgageInterestRateResponse(
                        Integer.parseInt(m.get("maturityPeriod")), Double.parseDouble(m.get("interestRate"))))
                .toList());
        MortgageInterestRateListResponse actual = getLastResponse(MortgageInterestRateListResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Then("client receives check mortgage response with {word}, {double}")
    public void clientReceivesCheckMortgageResponse(String mortgagePossible, double mortgageMonthlyAmount) {
        MortgageCheckResponse expected = new MortgageCheckResponse(Boolean.parseBoolean(mortgagePossible), mortgageMonthlyAmount);
        MortgageCheckResponse actual = getLastResponse(MortgageCheckResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Then("client receives error response with {int}, {string}")
    public void clientReceivesCheckMortgageResponse(int errorCode, String message) {
        ErrorResponse expected = new ErrorResponse(errorCode, message);
        ErrorResponse actual = getLastResponse(ErrorResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
