package com.mortgages.testassignment.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;

import java.time.ZonedDateTime;

@Entity
@Setter
@Getter
public class MortgageInterestRate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Integer maturityPeriod;
    private Double interestRate;
    @CurrentTimestamp(event = {EventType.INSERT, EventType.UPDATE}, source = SourceType.VM)
    private ZonedDateTime lastUpdate;
}
