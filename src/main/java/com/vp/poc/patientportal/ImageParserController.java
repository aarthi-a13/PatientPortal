package com.vp.poc.patientportal;

import com.vp.poc.patientportal.model.Insurance;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insurance/parse")
@Log4j2
public class ImageParserController {

    private final ChatClient chatClient;

    @Value("classpath:/data/insurance.png")
    private Resource imageResource;

    public ImageParserController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    public Insurance parseInsuranceCard(){

        var userMessage = new UserMessage("Please extract the following insurance details from the provided scanned image of the insurance card: " +
                "policyholder's name, policy number, insurance provider, coverage dates, address and any other relevant information.",
                new Media(MimeTypeUtils.IMAGE_PNG, this.imageResource));

        return chatClient.prompt(new Prompt(userMessage))
                .call()
                .entity(Insurance.class);
    }


}
