package com.mortgages.testassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mortgages.testassignment.model.RequestResponseModel.MortgageCheckRequest;
import com.mortgages.testassignment.repository.entity.MortgageInterestRate;
import com.mortgages.testassignment.service.MortgageCheckService;
import com.mortgages.testassignment.service.MortgageInterestRateService;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MortgageController.class)
public class MortgageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MortgageInterestRateService mortgageInterestRateService;
    @MockitoBean
    private MortgageCheckService mortgageCheckService;

    @Test
    @SneakyThrows
    public void testGetMortgageInterestRatesShouldReturnInterestRates() {
        MortgageInterestRate mortgageInterestRate = new MortgageInterestRate();
        mortgageInterestRate.setId(1L);
        mortgageInterestRate.setMaturityPeriodInYears(5);
        mortgageInterestRate.setAnnualInterestRatePercentage(3.5);
        ZonedDateTime now = ZonedDateTime.now();
        mortgageInterestRate.setLastUpdate(now);

        when(mortgageInterestRateService.getInterestRates()).thenReturn(Collections.singletonList(mortgageInterestRate));

        this.mockMvc.perform(get("/api/interest-rates")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.interestRates[0].maturityPeriodInYears", is(5)))
                .andExpect(jsonPath("$.interestRates[0].interestRatePercentage", is(3.5)))
                .andExpect(jsonPath("$.interestRates[0].lastUpdateInUTC", Matchers.not(is(emptyString()))));
        verify(mortgageInterestRateService, times(1)).getInterestRates();
    }

    @Test
    @SneakyThrows
    public void testMortgageCheckShouldReturnMonthlyMortgageAmount() {
        when(mortgageCheckService.getMonthlyCost(eq(1.0), eq(1), eq(1.0), eq(1.0)))
                .thenReturn(Optional.of(1.0));

        MortgageCheckRequest request = new MortgageCheckRequest(1.0, 1, 1.0, 1.0);
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/mortgage-check")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mortgagePossible", is(true)))
                .andExpect(jsonPath("$.monthlyCost", is(1.0)));
        verify(mortgageCheckService, times(1))
                .getMonthlyCost(eq(1.0), eq(1), eq(1.0), eq(1.0));
    }

    @Test
    @SneakyThrows
    public void testMortgageCheckWhenInvalidArgumentShouldReturnBadRequest() {
        MortgageCheckRequest request = new MortgageCheckRequest(-1, 100, 0, -100);
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/mortgage-check")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(101)))
                .andExpect(jsonPath("$.message", is(not(emptyString()))));
    }
}
