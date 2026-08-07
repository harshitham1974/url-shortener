package com.urls.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UrlRequest {

    @NotBlank(message = "URL must not be empty")
    @Pattern(regexp = "^(https?://).*", message = "URL must start with http:// or https://")
    private String url;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
