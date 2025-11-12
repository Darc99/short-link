package com.darc.shortlink.controller;

import com.darc.shortlink.domain.exceptions.ShortLinkNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException ex) {

        // Check if the missing resource is the favicon request
        if (ex.getResourcePath() != null && ex.getResourcePath().contains("favicon.ico")) {
            // Log at DEBUG or TRACE level, or not at all, to keep logs clean
            logger.debug("Suppressing NoResourceFoundException for expected missing resource: {}", ex.getResourcePath());

            // Return a simple 404 NOT FOUND response
            return ResponseEntity.notFound().build();
        }

        // For all other missing resources, log the error and return a generic 500 or 404
        logger.error("Unhandled NoResourceFoundException for path: {}", ex.getResourcePath(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
