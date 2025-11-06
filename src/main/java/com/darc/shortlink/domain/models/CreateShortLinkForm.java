package com.darc.shortlink.domain.models;

import jakarta.validation.constraints.NotBlank;

public record CreateShortLinkForm(
        @NotBlank(message = "Original URL is required")
        String originalUrl) {
}
