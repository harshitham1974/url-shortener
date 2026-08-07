package com.urls.urlshortener.controller;

import com.urls.urlshortener.dto.UrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shortenAndRedirect() throws Exception {
        UrlRequest request = new UrlRequest();
        request.setUrl("https://www.example.com");
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/shorten")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").exists());
    }

    @Test
    void rejectsInvalidUrl() throws Exception {
        mockMvc.perform(post("/api/shorten")
                        .contentType("application/json")
                        .content("{\"url\":\"not-a-url\"}"))
                .andExpect(status().isBadRequest());
    }
}