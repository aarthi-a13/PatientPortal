package com.vp.poc.patientportal.service;

import com.vp.poc.patientportal.entity.Patient;
import com.vp.poc.patientportal.exceptions.ParsedDataException;
import com.vp.poc.patientportal.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final PatientRepository patientRepository;
    private final ChatClient chatClient;

    private static final String userPrompt = """
            Please extract the following patient enrollment details from the uploaded scanned image:
            SSN, fullName, dateOfBirth, gender, address, contactNumber, emergencyContact, medicalHistory, primaryPhysician and any other relevant information.
            Return an empty response if the image is unclear and do not generate random information.
            """;

    public Patient processEnrollment(MultipartFile[] files) {

        List<Media> mediaList = new ArrayList<>();
        Arrays.stream(files).forEach(file ->
                mediaList.add(new Media(MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())), file.getResource())));

        Patient patient = chatClient.prompt(new Prompt(new UserMessage(userPrompt, mediaList)))
                .call()
                .entity(Patient.class);

        if (patient == null || patient.getFullName().isBlank())
            throw new ParsedDataException("Enrollment form processing failed: No valid details extracted.");

        String uniquePID;
        do {
            uniquePID = generatePID();
        } while (patientRepository.existsByPid(uniquePID));
        patient.setPid(uniquePID);

        System.out.println(patient);
        return patientRepository.save(patient);
    }

    private String generatePID() {
        Random random = new Random();
        return "PID" + (100000 + random.nextInt(900000));
    }
}