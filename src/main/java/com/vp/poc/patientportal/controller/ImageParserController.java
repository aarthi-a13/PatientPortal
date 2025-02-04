package com.vp.poc.patientportal.controller;

import com.vp.poc.patientportal.model.Insurance;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequestMapping("/insurance/parse")
@Log4j2
public class ImageParserController {

    private static final String userPrompt = """
            Please extract the following insurance details from the provided scanned image or file of the insurance card:
            policyholder's name, policy number, insurance provider information, coverage dates, address and any other relevant information.
            Return an empty response if the image is unclear and do not generate random information.
            """;

    private final ChatClient chatClient;
    @Value("classpath:/data/insurance.png")
    private Resource imageResource;

    public ImageParserController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /*
    Gets multipart file -> encodes into byte array and decodes back to image resource.
    Encoding and filetype restriction can be done from UI end.
    The API should be updated to accept byte array and file type (image/jpeg , image/png, application/pdf)
    */
    @PostMapping("/upload")
    public Insurance parseInsuranceCard(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        var decodedImage = new ByteArrayResource(decodedBytes);

        var userMessage = new UserMessage(userPrompt,
                new Media(MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())), file.getResource()));

        return chatClient.prompt(new Prompt(userMessage))
                .call()
                .entity(Insurance.class);
    }

// ----------------References to work with PDF if needed--------------------
    /*
    * https://www.baeldung.com/pdf-conversions-java#1-pdf-to-image
    * */

}
