package com.vp.poc.patientportal.repository;

import com.vp.poc.patientportal.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByPid(String pid);
    Optional<Patient> findBySsnOrPid(String ssn, String patientId);
    Optional<Patient> findByPid(String pid);

}