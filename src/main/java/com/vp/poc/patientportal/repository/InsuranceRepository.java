package com.vp.poc.patientportal.repository;

import com.vp.poc.patientportal.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<Insurance, String> {
    @Query("SELECT i FROM Insurance i WHERE i.patient.pid = :patientId")
    Optional<Insurance> findByPid(@Param("patientId") String patientId);

}