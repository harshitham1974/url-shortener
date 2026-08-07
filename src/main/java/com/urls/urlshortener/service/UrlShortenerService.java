package com.urls.urlshortener.service;

import com.urls.urlshortener.entity.UrlMapping;
import com.urls.urlshortener.repository.UrlMappingRepository;
import com.urls.urlshortener.util.Base62Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository repository;

    @Value("${app.base-url}")
    private String baseUrl;

    public UrlShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public String createShortUrl(String originalUrl) {
        // If this URL was already shortened before, reuse the same code
        Optional<UrlMapping> existing = repository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return baseUrl + "/r/" + existing.get().getShortCode();
        }

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode("temp"); // placeholder, fixed right after save
        mapping = repository.save(mapping); // save once to get an auto-generated ID

        String shortCode = Base62Encoder.encode(mapping.getId());
        mapping.setShortCode(shortCode);
        repository.save(mapping); // save again with the real short code

        return baseUrl + "/r/" + shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found: " + shortCode));
        mapping.setClickCount(mapping.getClickCount() + 1);
        repository.save(mapping);
        return mapping.getOriginalUrl();
    }
}
