package com.example.springaidemo.config;

import com.example.springaidemo.advisors.TokenUsageAdvisor;
import com.example.springaidemo.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TimeChatClientBuilder {

    @Bean("TimeChatClient")
    public ChatClient chatClientWithoutRAA(ChatClient.Builder chatClientBuilder,
                                           ChatMemory chatMemory,
                                           TimeTools timeTools){
        Advisor advisor = new SimpleLoggerAdvisor();
        Advisor tokenAdvisor = new TokenUsageAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultTools(timeTools)
                .defaultAdvisors(List.of(advisor, memoryAdvisor, tokenAdvisor))
                .build();
    }


}
