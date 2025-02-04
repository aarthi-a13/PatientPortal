package com.vp.poc.patientportal.controller;

import com.vp.poc.patientportal.entity.Patient;
import com.vp.poc.patientportal.repository.PatientRepository;
import com.vp.poc.patientportal.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/enrollment")
@Log4j2
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final PatientRepository patientRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> processEnrollment(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "No files uploaded"));
        }
        Patient patient = enrollmentService.processEnrollment(files);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/details")
    public ResponseEntity<?> getEnrollmentDetails(@RequestParam String patientId) {
        Optional<Patient> patient = patientRepository.findBySsnOrPid(patientId, patientId);
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Patient.builder().build()));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitEnrollment(@RequestBody Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findBySsnOrPid(patient.getSsn(), patient.getPid());

        if (existingPatient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found. Please upload the form first.");
        }

        // Update patient details if needed
        Patient updatedPatient = existingPatient.get();
        updatedPatient.setAddress(patient.getAddress());
        updatedPatient.setContactNumber(patient.getContactNumber());
        updatedPatient.setEmergencyContact(patient.getEmergencyContact());
        updatedPatient.setMedicalHistory(patient.getMedicalHistory());
        updatedPatient.setPrimaryPhysician(patient.getPrimaryPhysician());

        patientRepository.save(updatedPatient);
        return ResponseEntity.ok(Map.of("message", "Enrollment submitted successfully.", "pid", updatedPatient.getPid()));
    }

}