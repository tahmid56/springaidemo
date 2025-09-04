package com.example.springaidemo.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class HRPolicyLoader {
    private final SimpleVectorStore vectorStore;
    @Value("classpath:HR_Policies.pdf")
    Resource policyFile;
    private final String vectorStoreName = "vectorstore.json";


    public HRPolicyLoader(SimpleVectorStore simpleVectorStore) {
        this.vectorStore = simpleVectorStore;
    }

    @PostConstruct
    public void loadPdf() {

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyFile);
        List<Document> docs = tikaDocumentReader.get();

        TextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(100).withMaxNumChunks(400).build();

        vectorStore.add(textSplitter.split(docs));
    }
}
