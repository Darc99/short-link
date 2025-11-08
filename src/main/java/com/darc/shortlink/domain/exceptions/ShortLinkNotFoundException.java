package com.darc.shortlink.domain.exceptions;

public class ShortLinkNotFoundException extends RuntimeException {
    public ShortLinkNotFoundException(String message) {
        super(message);
    }
}
