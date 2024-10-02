package com.defectus.smartrecruiter.service;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.OllamaChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PertinenceService {

    private static final Logger logger = LoggerFactory.getLogger(PertinenceService.class);

    private final OllamaChatModel chatModel;

    @Autowired
    public PertinenceService(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public double calculatePertinence(String cvText, String jobDescription) {
        // Prepare the strict evaluation prompt with specific guidelines
        String promptText = "You are an expert in recruitment. Evaluate the relevance of the following CV in comparison to the job description based strictly on required skills, experience, and qualifications. Only give high scores if the CV matches the job description's core requirements closely. Use a scoring system where 0 indicates no match, and 100 indicates a perfect match:\n\n"
                + "CV:\n" + cvText + "\n\n"
                + "Job Description:\n" + jobDescription + "\n\n"
                + "Provide a strict pertinence score between 0 and 100, with detailed reasoning.";

        logger.info("Sending the following prompt to the model: {}", promptText);

        // Generate response from the Ollama model
        ChatResponse response = chatModel.call(new Prompt(promptText));

        // Log the raw response for debugging
        logger.info("Raw response from the model: {}", response);

        // Try converting the ChatResponse to string
        String responseText = extractResponseText(response);

        // Log the extracted response text
        logger.info("Extracted response text: {}", responseText);

        // Extract and return the pertinence score from the response text
        return extractPertinenceScore(responseText);
    }

    // Extract the response text from ChatResponse
    private String extractResponseText(ChatResponse response) {
        // Since getMessages() is not available, use toString() or other available methods
        return response != null ? response.toString() : ""; // If toString() returns the full response text
    }

    // Extract pertinence score from the response text
    // Extract pertinence score from the response text
    private double extractPertinenceScore(String responseText) {
        try {
            // Use regex to find the first occurrence of a number between 0 and 100 in the response
            Pattern pattern = Pattern.compile("\\b(\\d{1,2}|100)\\b");
            Matcher matcher = pattern.matcher(responseText);
            if (matcher.find()) {
                String scoreString = matcher.group(0);
                logger.info("Extracted pertinence score: {}", scoreString);
                return Double.parseDouble(scoreString);
            } else {
                logger.warn("No valid pertinence score found in the response: {}", responseText);
                return -1;
            }
        } catch (NumberFormatException e) {
            logger.error("Failed to extract pertinence score from response: {}", responseText, e);
            return -1; // Handle error appropriately
        }
    }
}

