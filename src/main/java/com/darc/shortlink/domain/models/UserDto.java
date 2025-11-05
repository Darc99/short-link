package com.darc.shortlink.domain.models;

import java.io.Serializable;

public record UserDto(Long id, String name) implements Serializable {
}
