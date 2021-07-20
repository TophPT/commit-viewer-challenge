package com.challenge.yaca.commitViewer.controller;

import com.challenge.yaca.commitViewer.service.GitCLIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.challenge.yaca.commitViewer.utils.Commands.CommandExecuteException;

/**
 * Exceptions are handled here
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    static final Logger logger = LoggerFactory.getLogger(GitCLIService.class);

    @ExceptionHandler(CommandExecuteException.class)
    public ResponseEntity<?> handleCommandExecuteException(CommandExecuteException e) {
        logger.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}

