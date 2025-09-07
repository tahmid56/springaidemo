package com.example.springaidemo.config;

import com.example.springaidemo.advisors.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryChatClientBuilder {

    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory.builder().maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    @Bean("ChatMemoryChatClient")
    public ChatClient chatClientWithoutRAA(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory){
        Advisor advisor = new SimpleLoggerAdvisor();
        Advisor tokenAdvisor = new TokenUsageAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(advisor, memoryAdvisor, tokenAdvisor))
                .build();
    }

    @Bean("ChatMemoryChatClientRetrievalAugmentationAdvisor")
    public ChatClient chatClientWithRAA(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, RetrievalAugmentationAdvisor retrievalAugmentationAdvisor){
        Advisor advisor = new SimpleLoggerAdvisor();
        Advisor tokenAdvisor = new TokenUsageAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(advisor, memoryAdvisor, tokenAdvisor, retrievalAugmentationAdvisor))
                .build();
    }

    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(SimpleVectorStore simpleVectorStore){
        return RetrievalAugmentationAdvisor.builder().documentRetriever(
                VectorStoreDocumentRetriever.builder().vectorStore(simpleVectorStore)
                        .topK(3).similarityThreshold(0.5).build()
        ).build();
    }
}
