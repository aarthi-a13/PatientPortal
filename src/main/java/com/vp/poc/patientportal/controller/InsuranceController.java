package com.vp.poc.patientportal.controller;

import com.vp.poc.patientportal.entity.Insurance;
import com.vp.poc.patientportal.entity.Patient;
import com.vp.poc.patientportal.repository.InsuranceRepository;
import com.vp.poc.patientportal.repository.PatientRepository;
import com.vp.poc.patientportal.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/insurance")
@Log4j2
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> processInsurance(@RequestParam("files") MultipartFile[] files,
                                                      @RequestParam("pid") String pid) {
        Insurance insurance = insuranceService.processInsurance(files, pid);
        if (insurance.getPolicyNumber()!=null)
            return ResponseEntity.ok(insurance);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid insurance or no data can be extracted");
    }

    @GetMapping("/details")
    public ResponseEntity<?> getInsuranceDetails(@RequestParam String patientId) {
        Optional<Insurance> insurance = insuranceRepository.findByPid(patientId);
        return insurance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Insurance.builder().build()));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitInsurance(@RequestBody Insurance insurance) {
        Optional<Patient> existingPatient = patientRepository.findByPid(insurance.getPatient().getPid());

        if (existingPatient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found. Please upload the insurance card first.");
        }

        Insurance savedInsurance = insuranceService.saveInsuranceDetails(insurance, existingPatient.get());
        return ResponseEntity.ok(Map.of("message", "Insurance details submitted successfully."));
    }

}