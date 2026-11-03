package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretCheckController {

    @Value("${MY_API_KEY:}")
    private String apiKey;

    @GetMapping("/secret-check")
    public String check() {
        if (apiKey.isEmpty()) return "MY_API_KEY is NOT set";
        if (apiKey.startsWith("@Microsoft.KeyVault")) return "Reference NOT resolved (check role and URI)";
        return "Secret loaded, length = " + apiKey.length();
    }
}