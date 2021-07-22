package com.challenge.yaca.commitViewer.controller;

import com.challenge.yaca.commitViewer.service.CommitViewerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.challenge.yaca.commitViewer.client.CommandLineClient.CommandExecuteException;
import static com.challenge.yaca.commitViewer.client.CommandLineClient.DateParseException;

/**
 * Exceptions are handled here
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    static final Logger logger = LoggerFactory.getLogger(CommitViewerService.class);

    @ExceptionHandler(CommandExecuteException.class)
    public ResponseEntity<?> handleCommandExecuteException(CommandExecuteException e) {
        logger.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(DateParseException.class)
    public ResponseEntity<?> handleDateParseException(DateParseException e) {
        logger.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}

