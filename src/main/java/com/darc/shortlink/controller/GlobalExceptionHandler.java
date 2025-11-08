package com.darc.shortlink.controller;

import com.darc.shortlink.domain.exceptions.ShortLinkNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShortLinkNotFoundException.class)
    String handleShortLinkNotFoundException(ShortLinkNotFoundException ex) {
        logger.error("Short URL not found: {}", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    String handleException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        return "error/500";
    }
}
