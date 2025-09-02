package com.example.springaidemo.controllers;


import com.example.springaidemo.models.CountryCities;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StructuredOutputController {
    private final ChatClient chatClient;

    public StructuredOutputController(ChatClient.Builder chatClientBuildert){
        this.chatClient = chatClientBuildert
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/structured-entity")
    public ResponseEntity<CountryCities> chat(@RequestParam("message") String message){
        CountryCities countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(CountryCities.class);
        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/structured-entity-list")
    public ResponseEntity<List<String>> chatList(@RequestParam("message") String message){
        List<String> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ListOutputConverter());
        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/structured-entity-map")
    public ResponseEntity<Map<String, Object>> chatMap(@RequestParam("message") String message){
        Map<String, Object> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new MapOutputConverter());
        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/structured-entity-list-complex")
    public ResponseEntity<List<CountryCities>> chatComplexList(@RequestParam("message") String message){
        List<CountryCities> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ParameterizedTypeReference<List<CountryCities>>(){
                });
        return ResponseEntity.ok(countryCities);
    }
}
