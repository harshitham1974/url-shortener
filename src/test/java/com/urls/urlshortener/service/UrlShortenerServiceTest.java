package com.urls.urlshortener.service;

import com.urls.urlshortener.entity.UrlMapping;
import com.urls.urlshortener.repository.UrlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private UrlMappingRepository repository;

    @InjectMocks
    private UrlShortenerService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "baseUrl", "http://localhost:8080");
    }

    @Test
    void createsShortUrlForNewLink() {
        when(repository.findByOriginalUrl("https://example.com"))
                .thenReturn(Optional.empty());

        UrlMapping saved = new UrlMapping();
        saved.setId(1L);
        when(repository.save(any(UrlMapping.class))).thenReturn(saved);

        String result = service.createShortUrl("https://example.com");

        assertTrue(result.startsWith("http://localhost:8080/r/"));
        verify(repository, times(2)).save(any(UrlMapping.class));
    }

    @Test
    void reusesExistingShortCodeForSameUrl() {
        UrlMapping existing = new UrlMapping();
        existing.setShortCode("abc123");
        when(repository.findByOriginalUrl("https://example.com"))
                .thenReturn(Optional.of(existing));

        String result = service.createShortUrl("https://example.com");

        assertEquals("http://localhost:8080/r/abc123", result);
        verify(repository, never()).save(any());
    }

    @Test
    void throwsWhenShortCodeNotFound() {
        when(repository.findByShortCode("missing")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getOriginalUrl("missing"));
    }
}