package com.vp.poc.patientportal.model;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonClassDescription("Holds insurance details of a user.")
public class Insurance {

    private String firstName;
    private String lastName;
    @JsonPropertyDescription("The policy provider's company name")
    private String policyProvider;
    @JsonPropertyDescription("The unique policy provider's number or ID")
    private String providerID;
    @JsonPropertyDescription("The unique policy holder or subscriber's number or ID")
    private String policyNumber;
    @JsonPropertyDescription("The policy/plan name")
    private String policyPlanName;
    @JsonPropertyDescription("The policy holder's address line1")
    private String addressLine1;
    @JsonPropertyDescription("The policy holder's address line2")
    private String addressLine2;
    @JsonPropertyDescription("The policy holder's city")
    private String city;
    @JsonPropertyDescription("The policy holder's state")
    private String state;
    @JsonPropertyDescription("The policy holder's zip code")
    private String zip;
    @JsonPropertyDescription("The date from which the policy is effective")
    private String effectiveDate;
    @JsonPropertyDescription("The date until the policy can be utilized")
    private String expiryDate;
    private PolicyPremiumAllocation premiumAllocation;
}
