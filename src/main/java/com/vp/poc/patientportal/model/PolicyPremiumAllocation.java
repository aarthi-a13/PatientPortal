package com.vp.poc.patientportal.model;

import com.fasterxml.jackson.annotation.JsonClassDescription;

@JsonClassDescription("Holds allocated policy premium percentage")
public record PolicyPremiumAllocation(
        String primaryCare,
        String specialist,
        String urgentCare,
        String emergencyRoom) {
}
