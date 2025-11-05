package com.darc.shortlink.domain.models;

import java.io.Serializable;
import java.time.Instant;

public record ShortLinkDto(Long id, String shortKey, String originalUrl,
                          Boolean isPrivate, Instant expiresAt,
                          UserDto createdBy, Long clickCount,
                          Instant createdAt) implements Serializable {
}
