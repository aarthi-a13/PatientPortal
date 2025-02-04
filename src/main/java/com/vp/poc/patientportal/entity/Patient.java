package com.vp.poc.patientportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Builder
@Table(name = "patients", uniqueConstraints = {@UniqueConstraint(columnNames = {"ssn", "pid"})})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private String ssn;

    @Column(unique = true, nullable = false)
    private String pid;  // Unique Patient ID

    private String fullName;
    private LocalDate dateOfBirth;
    @JsonPropertyDescription("Patient's sex or gender. Do not refer pronoun here")
    private String gender;
    private String address;
    private String contactNumber;
    private String emergencyContact;
    private String medicalHistory;
    private String primaryPhysician;
}