package com.swl.handler;

import com.swl.payload.response.ErrorResponse;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class ApplicationControllerAdvice {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlePageNotFoundException(MethodArgumentNotValidException ex) {

        ErrorResponse errorMessage = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.BAD_REQUEST.toString())
                .errors(ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> handleException(Exception ex) {

        ErrorResponse errorMessage = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.BAD_REQUEST.toString())
                .errors(new ArrayList<>(Collections.singletonList(ex.getMessage())))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    protected ResponseEntity<?> handleConflictException(HttpClientErrorException ex, WebRequest request) {

        ErrorResponse errorMessage = ErrorResponse.builder()
                .timestamp(LocalDate.now())
                .status(HttpStatus.CONFLICT.toString())
                .errors(new ArrayList<>(Collections.singletonList(ex.getMessage())))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }
}
