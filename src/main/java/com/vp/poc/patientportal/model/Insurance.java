package com.vp.poc.patientportal.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Description;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Description("Holds insurance details of a user.")
public class Insurance {

    private String firstName;
    private String lastName;
    private String policyProvider;
    private String policyNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
    private String effectiveDate;
    private String expiryDate;
}
