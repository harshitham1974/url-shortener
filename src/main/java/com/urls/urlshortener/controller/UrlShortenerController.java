package com.urls.urlshortener.controller;

import com.urls.urlshortener.dto.UrlRequest;
import com.urls.urlshortener.dto.UrlResponse;
import com.urls.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Shorten a URL",
            description = "Creates a short URL from the supplied original URL."
    )

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlResponse> shorten(@Valid @RequestBody UrlRequest request) {
        String shortUrl = service.createShortUrl(request.getUrl());
        return ResponseEntity.ok(new UrlResponse(shortUrl, request.getUrl()));
    }

    @Operation(
            summary = "Redirect to original URL",
            description = "Redirects a short URL to its original URL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect successful"),
            @ApiResponse(responseCode = "404", description = "Short URL not found")
    })

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = service.getOriginalUrl(code);
        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .location(URI.create(originalUrl))
                .build();
    }
}
