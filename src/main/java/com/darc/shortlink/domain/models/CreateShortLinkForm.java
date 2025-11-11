package com.darc.shortlink.domain.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateShortLinkForm(
        @NotBlank(message = "Original URL is required")
        String originalUrl,
        Boolean isPrivate,
        @Min(1)
        @Max(365)
        Integer expirationInDays) {
}
