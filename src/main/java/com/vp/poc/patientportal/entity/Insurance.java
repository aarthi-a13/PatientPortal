package com.vp.poc.patientportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Builder
@Table(name = "insurance")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Insurance {
    @Id
    @JsonPropertyDescription("The policy number/ID")
    private String policyNumber;

    @ManyToOne
    @JoinColumn(name = "patient_pid", referencedColumnName = "pid", nullable = false)
    @JsonIgnore
    private Patient patient;

    @JsonPropertyDescription("The policy holder's/insured's full name")
    private String policyholderName;

    @JsonPropertyDescription("The policy/insurance provider's name")
    private String insuranceProvider;
    @JsonPropertyDescription("The policy group number")
    private String groupNumber;
    @JsonPropertyDescription("The date from which the policy is effective")
    private LocalDate coverageStartDate;
    @JsonPropertyDescription("The date until the policy can be utilized")
    private LocalDate coverageEndDate;
    @JsonPropertyDescription("The policy holder's/insured's address")
    private String insuredAddress;
    @JsonPropertyDescription("The policy provider's contact")
    private String providerContact;
    @JsonPropertyDescription("The policy plan type")
    private String planType;
}
