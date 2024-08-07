package com.masagal.masaban_server.http;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;

@ControllerAdvice
public class MasabanControllerAdvice {

    Logger logger = LogManager.getLogger();

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Void> handleInvalidTypes(MethodArgumentTypeMismatchException ex) {
        logger.error("got exception of type MethodArgumentTypeMismatchException: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({NoSuchElementException.class, IndexOutOfBoundsException.class})
    protected ResponseEntity<Void> handleNotFound(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Void> handleInvalidInput(IllegalArgumentException ex) {
        logger.error("got exception of type IllegalArgumentException: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

}
