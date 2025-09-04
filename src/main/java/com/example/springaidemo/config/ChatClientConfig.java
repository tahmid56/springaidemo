package com.example.springaidemo.config;

import com.example.springaidemo.advisors.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient chatClient( ChatClient.Builder chatClientBuilder){
        ChatOptions chatOptions = ChatOptions
                .builder()
//                .model("ai/gemma3:270M-F16")
                .maxTokens(100)
                .temperature(0.8).build();
        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAdvisor()))
                .defaultSystem("""
                        You are an internal HR assistant. Your role is to help \s
                        employees with questions related to HR policies, such as \s
                        leave policies, working hours, benefits, and code of conduct.
                        If a user asks for help with anything outside of these topics, \s
                        kindly inform them that you can only assist with queries related to\s
                        HR policies.
                        """)
                .defaultUser("How can you help me?")
                .build();
    }
}
