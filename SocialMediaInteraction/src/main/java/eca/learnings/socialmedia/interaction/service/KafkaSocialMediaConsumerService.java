package eca.learnings.socialmedia.interaction.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaSocialMediaConsumerService {

    //private NlpAnalyser nlpAnalyzer = new NlpAnalyser();

    @KafkaListener(topics = "social_media_topic")
    public void listen(String message) {
        System.out.println("Received message: " + message);
/*
        // Analyze the incoming message using NLP
        String[] sentences = nlpAnalyzer.detectSentences(message);
        Map<String, Integer> tokenFrequencyMap = new HashMap<>();

        for (String sentence : sentences) {
            String[] tokens = nlpAnalyzer.tokenizeSentence(sentence);

            // Count frequency of each token
            for (String token : tokens) {
                tokenFrequencyMap.put(token, tokenFrequencyMap.getOrDefault(token, 0) + 1);
            }
        }

        // Print token frequencies
        System.out.println("Token Frequencies:");
        for (Map.Entry<String, Integer> entry : tokenFrequencyMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Extract keywords (e.g., tokens with high frequency)
        int keywordThreshold = 2; // Adjust this threshold based on your requirements
        System.out.println("\nKeywords:");
        for (Map.Entry<String, Integer> entry : tokenFrequencyMap.entrySet()) {
            if (entry.getValue() >= keywordThreshold) {
                System.out.println(entry.getKey());
            }
        }*/
    }
}
