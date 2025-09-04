package com.example.springaidemo.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.embedding.Embedding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class RagConfig {
    private static final Logger log = LoggerFactory.getLogger(RagConfig.class);

    private final String vectorStoreName = "vectorstore.json";



    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = getVectorStoreFile();
        try {
            boolean created = vectorStoreFile.createNewFile();
            if (created) {
                log.info("Created vector store file at {}", vectorStoreFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create vector store file", e);
        }
        return simpleVectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        File directory = path.toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String absolutePath = directory.getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }
}
