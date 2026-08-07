package com.urls.urlshortener.controller;

import com.urls.urlshortener.dto.UrlRequest;
import com.urls.urlshortener.dto.UrlResponse;
import com.urls.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService service;

    public UrlShortenerController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlResponse> shorten(@Valid @RequestBody UrlRequest request) {
        String shortUrl = service.createShortUrl(request.getUrl());
        return ResponseEntity.ok(new UrlResponse(shortUrl, request.getUrl()));
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = service.getOriginalUrl(code);
        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .location(URI.create(originalUrl))
                .build();
    }
}
