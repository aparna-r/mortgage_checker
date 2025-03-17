package perf;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MortgageApplicationSimulation extends Simulation {
    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();

    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildMortgageCheckScenario();
    private static final ScenarioBuilder GET_SCENARIO_BUILDER = buildGetMortgageInterestRateScenario();


    public MortgageApplicationSimulation() {
        setUp(
                GET_SCENARIO_BUILDER
                        .injectOpen(
                                nothingFor(1),
                                atOnceUsers(10),
                                rampUsers(10).during(5),
                                constantUsersPerSec(10).during(5),
                                rampUsersPerSec(10).to(0).during(5),
                                stressPeakUsers(10).during(5))
                        .protocols(HTTP_PROTOCOL_BUILDER),
                POST_SCENARIO_BUILDER
                        .injectOpen(
                                nothingFor(1),
                                atOnceUsers(10),
                                rampUsers(10).during(5),
                                constantUsersPerSec(10).during(5),
                                rampUsersPerSec(10).to(0).during(5),
                                stressPeakUsers(10).during(5))
                        .protocols(HTTP_PROTOCOL_BUILDER))
            .assertions(
                    global().responseTime().max().lte(10000),
                    global().successfulRequests().percent().gt(90d));
    }

    private static HttpProtocolBuilder setupProtocolForSimulation() {
        return http.baseUrl("http://localhost:8080")
                .acceptHeader("application/json")
                .maxConnectionsPerHost(10)
                .userAgentHeader("Gatling/Performance Test");
    }

    private static ScenarioBuilder buildMortgageCheckScenario() {
        return CoreDsl.scenario("Load Test - Mortgage Check")
                .feed(csv("perf/mortgage-check.csv").random())
                .exec(http("mortgage-check-request").post("/api/mortgage-check")
                        .header("Content-Type", "application/json")
                        .body(StringBody("{ \"income\": \"#{income}\", \"maturityPeriodInYears\": \"#{maturityPeriodInYears}\", \"loanValue\": \"#{loanValue}\", \"homeValue\": \"#{homeValue}\" }"))
                        .check(status().is(200))
                        .check(jsonPath("$.mortgagePossible").exists()));
    }

    private static ScenarioBuilder buildGetMortgageInterestRateScenario() {
        return CoreDsl.scenario("Load Test - Mortgage Get Interest")
                .exec(http("mortgage-interest-rate-request").get("/api/interest-rates")
                        .check(status().is(200))
                        .check(jsonPath("$.interestRates").exists()));
    }
}
