package it;

import com.mortgages.testassignment.model.RequestResponseModel.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Slf4j
public class StepDefs extends RestCallHandler {

    @When("client makes get interest rate request")
    public void clientMakesGetInterestCall() {
        executeGetEntity("http://localhost:8080/api/interest-rates");
    }

    @When("client makes check mortgage request with {double}, {int}, {double}, {double}")
    public void clientMakesPostMortgageCheck(double income, int maturityPeriod, double loanValue, double homeValue) {
        executePostEntity("http://localhost:8080/api/mortgage-check",
                new MortgageCheckRequest(income, maturityPeriod, loanValue, homeValue));
    }

    @When("client makes check mortgage request with non existing maturity period {int}")
    public void clientMakesPostMortgageCheck(int nonExistingMaturityPeriod) {
        executePostEntity("http://localhost:8080/api/mortgage-check",
                new MortgageCheckRequest(100, nonExistingMaturityPeriod, 200, 200));
    }

    @When("client makes check mortgage request with negative income")
    public void clientMakesPostMortgageCheck() {
        executePostEntity("http://localhost:8080/api/mortgage-check",
                new MortgageCheckRequest(-100, 5, 200, 200));
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
                        Integer.parseInt(m.get("maturityPeriodInYears")), Double.parseDouble(m.get("interestRatePercentage")),
                        ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .toList());
        MortgageInterestRateListResponse actual = getLastResponse(MortgageInterestRateListResponse.class);
        List<Map.Entry<MortgageInterestRateResponse, MortgageInterestRateResponse>> zippedLists = IntStream.range(0, Math.min(actual.interestRates().size(), expected.interestRates().size()))
                .mapToObj(i -> Map.entry(actual.interestRates().get(i), expected.interestRates().get(i)))
                .toList();
        zippedLists.forEach(entry -> {
            assertThat(entry.getKey().maturityPeriodInYears()).isEqualTo(entry.getValue().maturityPeriodInYears());
            assertThat(entry.getKey().interestRatePercentage()).isEqualTo(entry.getValue().interestRatePercentage());
            ZonedDateTime actualLastUpdate = ZonedDateTime.parse(entry.getKey().lastUpdateInUTC(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            ZonedDateTime expectedLastUpdate = ZonedDateTime.parse(entry.getValue().lastUpdateInUTC(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            assertThat(actualLastUpdate).isCloseTo(expectedLastUpdate, within(10, ChronoUnit.SECONDS));
        });
    }

    @Then("client receives check mortgage response with {word}, {double}")
    public void clientReceivesCheckMortgageResponse(String mortgagePossible, double monthlyCost) {
        MortgageCheckResponse expected = new MortgageCheckResponse(Boolean.parseBoolean(mortgagePossible), monthlyCost);
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
