package com.vp.poc.patientportal.service;

import com.vp.poc.patientportal.entity.Insurance;
import com.vp.poc.patientportal.entity.Patient;
import com.vp.poc.patientportal.repository.InsuranceRepository;
import com.vp.poc.patientportal.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;
    private final ChatClient chatClient;

    private static final String userPrompt = """
            Please extract all the health insurance details from the uploaded image:
            Return an empty response if the insurance is expired. Current  for validation is: %s
            """;

    public Insurance processInsurance(MultipartFile[] files, String pid) {
        Patient patient = patientRepository.findBySsnOrPid(null, pid).orElse(null);
        if (patient == null) {
            throw new RuntimeException("Patient with PID " + pid + " not found.");
        }
        List<Media> mediaList = new ArrayList<>();
        Arrays.stream(files).forEach(file ->
                mediaList.add(new Media(MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())), file.getResource())));

        var userMsg = String.format(userPrompt, LocalDate.now());
        mediaList.forEach(media -> {
            System.out.println(media.getMimeType());
        });
        Insurance insurance = chatClient.prompt(new Prompt(new UserMessage(userMsg, mediaList)))
                .call()
                .entity(Insurance.class);

        System.out.println("Insurance: " + insurance);
        if (insurance != null && insurance.getPolicyNumber()!=null){
            insurance.setPatient(patient);
            return insuranceRepository.save(insurance);
        }
        return Insurance.builder().build();
    }

    public Insurance saveInsuranceDetails(Insurance insurance, Patient patient) {
        // Find insurance by patient PID correctly
        Optional<Insurance> existingInsurance = insuranceRepository.findByPid(patient.getPid());

        Insurance insuranceToSave;
        if (existingInsurance.isPresent()) {
            insuranceToSave = existingInsurance.get();
        } else {
            insuranceToSave = new Insurance();
            insuranceToSave.setPatient(patient); // Correctly associate the patient
        }

        // Set insurance details
        insuranceToSave.setPolicyholderName(insurance.getPolicyholderName());
        insuranceToSave.setPolicyNumber(insurance.getPolicyNumber());
        insuranceToSave.setInsuranceProvider(insurance.getInsuranceProvider());
        insuranceToSave.setGroupNumber(insurance.getGroupNumber());
        insuranceToSave.setCoverageStartDate(insurance.getCoverageStartDate());
        insuranceToSave.setCoverageEndDate(insurance.getCoverageEndDate());
        insuranceToSave.setInsuredAddress(insurance.getInsuredAddress());
        insuranceToSave.setProviderContact(insurance.getProviderContact());
        insuranceToSave.setPlanType(insurance.getPlanType());

        return insuranceRepository.save(insuranceToSave);
    }

}